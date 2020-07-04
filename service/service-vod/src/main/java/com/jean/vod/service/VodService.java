package com.jean.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//上传视频到阿里云
public interface VodService {
    //上传视频到阿里云
    String uploadVideoAly(MultipartFile file);

    //删除多个视频
    void removeMoreAlyVideo(List<String> videoIdList);

}
