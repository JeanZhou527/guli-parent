package com.jean.eduservice.mapper;

import com.jean.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jean.eduservice.entity.frontvo.CourseWebVo;
import com.jean.eduservice.entity.vo.CourseAndTeacherInfo;
import com.jean.eduservice.entity.vo.CoursePublishVo;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-05-26
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    public CoursePublishVo getPublishCourseInfo(String courseId);

    public List<CourseAndTeacherInfo> getCourseAndTeacherInfo();

    // 根据课程id，查询课程的基本信息
    CourseWebVo getBaseCourseInfo(String courseId);
}
