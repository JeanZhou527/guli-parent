package com.jean.eduservice.controller;


import com.jean.commonutils.R;
import com.jean.eduservice.entity.EduChapter;
import com.jean.eduservice.entity.chapter.ChapterVo;
import com.jean.eduservice.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/eduservice/edu-chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;

    //课程大纲列表也就是章节列表，根据课程id进行查询，
    @GetMapping("/getChapterVideo/{courseId}")
    public R getChapterVideo( @PathVariable  String courseId)
    {
        List<ChapterVo> list= eduChapterService.getChapterVideoByCourseId(courseId);

        return R.ok().data("allChapterVideo",list); //allChapterVideo这个名字会传给前端使用
    }

    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter)
    {
        eduChapterService.save(eduChapter);
        return  R.ok();
    }

    //根据章节id查询
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId)
    {
        EduChapter eduChapter = eduChapterService.getById(chapterId);
        return  R.ok().data("chapter",eduChapter); //把数据传给前端使用
    }

    //修改章节
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter)
    {
        eduChapterService.updateById(eduChapter);
        return  R.ok();
    }

    //删除章节
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId)
    {
        boolean flag=eduChapterService.deleteChapter(chapterId);
        if(flag){
            return R.ok();
        }else{
            return R.error();
        }

    }


}

