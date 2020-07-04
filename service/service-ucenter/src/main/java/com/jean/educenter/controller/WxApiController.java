package com.jean.educenter.controller;

import com.google.gson.Gson;
import com.jean.commonutils.JwtUtils;
import com.jean.educenter.entity.UcenterMember;
import com.jean.educenter.service.UcenterMemberService;
import com.jean.educenter.utils.ConstantWxUtils;
import com.jean.educenter.utils.HttpClientUtils;
import com.jean.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.HashMap;

@RequestMapping("/api/ucenter/wx")
@Controller  //只是请求地址，不需要返回数据，所以不用@RestController
@CrossOrigin
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    //2、获取扫描人信息，添加数据
    @GetMapping("/callback")
    public String callback(String code,String state)
    {
        try {
            //1、获取code值，也就是临时票据，类似于验证码

            //2、拿着code，请求微信的一个回定地址，就会得到两个值access_token和openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            //拼接三个参数：id 、秘钥 和code值
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code);

            //请求这个拼接好的地址，得到返回的两个值 access_token和openid
            //使用httpclient发送请求，得到返回结果
            //accessTokenInfo{"access_token":"34_BfXUb2zdvFcfDaj9XCFed3omhS3qoJo9Ru3hkcc9E86OZs0hq0pSgSlo11vg3_6mmAQHQ6sK7bfoR2H1qlbnogUm34ae4V88mjdM0-waFa0","expires_in":7200,"refresh_token":"34_gzf9qt3lyk9G88yktjzUCH2sJRF_h8XhUjR4rgoHHlVXpSSvtOFRK6Hp0I_C6sChNMY7VT1_GITvEItW1zEygVu0VTUaWRbr9qM4NDKOgNc","openid":"o3_SC51phD5ZyEDdSgmGpGYPpqqk","scope":"snsapi_login","unionid":"oWgGz1KI4ijq70Ecjm8_9FCfWesQ"}
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
//            System.out.println("accessTokenInfo"+accessTokenInfo);
            //从accessTokenInfo字符串中获取出两个值 access_token和openid
            //把accessTokenInfo字符串转换成map集合，根据map里面key获取对应值
            //使用json转换工具Gson
            Gson gson = new Gson();
            HashMap map = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String)map.get("access_token");
            String openid = (String)map.get("openid");


            //把扫描人信息添加到数据库中
            //判断数据库的表中是否存在相同的微信信息，根据openid可以判断
            UcenterMember member= memberService.getOpenIdMember(openid);
            if(member==null){ //member是空，表示：表里面没有相同的微信信息，进行添加到数据库表中

                //3、拿着access_token和openid，再去请求微信的一个固定地址，获取扫描人的信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接两个参数
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                //发送请求
                String userInfo = HttpClientUtils.get(userInfoUrl);
//            System.out.println("userInfo"+userInfo);
//userInfo{"openid":"o3_SC51phD5ZyEDdSgmGpGYPpqqk","nickname":"辰露Jean","sex":2,"language":"zh_CN","city":"Hangzhou","province":"Zhejiang","country":"CN","headimgurl":"http:\/\/thirdwx.qlogo.cn\/mmopen\/vi_32\/mxZFWkOAV3SEvBQ0bjvuvjicq9toHg8CbgB5pR536V4AZ8PNamqP9kiaaF2Gb0ZSWUGV7XvZ2L0UUNFIOyvJjjwQ\/132","privilege":[],"unionid":"oWgGz1KI4ijq70Ecjm8_9FCfWesQ"}
                //获取userInfo字符串中扫描人的信息
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String)userInfoMap.get("nickname"); //昵称
                String headimgurl = (String)userInfoMap.get("headimgurl");//头像

                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }

            //因为端口号不同存在跨域问题，cookie不能跨域，所以这里使用jwt的token进行传输用户信息，而不使用cookie
            //使用jwt，根据member对象生成token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());


            //最后：返回到首页面，并显示用户信息
            return "redirect:http://localhost:3000?token="+jwtToken;
        } catch (Exception e) {
            throw new GuliException(20001,"微信登录失败");
        }


    }


    //1、生成微信扫描的二维码
    @GetMapping("/login")
    public String getWxCode()
    {
        //在固定的地址后面拼接参数     方式一
//        String url="https://open.weixin.qq.com/connect/qrconnect?appid="+ ConstantWxUtils.WX_OPEN_APP_ID
//                +"&response_type=code";

        // 微信开放平台授权baseUrl  %s相当于？ 代表占位符   方式二 常用
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
        "?appid=%s" +
        "&redirect_uri=%s" +
        "&response_type=code" +
        "&scope=snsapi_login" +
        "&state=%s" +
        "#wechat_redirect";

        //对redirect_url进行URLEncode编码
        String redirectUrl=ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl= URLEncoder.encode(redirectUrl,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //设置%s里面的值
        String url=String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "jean"
        );

        //重定向到请求的微信地址去
        return "redirect:"+url;
    }




}
