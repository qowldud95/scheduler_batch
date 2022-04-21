package jiyoung.scheduling.service;

import jiyoung.scheduling.reopsitory.TableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ScheduleTask {
    private final TableRepository tableRepository;
    public ScheduleTask(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Scheduled(cron = "2 * * * * *")
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

        List<Map<String, Object>> tableMapping = tableRepository.tableData("SELECT TABLE_KEY, SOURCE_TABLENAME, TARGET_TABLENAME, SOURCE_CONN, SOURCE_ID, SOURCE_PASSWORD, TARGET_CONN, TARGET_ID, TARGET_PASSWORD FROM TABLE_MAPPING");
        List<Map<String, Object>> columnMapping = tableRepository.tableData("SELECT TABLE_KEY, SOURCE_COLUMNNAME, TARGET_COLUMNNAME, IS_PRIMARYKEY FROM COLUMN_MAPPING");

        for (int i = 0; i < tableMapping.size(); i++) {
            Map<String, Object> table = tableMapping.get(i);
            List<Map<String, Object>> filterColumn = columnMapping.stream().filter(x -> x.get("TABLE_KEY").equals(table.get("TABLE_KEY"))).collect(Collectors.toList());
            Map<String, Object> pkColumn = filterColumn.stream().filter(x -> (boolean) x.get("IS_PRIMARYKEY")).findFirst().get();

            List<Map<String, Object>> sourceTable = tableRepository.tableData("SELECT MEMBER_ID, MEMBER_NAME, MONEY FROM " + table.get("SOURCE_TABLENAME"));
            List<Map<String, Object>> targetTable = tableRepository.tableData("SELECT MEMBER_ID, MEMBER_NAME, MONEY FROM " + table.get("TARGET_TABLENAME"));

            for (int j = 0; j < sourceTable.size(); j++) {
                Map<String, Object> source = sourceTable.get(j);
                log.info("source={}" , source);
                // [{TARGET_ID=sa, SOURCE_CONN=jdbc:h2:tcp://localhost/~/test, SOURCE_ID=sa, TARGET_CONN=jdbc:h2:tcp://localhost/~/test, TARGET_TABLENAME=MEMBER2, TARGET_PASSWORD=, SOURCE_TABLENAME=MEMBER, SOURCE_PASSWORD=, TABLE_KEY=d0db7916-b6e9-401d-a97c-8236bde01157}]
                if(targetTable.stream().filter(x -> x.get(pkColumn.get("TARGET_COLUMNNAME")) == source.get(pkColumn.get("SOURCE_COLUMNNAME"))).count() == 0){
                    //insert

                }
            }

        }

//        for (int i = 0; i < tableMapping.size(); i++) {
//
//            tableMapping.get(i);
//            String test = "";
//            List<Map<String, Object>> columnMapping = tableRepository.tableData(null,"select * from COLUMN_MAPPING WHERE TABLE_KEY ='"+test+"' ");
//
//            //int tmpI = i;
//            //List<Map<String, Object>> filterColumn = columnMapping.stream().filter(x -> x.get("table_key").equals(tableMapping.get(tmpI).get("table_key"))).collect(Collectors.toList());
//            //List<Map<String, Object>> primaryColumn = filterColumn.stream().filter(x -> (boolean) x.get("is_primarykey")).collect(Collectors.toList());
//
//            List<Map<String, Object>> sourceList =
//            List<Map<String, Object>> targetList = connection();
//
//            //target리스트에 없으면 insert
//            if(targetList.stream().filter(x -> x.get() == ).count() == 0) {
//
//            }
//            //target리스트에 있으면 update
//            else {
//
//            }
//            for (int j = 0; j < filterColumn.size(); j++) {
//                filterColumn.get(j).get("targetColumnname") = filterColumn.get(j).get("souceColumnname");
//            }
//        }


        log.info("The upload date : " + LocalDateTime.now() );
    }
}
