package jiyoung.scheduling.service;

import jiyoung.scheduling.dto.ConnectionDTO;
import jiyoung.scheduling.reopsitory.TableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.SQLException;
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
            String targetTableName = String.valueOf(table.get("TARGET_TABLENAME"));

            ConnectionDTO sourceConnect = new ConnectionDTO(String.valueOf(table.get("SOURCE_CONN")), String.valueOf(table.get("SOURCE_ID")), String.valueOf(table.get("SOURCE_PASSWORD")));
            ConnectionDTO targetConnect = new ConnectionDTO(String.valueOf(table.get("TARGET_CONN")), String.valueOf(table.get("TARGET_ID")), String.valueOf(table.get("TARGET_PASSWORD")));

            List<Map<String, Object>> filterColumn = columnMapping.stream().filter(x -> x.get("TABLE_KEY").equals(table.get("TABLE_KEY"))).collect(Collectors.toList());
            Map<String, Object> pkColumn = filterColumn.stream().filter(x -> (boolean) x.get("IS_PRIMARYKEY")).findFirst().get();

            List<Map<String, Object>> sourceTable = tableRepository.tableData("SELECT MEMBER_ID, MEMBER_NAME, MONEY FROM " + table.get("SOURCE_TABLENAME"), sourceConnect);
            List<Map<String, Object>> targetTable = tableRepository.tableData("SELECT MEMBER_ID, MEMBER_NAME, MONEY FROM " + table.get("TARGET_TABLENAME"), targetConnect);

            log.info("sourceTable = {}" , sourceTable);
            //sourceTable = [{MEMBER_NAME=baeji, MONEY=1000, MEMBER_ID=1}, {MEMBER_NAME=jiyoung, MONEY=2000, MEMBER_ID=2}, {MEMBER_NAME=baejiyoung, MONEY=3000, MEMBER_ID=3}]

            log.info("targetTable = {}", targetTable);
            //targetTable = [{MEMBER_NAME=baejiyoung, MONEY=1000, MEMBER_ID=1}]


            for (int j = 0; j < sourceTable.size(); j++) {
                Map<String, Object> source = sourceTable.get(j);
                log.info("source={}" , source);
                //{MEMBER_NAME=baeji, MONEY=1000, MEMBER_ID=1}

                tableRepository.targetDataUpdate(source, targetTableName);

                if(targetTable.stream().filter(x -> x.get(pkColumn.get("TARGET_COLUMNNAME")) == source.get(pkColumn.get("SOURCE_COLUMNNAME"))).count() == 0){
                   tableRepository.targetDataInsert(source, targetTableName);
                }
            }
        }
    }
}
