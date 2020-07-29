package com.jean.eduservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.jean.commonutils.R;
import com.jean.eduservice.entity.EduTeacher;
import com.jean.eduservice.entity.vo.TeacherQuery;
import com.jean.eduservice.service.EduTeacherService;

import com.jean.servicebase.exceptionhandler.GuliException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-04-22
 */
@RestController
//@CrossOrigin //解决跨域问题
@RequestMapping("/eduservice/edu-teacher")
public class EduTeacherController {
//访问地址： http://localhost:8001/eduservice/edu-teacher/findAll
//通过网关访问地址： http://localhost:8222/eduservice/edu-teacher/findAll

    @Autowired  //把EduTeacherService注入进来
   private EduTeacherService teacherService;

    //rest风格   查询是@GetMapping  删除@DeleteMapping

    //1查询讲师表中的所有数据
//    @GetMapping("/findAll")
//    public List<EduTeacher> findAllTeacher()
//    {
//        //调用service的方法实现查询所有的操作
//        List<EduTeacher> list = teacherService.list(null);
//        return  list;
//    }

    //统一返回数据的格式
    //1查询讲师表中的所有数据
    @GetMapping("/findAll")
    public R findAllTeacher()
    {
        //调用service的方法实现查询所有的操作
        List<EduTeacher> list = teacherService.list(null);
//        int i=10/0;   //为了测试下全局异常 或 特定异常   测试1

//        try{
//            int i=10/0;
//        } catch (Exception e)
//        {   //一般自定义异常是要自己手动throw出的
//            throw new GuliException(20001,"执行了自定义异常处理。。。");
//        }  //测试自定义异常        测试2

        return  R.ok().data("items",list);
    }


    //@PathVariable从路径中得到id值
    //  2、逻辑删除讲师
//    @DeleteMapping("{id}")
//    public boolean removeTeacher(@PathVariable String id)
//    {
//        boolean flag = teacherService.removeById(id);
//      return  flag;
//    }

    //统一返回数据的格式
    //@PathVariable从路径中得到id值
    //  2、逻辑删除讲师
    @DeleteMapping("{id}")
    public R removeTeacher(@PathVariable String id)
    {
        boolean flag = teacherService.removeById(id);
        if(flag){
            return R.ok();
        }else{
            return  R.error();
        }
    }

    //3、分页查询讲师的方法
    //current 当前页  limit 每页显示条数
    @GetMapping("/pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,@PathVariable long limit)
    {
        //创建Page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        
        //调用方法实现分页，调用方法时，会把分布的所有数据封装到Page对象里
        teacherService.page(pageTeacher, null);

        long total=pageTeacher.getTotal(); //总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); //所有数据的list集合

        return R.ok().data("total",total).data("rows",records); //第一种写法

//        HashMap Map = new HashMap();
//        Map.put("total",total);
//        Map.put("rows",records);
//        return R.ok().data(Map);    //第二种写法

    }

    //4、条件查询带分页功能
    //用@RequestBody进行传参，一定要是Post提交 @PostMapping
    // @GetMapping("/pageTeacherCondition/{current}/{limit}")  //方式一传参
    // public R pageTeacherCondition(@PathVariable long current, @PathVariable long limit, TeacherQuery teacherQuery)  //方式一传参
    @PostMapping("/pageTeacherCondition/{current}/{limit}")  //方式二传参
    public R pageTeacherCondition(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false) TeacherQuery teacherQuery) throws UnsupportedEncodingException  //方式二传参
    {
        //创建Page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);

        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //多条件组合查询，，条件值可有可无 ，，有点类似动态sql
        String name=teacherQuery.getName();
        Integer level=teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件值是否为空，如果不为空，再拼接条件
        if(!StringUtils.isEmpty(name))
        {   //构建条件

//            String _name = new String("王".getBytes("GB2312"),"GB2312");
            wrapper.like("name",name); //字段名 模糊查询
        }

        if(!StringUtils.isEmpty(level))
        {
            wrapper.eq("level",level);  //等于
        }

        if(!StringUtils.isEmpty(begin))
        {
            wrapper.ge("gmt_create",begin); //大于等于
        }

        if(!StringUtils.isEmpty(end))
        {
            wrapper.le("gmt_modified",end); //小于等于
        }

        //排序，按时间进行降序排序
        wrapper.orderByDesc("gmt_create");


        //调用service方法，实现条件查询带分页
        teacherService.page(pageTeacher,wrapper);
        long total=pageTeacher.getTotal(); //总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); //所有数据的list集合
        return R.ok().data("total",total).data("rows",records);
    }


    //5 、添加讲师
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher)
    {
        boolean save=teacherService.save(eduTeacher);
        if(save) {return R.ok();}
        else{ return  R.error();}
    }

    //6、根据讲师id进行查询
    @GetMapping("/getTeacher/{id}")
    public R getTeacher(@PathVariable String id)
    {
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }

    //6、根据讲师id进行查询
    @GetMapping("/getTeacherName/{id}")
    public R getTeacherName(@PathVariable String id)
    {
        EduTeacher eduTeacher = teacherService.getById(id);
        String name=eduTeacher.getName();
        return  R.ok().data("teacherName",name);
    }

    //7、根据讲师id进行修改
    @PostMapping("/updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher)
    {
        boolean flag = teacherService.updateById(eduTeacher);

        if(flag) {return R.ok();}
        else{ return  R.error();}
    }


    //8、根据讲师名字进行查询
    @PostMapping("/getTeacherByName/{name}")
    public R getTeacherByName(@RequestBody  String name)
    {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        if(!StringUtils.isEmpty(name))
        {   //构建条件
            wrapper.eq("name",name);
        }

        EduTeacher eduTeacher = teacherService.getOne( wrapper);
        return R.ok().data("teacher",eduTeacher);
    }

}

