package com.jean.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jean.commonutils.R;
import com.jean.eduservice.entity.EduCourse;
import com.jean.eduservice.entity.EduTeacher;
import com.jean.eduservice.service.EduCourseService;
import com.jean.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/teacherfront")
@CrossOrigin
public class TeacherFrontController {

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    //1、分页查询讲师的方法
   @PostMapping("/getTeacherFrontList/{page}/{limit}")
    public R getgetTeacherFrontList(@PathVariable long page,@PathVariable long limit){
       Page<EduTeacher> pageTeacher = new Page<>(page,limit);
       Map<String,Object> map= teacherService.getTeacherFrontList(pageTeacher);

       //要返回分页中的所有数据
       return R.ok().data(map);
   }

   //2、讲师详情的功能
    @GetMapping("/getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable String teacherId)
    {
        //1、根据讲师id，查询讲师基本信息
        EduTeacher eduTeacher = teacherService.getById(teacherId);
        //2、根据讲师id，查询所讲的课程信息
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<EduCourse> courseList = courseService.list(wrapper);
        return R.ok().data("teacher",eduTeacher).data("courseList",courseList);
    }



}
