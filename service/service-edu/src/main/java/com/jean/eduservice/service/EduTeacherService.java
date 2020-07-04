package com.jean.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jean.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-04-22
 */
public interface EduTeacherService extends IService<EduTeacher> {

    //分页查询所有讲师
    Map<String,Object> getTeacherFrontList(Page<EduTeacher> pageTeacher);
}
