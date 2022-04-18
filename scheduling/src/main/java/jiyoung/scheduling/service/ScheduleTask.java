package jiyoung.scheduling.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class ScheduleTask {
    @Scheduled(cron = "10 * * * * *")
    public void task1(){
        

        log.info("The current date (1) : " + LocalDateTime.now() );
    }
}
