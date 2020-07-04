package com.jean.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jean.commonutils.R;
import com.jean.eduservice.entity.EduCourse;
import com.jean.eduservice.entity.EduTeacher;
import com.jean.eduservice.entity.vo.*;
import com.jean.eduservice.service.EduCourseService;
import com.jean.eduservice.service.EduTeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-26
 */
@RestController
@RequestMapping("/eduservice/edu-course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService eduTeacherService;
    

    //课程列表 ，基本实现
    @GetMapping
    public R getCourseList()
    {
        List<EduCourse> list = courseService.list(null);
        return R.ok().data("list",list);
    }


    //TODO 完善带分页的条件查询
    //3、分页查询讲师的方法
    //current 当前页  limit 每页显示条数
    @GetMapping("/pageCourse/{current}/{limit}")
    public R pageListCourse(@PathVariable long current,@PathVariable long limit)
    {
        //创建Page对象
        Page<EduCourse> pageCourse = new Page<>(current,limit);

        //调用方法实现分页，调用方法时，会把分布的所有数据封装到Page对象里
       courseService.page(pageCourse, null);

        long total=pageCourse.getTotal(); //总记录数
        List<EduCourse> records = pageCourse.getRecords(); //所有数据的list集合

        return R.ok().data("total",total).data("rows",records); //第一种写法

//        HashMap Map = new HashMap();
//        Map.put("total",total);
//        Map.put("rows",records);
//        return R.ok().data(Map);    //第二种写法

    }

//    //4、条件查询带分页功能
//    //用@RequestBody进行传参，一定要是Post提交 @PostMapping
//    // @GetMapping("/pageCourseCondition/{current}/{limit}")  //方式一传参
//    // public R pageCourseCondition(@PathVariable long current, @PathVariable long limit, CourseQuery courseQuery)  //方式一传参
//    @PostMapping("/pageCourseCondition/{current}/{limit}")  //方式二传参
//    public R pageCourseCondition(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false) CourseQuery courseQuery) throws UnsupportedEncodingException  //方式二传参
//    {
//        //创建Page对象
//        Page<EduCourse> pageCourse = new Page<>(current,limit);
//
//        //构建条件
//        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
//        //多条件组合查询，，条件值可有可无 ，，有点类似动态sql
//        String title=courseQuery.getTitle();
//        String status=courseQuery.getStatus();
//        String teacherId=courseQuery.getTeacherId();
//        Integer lessonNum=courseQuery.getLessonNum();
//        Long viewCount=courseQuery.getViewCount();
//        String begin = courseQuery.getBegin();
//        //判断条件值是否为空，如果不为空，再拼接条件
//        if(!StringUtils.isEmpty(title))
//        {   //构建条件
//            wrapper.like("title",title); //字段名 模糊查询
//        }
//
//        if(!StringUtils.isEmpty(status))
//        {
//            wrapper.eq("status",status);  //等于
//        }
//
//        if(!StringUtils.isEmpty(teacherId))
//        {
//            wrapper.eq("teacher_id",teacherId);  //等于
//        }
//
//        if(!StringUtils.isEmpty(viewCount))
//        {
//            wrapper.ge("view_count",viewCount);  //大于等于
//        }
//
//        if(!StringUtils.isEmpty(lessonNum))
//        {
//            wrapper.ge("lesson_num",lessonNum);  //大于等于
//        }
//
//        if(!StringUtils.isEmpty(begin))
//        {
//            wrapper.ge("gmt_create",begin); //大于等于
//        }
//
//        //排序，按时间进行降序排序
//        wrapper.orderByDesc("gmt_create");
//
//
//        //调用service方法，实现条件查询带分页
//        courseService.page(pageCourse,wrapper);
//        long total=pageCourse.getTotal(); //总记录数
//        List<EduCourse> records = pageCourse.getRecords(); //所有数据的list集合
//        return R.ok().data("total",total).data("rows",records);
//    }

    //fym
    //4、条件查询带分页功能  加入了通过teacherId去查询老师的name
    //用@RequestBody进行传参，一定要是Post提交 @PostMapping
    // @GetMapping("/pageCourseCondition/{current}/{limit}")  //方式一传参
    // public R pageCourseCondition(@PathVariable long current, @PathVariable long limit, CourseQuery courseQuery)  //方式一传参
    @PostMapping("/pageCourseCondition/{current}/{limit}")  //方式二传参
    public R pageCourseCondition(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false) CourseQuery courseQuery) throws UnsupportedEncodingException  //方式二传参
    {
        //创建Page对象
        Page<EduCourse> pageCourse = new Page<>(current,limit);

        //构建条件
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //多条件组合查询，，条件值可有可无 ，，有点类似动态sql
        String title=courseQuery.getTitle();
        String status=courseQuery.getStatus();
        String teacherId=courseQuery.getTeacherId();
        Integer lessonNum=courseQuery.getLessonNum();
        Long viewCount=courseQuery.getViewCount();
        String begin = courseQuery.getBegin();
        //判断条件值是否为空，如果不为空，再拼接条件
        if(!StringUtils.isEmpty(title))
        {   //构建条件
            wrapper.like("title",title); //字段名 模糊查询
        }

        if(!StringUtils.isEmpty(status))
        {
            wrapper.eq("status",status);  //等于
        }

        if(!StringUtils.isEmpty(teacherId))
        {
            wrapper.eq("teacher_id",teacherId);  //等于
        }

        if(!StringUtils.isEmpty(viewCount))
        {
            wrapper.ge("view_count",viewCount);  //大于等于
        }

        if(!StringUtils.isEmpty(lessonNum))
        {
            wrapper.ge("lesson_num",lessonNum);  //大于等于
        }

        if(!StringUtils.isEmpty(begin))
        {
            wrapper.ge("gmt_create",begin); //大于等于
        }

        //排序，按时间进行降序排序
        wrapper.orderByDesc("gmt_create");


        //调用service方法，实现条件查询带分页
        courseService.page(pageCourse,wrapper);
        long total=pageCourse.getTotal(); //总记录数
        //        List<EduCourse> records = pageCourse.getRecords(); //所有数据的list集合
        List<EduCourse> eduCourses = pageCourse.getRecords(); //所有数据的list集合

        List<CourseAndTeacherNameVo> records= new ArrayList<>();
        for (EduCourse course : eduCourses) {
            //通过得到课程表中老师的id teacherId,再通过老师id，得到老师teacher对象
            EduTeacher teacher = eduTeacherService.getById(course.getTeacherId());

            String teacherName="";
            if (null != teacher){
                //得到老师名称
                teacherName = teacher.getName();
            }
            //给CourseAndTeacherNameVo赋值
            CourseAndTeacherNameVo vo = new CourseAndTeacherNameVo();
            vo.setTeacherName(teacherName);
            BeanUtils.copyProperties(course, vo);

            records.add(vo);
        }

        return R.ok().data("total",total).data("rows",records);
    }


    //根据课程id，查询确认信息
    @GetMapping("getCourseAndTeacherName")
    public R getCourseAndTeacherName()
    {
        List<CourseAndTeacherInfo> list = courseService.courseAndTeacherInfo();
        for(int i=0;i<list.size();i++){
            CourseAndTeacherInfo ob1= list.get(i);
           System.out.println(ob1.getTeacherName());
        }
        return R.ok().data("courseAndTeacherInfo",list);
    }



    //添加课程基本信息的方法
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo)
    {
        //返回添加之后课程id，为了后面添加课程大纲需要使用
        String id=courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }

    //根据课程id，查询课程基本信息
    @GetMapping("/getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId)
    {
        CourseInfoVo courseInfoVo=courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    //修改课程信息
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo)
    {
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程id，查询确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id)
    {
        CoursePublishVo coursePublishVo=courseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);
    }

    //课程的最终发布
    //修改课程状态status
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id)
    {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal"); //设置课程发布状态
        courseService.updateById(eduCourse);
        return R.ok();
    }

    //删除课程
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId)
    {
        courseService.removeCourse(courseId);

        return R.ok();
    }
    


}

