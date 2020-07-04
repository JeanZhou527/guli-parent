package com.jean.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jean.eduservice.entity.EduCourse;
import com.jean.eduservice.entity.EduCourseDescription;
import com.jean.eduservice.entity.frontvo.CourseFrontVo;
import com.jean.eduservice.entity.frontvo.CourseWebVo;
import com.jean.eduservice.entity.vo.CourseAndTeacherInfo;
import com.jean.eduservice.entity.vo.CourseInfoVo;
import com.jean.eduservice.entity.vo.CoursePublishVo;
import com.jean.eduservice.mapper.EduCourseMapper;
import com.jean.eduservice.service.EduChapterService;
import com.jean.eduservice.service.EduCourseDescriptionService;
import com.jean.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jean.eduservice.service.EduVideoService;
import com.jean.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-26
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //课程描述的注入
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private EduChapterService eduChapterService;

    //添加课程基本信息的方法，并返回课程id
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1、向课程表edu_course中添加课程基本信息
        //CourseInfoVo对象转换 eduCourse对象
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
//        eduCourse.setSubjectId(courseInfoVo.getSubjectId());
//        eduCourse.setTeacherId(courseInfoVo.getTeacherId());
//        eduCourse.setTitle(courseInfoVo.getTitle());
//        eduCourse.setPrice(courseInfoVo.getPrice());
//        eduCourse.setLessonNum(courseInfoVo.getLessonNum());
//        eduCourse.setCover(courseInfoVo.getCover());
        int insert = baseMapper.insert(eduCourse);
        if(insert<=0){
            //添加失败
            throw new GuliException(20001,"添加课程信息失败");
        }

        //获取添加之后课程id
        String cid=eduCourse.getId();

        //2、向课程简介表edu_course_description添加课程简价
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        //设置描述id就是课程id
        courseDescription.setId(cid);
        courseDescriptionService.save(courseDescription);
        return cid;
    }

    //根据课程id，查询课程基本信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //1、先查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        //2、再查询描述表
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());
        return courseInfoVo;
    }

    //修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
         //1、先修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int update=baseMapper.updateById(eduCourse);
        if(update==0){
            throw  new GuliException(20001,"修改课程信息失败");
        }
        //2、再修改描述表
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.updateById(description);
    }

    //根据课程id，查询确认信息
    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        //调用mapper
        CoursePublishVo publishlishCourseInfo = baseMapper.getPublishCourseInfo(id);
        return publishlishCourseInfo;
    }

    //删除课程
    @Override
    public void removeCourse(String courseId) {
        //1、根据课程id，删除小节
        eduVideoService.removeVideoByCourseId(courseId);
        //2、根据课程id，删除章节
        eduChapterService.removeChapterByCourseId(courseId);
        //3、根据课程id，删除描述
        courseDescriptionService.removeById(courseId);
        //4、根据课程id，删除课程本身
        int result = baseMapper.deleteById(courseId);
        if(result==0){
            throw new GuliException(20001,"删除课程失败");
        }

    }

    @Override
    public List<CourseAndTeacherInfo> courseAndTeacherInfo() {

        List<CourseAndTeacherInfo> list = baseMapper.getCourseAndTeacherInfo();
        return list;
    }

    //条件查询，带分页，查询所有课程 前台
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断条件值是否为空，不为空，才去拼接条伯
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){//判断一级分类是否为空
          wrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectId())){//判断二级分类是否为空
            wrapper.eq("subject_id",courseFrontVo.getSubjectId());
        }

        if(!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){//关注度也是销量
            wrapper.orderByDesc("buy_count");
        }

        if(!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())){//最新，也就是时间
            wrapper.orderByDesc("gmt_create");
        }
        if(!StringUtils.isEmpty(courseFrontVo.getPriceSort())){//价格
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(pageCourse,wrapper);

        List<EduCourse> records = pageCourse.getRecords();
        long current = pageCourse.getCurrent();//当前页
        long pages = pageCourse.getPages(); //总页数
        long size = pageCourse.getSize();//每页记录数
        long total = pageCourse.getTotal();//总记录数

        boolean hasNext = pageCourse.hasNext(); //是否有下一页
        boolean hasPrevious = pageCourse.hasPrevious();//是否有上一页

        // 把分页数据获取出来，放到map集合中去
        HashMap<String, Object> map = new HashMap<>();
        map.put("items",records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    // 根据课程id，查询课程的基本信息
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }
}
