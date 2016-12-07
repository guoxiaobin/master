package com.nd.share.demo.service.impl;

import com.nd.share.demo.repository.dao.WordSearchLogDao;
import com.nd.share.demo.service.AppService;
import com.nd.share.demo.service.ScheduleTaskService;
import com.nd.share.demo.task.AppUpdInfoTimerTask;
import com.nd.share.demo.task.OfflinePkgTimerTask;
import com.nd.share.demo.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时任务
 *
 * @author 郭晓斌(121017)
 * @version created on 20160603.
 */
@Service
public class ScheduleTaskServiceImpl implements ScheduleTaskService, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleTaskServiceImpl.class);

    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private WordSearchLogDao wordSearchLogDao;
    @Resource
    private OfflinePkgTimerTask offlinePkgTimerTask;
    /**
     * 热修复定时包
     */
    @Resource
    private AppUpdInfoTimerTask appUpdInfoTimerTask;
    @Resource
    private AppService appService;

    @Value("${schedule.hotword.server.start.delay}")
    private int hotwordServerDelay;
    @Value("${schedule.hotword.excute.delay}")
    private int hotwordExcuteDelay;
    @Value("${cs.service.name}")
    private String csName;

    // 定期取得热词列表
    TimerTask hostwordTimerTask = new TimerTask() {
        @Override
        public void run() {
            wordSearchLogDao.saveHotwords();
        }
    };

    // 离线包打包定时处理
    TimerTask pkgTimerTask = new TimerTask() {
        @Override
        public void run() {
            offlinePkgTimerTask.packOfflinePkg();
        }
    };
    //热修复定时更新
    TimerTask appUpdTimerTask = new TimerTask() {
        @Override
        public void run() {
            appUpdInfoTimerTask.saveAppUpdInfos2Web();
        }
    };

    /**
     * 定时器
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
//        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
//        scheduledExecutorService.scheduleAtFixedRate(hostwordRunner, hotwordServerDelay, hotwordExcuteDelay, TimeUnit.DAYS);
        // 离线包定时处理
        this.taskimerTask(1);
        // 热词列表更新处理
        this.taskimerTask(2);
        this.taskimerTask(3);
    }

    /**
     * 离线包定时处理
     */
    private void taskimerTask(int taskFlag) {
        Calendar calendar = Calendar.getInstance();
        long period = 0;
        String taskName = "";
        Date date = new Date();
        TimerTask timerTask = null;

        // 执行定时器策略(默认:每日00:00:00执行方法)
        switch (taskFlag) {
            // 离线包打包
            case 1:
                taskName = "离线包打包";
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
                period = 24 * 60 * 60 * 1000;
                date = calendar.getTime();
                timerTask = pkgTimerTask;
                break;
            // 热词更新
            case 2:
                taskName = "热词更新";
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
                period = 24 * 60 * 60 * 1000;
                date = calendar.getTime();
                timerTask = hostwordTimerTask;
                break;
            case 3:
                taskName = "热修复信息更新";
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
                period = 24 * 60 * 60 * 1000;
                date = calendar.getTime();
                timerTask = appUpdTimerTask;
                break;
        }

        // 第一次执行定时任务的时间<当前的时间
        // 则加一天才能保证定点执行
        if(date.before(new Date())){
            date = StringUtil.addDay(date,1);
        }

        Timer timer = new Timer();
        try {
            timer.schedule(timerTask, date, period);
        }catch (Exception e){
            logger.error("ScheduleTaskServiceImpl.taskimerTask:Timertask {} is ERROR..",taskName);
            e.printStackTrace();
        }
    }
}
