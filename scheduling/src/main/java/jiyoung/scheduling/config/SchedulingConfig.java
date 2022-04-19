package jiyoung.scheduling.config;


import jiyoung.scheduling.reopsitory.MemberRepositoryV1;
import jiyoung.scheduling.service.ScheduleTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SchedulingConfig {
    private final DataSource dataSource;

    public SchedulingConfig(DataSource dataSource){
        this.dataSource = dataSource;
    }
    @Bean
    MemberRepositoryV1 memberRepository(){
        return new MemberRepositoryV1(dataSource);
    }

    @Bean
    ScheduleTask scheduleTask(){
        return new ScheduleTask(memberRepository());
    }

}
