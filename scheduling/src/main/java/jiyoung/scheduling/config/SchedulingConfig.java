package jiyoung.scheduling.config;


import jiyoung.scheduling.reopsitory.MemberRepositoryV1;
import jiyoung.scheduling.reopsitory.TableRepository;
import jiyoung.scheduling.service.ScheduleTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SchedulingConfig {

    @Bean
    TableRepository tableRepository(){
        return new TableRepository();
    }

    @Bean
    ScheduleTask scheduleTask(){
        return new ScheduleTask(tableRepository());
    }

}
