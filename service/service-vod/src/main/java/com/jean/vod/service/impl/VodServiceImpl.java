package com.jean.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.jean.servicebase.exceptionhandler.GuliException;
import com.jean.vod.service.VodService;
import com.jean.vod.utils.ConstantVodUtils;
import com.jean.vod.utils.InitVodClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {

    //上传视频到阿里云
    @Override
    public String uploadVideoAly(MultipartFile file) {

        try {
            //accessKeyId, accessKeySecret 用一个工具类ConstantVodUtils去读取配置文件中的值
            // fileName：上传文件的原始名称  tittle、fileName一般会设成相同
            //fileName：上传文件原始名称
            // 01.03.09.mp4
            String fileName = file.getOriginalFilename();
            //title：上传之后显示名称
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            //inputStream：上传文件输入流
            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID,ConstantVodUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            String videoId = null;
            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId = response.getVideoId();
            }
            return videoId;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    //删除多个阿里云视频
    @Override
    public void removeMoreAlyVideo(List<String> videoIdList) {

        try {
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);

            //创建一个删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();

            //把里面的元素拼成与,分隔的字符串
            String str= org.apache.commons.lang.StringUtils.join(videoIdList.toArray(),",");
            //向request中设置视频id，，可以传多个id  id1,id2,id3
            request.setVideoIds(str);

            //调用初始化对象的方法实现删除
            client.getAcsResponse(request);

        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除多个阿里云上的视频失败");
        }

    }


}
