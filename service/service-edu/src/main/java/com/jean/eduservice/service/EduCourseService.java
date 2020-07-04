package com.jean.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jean.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jean.eduservice.entity.frontvo.CourseFrontVo;
import com.jean.eduservice.entity.frontvo.CourseWebVo;
import com.jean.eduservice.entity.vo.CourseAndTeacherInfo;
import com.jean.eduservice.entity.vo.CourseInfoVo;
import com.jean.eduservice.entity.vo.CoursePublishVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-05-26
 */
public interface EduCourseService extends IService<EduCourse> {

    //添加课程基本信息的方法
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id，查询课程基本信息
    CourseInfoVo getCourseInfo(String courseId);

    //修改课程信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id，查询确认信息
    CoursePublishVo publishCourseInfo(String id);

    //删除课程方法
    void removeCourse(String courseId);

    //根据所有课程和老师
    List<CourseAndTeacherInfo> courseAndTeacherInfo();

    //条件查询，带分页，查询所有课程 前台
    Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo);

    // 根据课程id，查询课程的基本信息
    CourseWebVo getBaseCourseInfo(String courseId);
}

