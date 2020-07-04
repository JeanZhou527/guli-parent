package com.jean.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jean.eduservice.entity.EduTeacher;
import com.jean.eduservice.mapper.EduTeacherMapper;
import com.jean.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-04-22
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageTeacher) {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //降序排序Desc    Asc升序
        wrapper.orderByDesc("id");
        baseMapper.selectPage(pageTeacher,wrapper);

        List<EduTeacher> records = pageTeacher.getRecords();
        long current = pageTeacher.getCurrent();//当前页
        long pages = pageTeacher.getPages(); //总页数
        long size = pageTeacher.getSize();//每页记录数
        long total = pageTeacher.getTotal();//总记录数

        boolean hasNext = pageTeacher.hasNext(); //是否有下一页
        boolean hasPrevious = pageTeacher.hasPrevious();//是否有上一页

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
}
