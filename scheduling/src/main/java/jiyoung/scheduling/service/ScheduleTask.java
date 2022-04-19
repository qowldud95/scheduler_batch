package jiyoung.scheduling.service;

import jiyoung.scheduling.domain.Member;
import jiyoung.scheduling.reopsitory.MemberRepositoryV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static jiyoung.scheduling.connection.ConnectionConst.*;

@Slf4j
public class ScheduleTask {
    private final MemberRepositoryV1 memberRepository;
    public ScheduleTask(MemberRepositoryV1 memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Scheduled(cron = "10 * * * * *")
    public void task1() throws SQLException {
        List<Member> member1List = memberRepository.findByMember1();
        List<Member> member2List = memberRepository.findByMember2();

        List<Map<String, Object>> tableMapping = new ArrayList<>();
        List<Map<String, Object>> columnMapping = new ArrayList<>();

        for (int i = 0; i < tableMapping.size(); i++) {
            int tmpI = i;
            List<Map<String, Object>> filterColumn = columnMapping.stream().filter(x -> x.get("tableKey").equals(tableMapping.get(tmpI).get("tableKey"))).collect(Collectors.toList());
            List<Map<String, Object>> primaryColumn = filterColumn.stream().filter(x -> (boolean) x.get("isPrimarykey")).collect(Collectors.toList());
            List<Map<String, Object>> sourceList = connection();
            List<Map<String, Object>> targetList = connection();

            if(targetList.stream().filter(x -> x.get(filterColumn.get(0).get("targetColumnname")) == ).count() == 0) {

            }
            for (int j = 0; j < filterColumn.size(); j++) {
                filterColumn.get(j).get("targetColumnname") = filterColumn.get(j).get("souceColumnname");
            }
        }

        for(Member m : member1List) {
            String member_id = m.getMemberId();
            String member_name = m.getMemberName();
            int money = m.getMoney();

            memberRepository.update(member_id, member_name, money);

            if(member2List.stream().filter(x -> x.getMemberId().equals(member_id)).count() == 0){
                memberRepository.insert(member_id, member_name, money);
            }

        }
        log.info("The upload date : " + LocalDateTime.now() );
    }
}
