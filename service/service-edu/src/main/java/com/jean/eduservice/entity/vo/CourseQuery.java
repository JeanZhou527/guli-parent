package com.jean.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class CourseQuery {
    private String title;
    private String status;
    private String teacherId;
//    private String teacherName;
    private Integer lessonNum;
    private Long viewCount;
    @ApiModelProperty(value="查询开始时间，格式为：2019-01-01 10:10:10",example = "2019-01-01 10:10:10")
    private String begin;
}
