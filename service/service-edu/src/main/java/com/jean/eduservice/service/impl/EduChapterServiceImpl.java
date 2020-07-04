package com.jean.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jean.eduservice.entity.EduChapter;
import com.jean.eduservice.entity.EduVideo;
import com.jean.eduservice.entity.chapter.ChapterVo;
import com.jean.eduservice.entity.chapter.VideoVo;
import com.jean.eduservice.mapper.EduChapterMapper;
import com.jean.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jean.eduservice.service.EduVideoService;
import com.jean.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-26
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    //注入小节service
    @Autowired
    private EduVideoService videoService;

    //课程大纲列表，根据课程id进行查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //1、根据课程id，查询所有课程里面所有的章节
        QueryWrapper<EduChapter> wrapperChapter=new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        //2、查询小节
        QueryWrapper<EduVideo> wrapperVideo=new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> eduVideoList=videoService.list(wrapperVideo);

        //创建list集合，用于最终封装数据
        ArrayList<ChapterVo> finalList = new ArrayList<>();
        //3、遍历查询到的所有章节list，并进行封装
        //遍历查询到的章节eduChapterList
        for(int i=0;i<eduChapterList.size();i++){
            //得到每个章节
            EduChapter eduChapter = eduChapterList.get(i);
            //把eduChapter对象的值，复制到ChapterVo对象里面 ，就是进行数据的封装
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            finalList.add(chapterVo);

            //创建集合，用于封装小节
            ArrayList<VideoVo> videoList = new ArrayList<>();

            //4、遍历查询到的所有小节list，并进行封装
            for(int j=0;j<eduVideoList.size();j++){
                //得到每个小节
                EduVideo eduVideo = eduVideoList.get(j);
                //判断：小节里面的chapterId 与章节里面的id是否一样
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    //进行封装
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    //放到封装小节的集合里
                    videoList.add(videoVo);
                }

            }
            //所封装之后的小节list集合，放到ChapterVo chapterVo对象中去
            chapterVo.setChildren(videoList);
        }
        return finalList;
    }

    //删除章节的方法
    @Override
    public boolean deleteChapter(String chapterId) {
        //根据chapterId章节id，查询小节表：如果有数据，就不删除章节；没有数据就删除章节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = videoService.count(wrapper);
        //判断，count>0，表示小节表中有数据，不进行删除章节
        if(count>0){
            throw new GuliException(20001,"不能删除章节");
        }else{  //小节表中没有数据，就可以删除章节
            //删除章节
            int result = baseMapper.deleteById(chapterId);
            //删除成功 1>0 true； 0>0 false；
            return result>0;
        }
    }

    //2、根据课程id，删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }


}
