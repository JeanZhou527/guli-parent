package com.jean.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jean.commonutils.R;
import com.jean.eduservice.entity.EduCourse;
import com.jean.eduservice.entity.chapter.ChapterVo;
import com.jean.eduservice.entity.frontvo.CourseFrontVo;
import com.jean.eduservice.entity.frontvo.CourseWebVo;
import com.jean.eduservice.service.EduChapterService;
import com.jean.eduservice.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/eduservice/coursefront")
@RestController
@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    //1、条件查询，带分页查询课程
    @PostMapping("/getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page, @PathVariable long limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo)
    {
        Page<EduCourse> pageCourse = new Page<>(page, limit);
       Map<String,Object> map=courseService.getCourseFrontList(pageCourse,courseFrontVo);
        //返回分页的所有数据
        return R.ok().data(map);
    }
    //2、 课程详情页
    @GetMapping("/getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId){
        // 根据课程id，编写sql语句，查询课程基本信息
        CourseWebVo courseWebVo=courseService.getBaseCourseInfo(courseId);
        // 根据课程id，查询章节和小节信息
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);
        return  R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList);
    }
}
