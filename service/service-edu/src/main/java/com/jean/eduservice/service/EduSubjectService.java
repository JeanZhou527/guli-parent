package com.jean.eduservice.service;

import com.jean.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jean.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-05-22
 */
public interface EduSubjectService extends IService<EduSubject> {

    //添加课程分类
    void saveSubject(MultipartFile file, EduSubjectService subjectService);

    //课堂分类列表(树型)
    List<OneSubject> getAllOneSubject();

}
