package com.jean.eduservice.entity.chapter;

import lombok.Data;

@Data
public class VideoVo {
    private String id;
    private  String title;
    private  String videoSourceId; //视频存储在阿里云上的视频id
}
