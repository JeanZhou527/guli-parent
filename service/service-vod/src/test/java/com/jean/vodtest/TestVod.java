package com.jean.vodtest;

import com.aliyun.vod.upload.impl.UploadImageImpl;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadImageRequest;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadImageResponse;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;

import java.util.List;

public class TestVod {
    //账号AK信息请填写(必选)
    private static final String accessKeyId = "LTAI4GAsEY5HwsS2R56jZc8W";
    //账号AK信息请填写(必选)
    private static final String accessKeySecret = "请登录自己的阿里云中查看keysecret";

    public static void main(String[] args) throws ClientException {


        //视频标题(必选)
        String title = "firstName1";  //上传之后文件叫什么名字
        //本地文件上传和文件流上传时，文件名称为上传文件绝对路径，如:/User/sample/文件名称.mp4 (必选)
        //文件名必须包含扩展名
        String fileName = "C:\\Users\\zhouq\\Desktop\\6 - What If I Want to Move Faster.mp4";  //本地文件的路径和文件名

        //1.音视频上传-本地文件上传
        testUploadVideo(accessKeyId, accessKeySecret, title, fileName);

        //2.图片上传-本地文件上传
//        testUploadImageLocalFile(accessKeyId, accessKeySecret);
        //3、测试下，根据视频id获取视频播放凭证
        getPlayAuth();

    }


        //音视频上传-本地文件上传
        private static void testUploadVideo(String accessKeyId, String accessKeySecret, String title, String fileName) {

            UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
            /* 可指定分片上传时每个分片的大小，默认为1M字节 */
            request.setPartSize(1 * 1024 * 1024L);
            /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
            request.setTaskNum(1);
        /* 是否开启断点续传, 默认断点续传功能关闭。当网络不稳定或者程序崩溃时，再次发起相同上传请求，可以继续未完成的上传任务，适用于超时3000秒仍不能上传完成的大文件。
        注意: 断点续传开启后，会在上传过程中将上传位置写入本地磁盘文件，影响文件上传速度，请您根据实际情况选择是否开启*/
            request.setEnableCheckpoint(false);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadVideoResponse response = uploader.uploadVideo(request);
//            System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
            if (response.isSuccess()) {
                System.out.print("VideoId=" + response.getVideoId() + "\n");
            } else {
                /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
                System.out.print("VideoId=" + response.getVideoId() + "\n");
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            }
        }

        /**
         * 图片上传接口，本地文件上传示例
         */
        private static void testUploadImageLocalFile(String accessKeyId, String accessKeySecret) {
            // 图片类型（必选）取值范围：default（默认)，cover（封面），watermark(水印)
            String imageType = "cover";
            UploadImageRequest request = new UploadImageRequest(accessKeyId, accessKeySecret, imageType);
            /* 图片文件扩展名（可选）取值范围：png，jpg，jpeg */
            //request.setImageExt("png");
            /* 图片标题（可选）长度不超过128个字节，UTF8编码 */
            //request.setTitle("图片标题");
            /* 图片标签（可选）单个标签不超过32字节，最多不超过16个标签，多个用逗号分隔，UTF8编码 */
            //request.setTags("标签1,标签2");
            /* 存储区域（可选）*/
            //request.setStorageLocation("out-4f3952f78c0211e8b3020013e7.oss-cn-shanghai.aliyuncs.com");
            /* 流式上传时，InputStream为必选，fileName为源文件名称，如:文件名称.png(可选)*/
            //request.setFileName("测试文件名称.png");
            /* 开启默认上传进度回调 */
            // request.setPrintProgress(true);
            /* 设置自定义上传进度回调 (必须继承 ProgressListener) */
            // request.setProgressListener(new PutObjectProgressListener());

            UploadImageImpl uploadImage = new UploadImageImpl();
            UploadImageResponse response = uploadImage.upload(request);
            System.out.print("RequestId=" + response.getRequestId() + "\n");
            if (response.isSuccess()) {
                System.out.print("ImageId=" + response.getImageId() + "\n");
                System.out.print("ImageURL=" + response.getImageURL() + "\n");
            } else {
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            }


        }


    public static void getPlayAuth() throws ClientException {
        //根据视频id，获取视频播放凭证
        //1、创建初始化对象
        DefaultAcsClient client=InitObject.initVodClient("LTAI4GAsEY5HwsS2R56jZc8W","oGaEcWLBYXB76dlJcQd5NC9AWvIdDa");

        //2、创建获取视频凭证request和response
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        //3向request对象里面设置视频id
        request.setVideoId("c63712c246e94d0396a87e142a12232a");

        //4、调用初始化对象的方法得到视频播放凭证
        response=client.getAcsResponse(request);
        System.out.println("playauth："+response.getPlayAuth());
        //还可以设置凭证的有效期。默认时间是100秒，最大为3000秒
        request.setAuthInfoTimeout(2000L);
    }



   public static void getPlayUrl() throws ClientException {
       //根据视频id，获取视频播放地址   如果该视频不是加密视频的话，就可以直接播放了，
       //如果是加密视频还需要获得播放凭证。
       //1、创建初始化对象
       DefaultAcsClient client=InitObject.initVodClient("LTAI4GAsEY5HwsS2R56jZc8W","oGaEcWLBYXB76dlJcQd5NC9AWvIdDa");

       //2创建获取视频地址request和response
       GetPlayInfoRequest request=new GetPlayInfoRequest();
       GetPlayInfoResponse response=new GetPlayInfoResponse();

       //3向request对象里面设置视频id
       request.setVideoId("a1e0e0c91ead411d83bd048cf41181f0");

       //4调用初始化对象里面的方法，把request传递给该方法，获取数据
       response= client.getAcsResponse(request);

       List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
       //播放地址PlayURL就是播放地址，Title就是视频的名称
       for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
           System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
       }
       //Base信息
       System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
   }


}
