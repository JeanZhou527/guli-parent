package com.jean.staservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jean.commonutils.R;
import com.jean.staservice.client.UcenterClient;
import com.jean.staservice.entity.StatisticsDaily;
import com.jean.staservice.mapper.StatisticsDailyMapper;
import com.jean.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-10
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;


    //得到某一天的注册人数，并记录到统计分析表中
    @Override
    public void registerCount(String day) {
        //添加记录到数据库统计分析表之前，删除表中相同日期的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);

        //远程调用Ucenter微服务，得到某一天的注册人数
        R registerR = ucenterClient.countRegister(day);
        Integer countRegister= (Integer)registerR.getData().get("countRegister");

        //把获取到的数据，添加到数据库的统计分析表中
        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(countRegister);//注册人数
        sta.setDateCalculated(day);//统计日期
        // 生成一个100到200之间的随机数
//        RandomUtils.nextInt(100,200);

        sta.setVideoViewNum(RandomUtils.nextInt(100,200));
        sta.setLoginNum(RandomUtils.nextInt(100,200));
        sta.setCourseNum(RandomUtils.nextInt(100,200));
        baseMapper.insert(sta);

    }

    // 图表显示，返回两部分数据，日期为json数组，数量为json数组
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        // 根据条件查询对应的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated",begin,end);
        wrapper.select("date_calculated",type); //指定查询哪例数据
        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);

        // 因为返回有两部分数据，日期和日期对应的数量
        // 前端要求json数组结构，对应后端java的结构为list集合
        // 创建两个list集合，一个日期list，一个数量list
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<Integer> mountList = new ArrayList<>();

        // 遍历查询到所有数据的list集合，进行封装
        for(int i=0; i<staList.size(); i++)
        {
            StatisticsDaily daily = staList.get(i);

            // 封装日期list依赖
            dateList.add(daily.getDateCalculated());
            //封装日期对应的数量
            switch (type){
                case "login_num":
                    mountList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    mountList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    mountList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    mountList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        // 把封装之后的两个list集合放到map中，进行返回
        Map<String, Object> map = new HashMap<>();
        map.put("date_calculatedList",dateList);
        map.put("numDataList",mountList);

        return map;
    }
}
