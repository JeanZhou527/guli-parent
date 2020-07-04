package com.jean.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jean.commonutils.R;
import com.jean.eduservice.client.VodClient;
import com.jean.eduservice.entity.EduVideo;
import com.jean.eduservice.mapper.EduVideoMapper;
import com.jean.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jean.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-26
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    //注入vodClient
    @Autowired
    private VodClient vodClient;

    //根据课程id，删除小节
    //todo 删除小节时，还需要去删除对应的视频文件
    @Override
    public void removeVideoByCourseId(String courseId) {

        //1、根据课程id，查询出所有课程的视频id
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        //只需要一列video_source_id的值
//        wrapperVideo.select("video_source_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapperVideo);

        //把List<EduVideo> 变成 List<String>
        List<String> videoIds=new ArrayList<>();
        for(int i=0;i<eduVideoList.size();i++){
            EduVideo eduVideo = eduVideoList.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();

            if(!StringUtils.isEmpty(videoSourceId)){
                //把videoSourceId放List<String>集合里去
                videoIds.add(videoSourceId);
            }
        }

        //如果videoIds有值，就执行删除视频操作，没有值就代表没有视频，也就不需要删除视频
        if(videoIds.size()>0){
            //根据多个视频id，删除多个视频
//            vodClient.deleteBatch(videoIds);
            R result= vodClient.deleteBatch(videoIds); //删除阿里云上的多个视频
            if(result.getCode()==20001){
                throw new GuliException(20001,"删除阿里云上的多个视频失败，熔断器...");
            }
        }

        //再删除小节的其他信息
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
