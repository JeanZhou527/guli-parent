package com.jean.eduservice.controller.front;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jean.commonutils.JwtUtils;
import com.jean.commonutils.R;
import com.jean.eduservice.client.UcenterClient;
import com.jean.eduservice.entity.EduComment;
import com.jean.eduservice.entity.UcenterMember;
import com.jean.eduservice.service.EduCommentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-04
 */
@RestController
@RequestMapping("/eduservice/comment")
@CrossOrigin
public class CommentFrontController {

    @Autowired
    private EduCommentService commentService;
    @Autowired
    private UcenterClient ucenterClient;


        //根据课程id查询评论列表
    @ApiOperation(value = "评论分页列表")
    @GetMapping("/{courseId}/{page}/{limit}")
    public R index(
        @ApiParam(name = "page", value = "当前页码", required = true)
        @PathVariable Long page,
        @ApiParam(name = "limit", value = "每页记录数", required = true)
        @PathVariable Long limit,
        @ApiParam(name = "courseQuery", value = "查询对象", required = false)
        @PathVariable String courseId) {
        Page<EduComment> pageParam = new Page<>(page, limit);

        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        //排序，按时间进行降序排序
        wrapper.orderByDesc("gmt_create");

        commentService.page(pageParam,wrapper);
        List<EduComment> commentList = pageParam.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return R.ok().data(map);
    }

    // 添加评论
    @ApiOperation(value = "添加评论")
    @PostMapping("auth/save")
    public R save(@RequestBody EduComment comment, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(20001).message("请登录");
        }
        comment.setMemberId(memberId);
        UcenterMember ucenterMemberInfo = ucenterClient.getInfo(memberId);
//        Map<String,Object> ucenterInfo = (LinkedHashMap<String,Object>)ucenterMemberInfo.getData().get("memberInfo");
//        comment.setNickname(ucenterInfo.get("nickname").toString());
//        comment.setAvatar(ucenterInfo.get("avatar").toString());
        comment.setNickname(ucenterMemberInfo.getNickname());
        comment.setAvatar(ucenterMemberInfo.getAvatar());
        commentService.save(comment);
        return R.ok();
    }


}

