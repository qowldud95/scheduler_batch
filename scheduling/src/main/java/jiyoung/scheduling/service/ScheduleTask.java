package jiyoung.scheduling.service;

import jiyoung.scheduling.domain.Member;
import jiyoung.scheduling.reopsitory.MemberRepositoryV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ScheduleTask {
    private final MemberRepositoryV1 memberRepository;
    public ScheduleTask(MemberRepositoryV1 memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Scheduled(cron = "10 * * * * *")
    public void task1() throws SQLException {
        /*
        List<Member> member1List = memberRepository.findByMember1();
        List<Member> member2List = memberRepository.findByMember2();

        for(Member m : member1List) {
            String member_id = m.getMemberId();
            String member_name = m.getMemberName();
            int money = m.getMoney();

            memberRepository.update(member_id, member_name, money);

            if(member2List.stream().filter(x -> x.getMemberId().equals(member_id)).count() == 0){
                memberRepository.insert(member_id, member_name, money);
            }

        }
        */

        List<Map<String, Object>> tableMapping = memberRepository.tableInfo("select * from TABLE_MAPPING");
        List<Map<String, Object>> columnMapping = memberRepository.tableInfo("select * from COLUMN_MAPPING");

        for (int i = 0; i < tableMapping.size(); i++) {
            int tmpI = i;
            List<Map<String, Object>> filterColumn = columnMapping.stream().filter(x -> x.get("table_key").equals(tableMapping.get(tmpI).get("table_key"))).collect(Collectors.toList());
            List<Map<String, Object>> primaryColumn = filterColumn.stream().filter(x -> (boolean) x.get("is_primarykey")).collect(Collectors.toList());
            List<Map<String, Object>> sourceList = connection();
            List<Map<String, Object>> targetList = connection();

            //target리스트에 없으면 insert
            if(targetList.stream().filter(x -> x.get() == ).count() == 0) {

            }
            //target리스트에 있으면 update
            else {

            }
            for (int j = 0; j < filterColumn.size(); j++) {
                filterColumn.get(j).get("targetColumnname") = filterColumn.get(j).get("souceColumnname");
            }
        }


        log.info("The upload date : " + LocalDateTime.now() );
    }
}
