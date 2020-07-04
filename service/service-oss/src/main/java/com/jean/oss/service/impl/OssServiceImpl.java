package com.jean.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.jean.oss.service.OssService;
import com.jean.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    //上传头像到阿里云的oss
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtils.END_POINT;

        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName=ConstantPropertiesUtils.BUCKET_NAME;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try
        {
            // 上传文件流。
            //获取上传文件的输入流
            InputStream inputStream =file.getInputStream();

            //获取文件名称
            String fileName=file.getOriginalFilename();

            //1、在文件名称里面添加一个随机唯一的值 oegg-fpwp-222u
            String uuid= UUID.randomUUID().toString().replaceAll("-","");
            fileName=uuid+fileName;

            //2、把文件按照日期进行分类  2020/05/12
            //获取当关日期，日期工具类joda-time
            String datePath = new DateTime().toString("yyyy/MM/dd");
            fileName=datePath+"/"+fileName;


            //调用oss的方法实现上传
            //第一个参数：BucketName；第二个参数：上传到oss上的文件路径或文件名称 /aa/bb/.jpg 或者 1.jpg
            // 第三个参数：上传文件的输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件的路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            //https://edu-01-jean.oss-cn-shanghai.aliyuncs.com/26%E5%AD%97%E6%AF%8D%E9%9F%B3%E6%A0%87.png
            String url="https://"+bucketName+"."+endpoint+"/"+fileName;
            return url;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
