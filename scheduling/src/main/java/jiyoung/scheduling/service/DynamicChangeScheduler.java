//package jiyoung.scheduling.service;
//
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DynamicChangeScheduler {
//    private ThreadPoolTaskScheduler scheduler;
//    private String con = "*/2 * * * * *";
//    public void startScheduler(){
//        scheduler = new ThreadPoolTaskScheduler();
//        scheduler.initialize();  //scheduler 초기화
//        scheduler.schedule(getRunnable(), getTrigger());     //scheduler setting
//    }
//
//    private Runnable getRunnable(){
//
//    }
//}
