package com.jean.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jean.eduservice.entity.EduSubject;
import com.jean.eduservice.entity.excel.SubjectData;
import com.jean.eduservice.entity.subject.OneSubject;
import com.jean.eduservice.entity.subject.TwoSubject;
import com.jean.eduservice.listener.SubjectExcelListener;
import com.jean.eduservice.mapper.EduSubjectMapper;
import com.jean.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-22
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService subjectService) {
        try{
            //文件输入流
            InputStream in=file.getInputStream();

            //调用方法进行读取
            EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();
        }
        catch (Exception e){ e.printStackTrace(); }
    }

    //课堂分类列表(树型)
    @Override
    public List<OneSubject> getAllOneSubject() {
        //1、查询出所有的一级分类 parentid=0
        QueryWrapper<EduSubject> wrapperOne=new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

        //2、查询出所有的二级分类 parentid !=0
        QueryWrapper<EduSubject> wrapperTwo=new QueryWrapper<>();
        wrapperTwo.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);

        //创建list集合，用于存储最终封装的数据
        List<OneSubject> finalSubjectList=new ArrayList<>();

        //3、封装一级分类
        //查询出来所有的一级分类list集合遍历，得到每个一级分类对象 ，获取每个一级分类对象值
        //封装到要求的list集合里面List<OneSubject> finalSubjectList
        for(int i=0;i< oneSubjectList.size(); i++){
            //得到oneSubjectList集合的每个eduSubject对象
            EduSubject eduSubject = oneSubjectList.get(i);

            //把eduSubject里面值获取出来，放到OneSubject对象里面
            OneSubject oneSubject = new OneSubject();

//            oneSubject.setId(eduSubject.getId());
//            oneSubject.setTitle(eduSubject.getTitle());
            BeanUtils.copyProperties(eduSubject,oneSubject);   //上面两行代码的简化
            //多个OneSubject放到finalSubjectList里面
            finalSubjectList.add(oneSubject);

            //在一级分类循环遍历查询所有的二级分类
            //创建list集合封装每个一级分类的二级分类
            ArrayList<TwoSubject> finalTwoSubjectList = new ArrayList<>();
            for(int m=0; m< twoSubjectList.size(); m++){
                //获取每个二级分类
                EduSubject tSubject = twoSubjectList.get(m);
                //判断二级分类parentid和一级分类的id是否一样
                if(tSubject.getParentId().equals(eduSubject.getId())){
                    //把tSubject值复制到TwoSubject里面，并放到finalTwoSubjectList里面
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(tSubject,twoSubject);
                    finalTwoSubjectList.add(twoSubject);
                }
            }
                //把一级下面所有二级分类放到一级分类里面
            oneSubject.setChildren(finalTwoSubjectList);
        }
        return finalSubjectList;
    }
}
