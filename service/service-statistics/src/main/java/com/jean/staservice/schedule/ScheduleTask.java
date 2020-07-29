package com.jean.staservice.schedule;

import com.jean.staservice.service.StatisticsDailyService;
import com.jean.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduleTask {

    // springboot项目只支持6位，第7位springboot是默认为今年，所以不写第7位；写了第7位反而会报错
    // 0/5****?每隔5秒钟，去执行一次任务
//    @Scheduled(cron= "0/5 * * * * ?") // 定时任务的第二步
//    public  void task1()
//    {
//        System.out.println("***********task1执行了");
//    }

    @Autowired
    private StatisticsDailyService staService;

    // 每天凌晨1点钟执行, 把前一天的数据进行数据查询和添加
  @Scheduled(cron= "0 0 1 * * ? ") // 定时任务的第二步
    public  void task2()
    {
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        staService.registerCount(day);
    }


}
