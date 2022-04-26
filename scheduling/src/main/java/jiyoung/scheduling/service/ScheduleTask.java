package jiyoung.scheduling.service;

import jiyoung.scheduling.dto.ConnectionDTO;
import jiyoung.scheduling.reopsitory.TableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.SQLException;
import java.util.ArrayList;
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

        List<Map<String, Object>> tableMapping = tableRepository.tableData("SELECT TABLE_KEY, SOURCE_TABLENAME, TARGET_TABLENAME, SOURCE_CONN, SOURCE_ID, SOURCE_PASSWORD, TARGET_CONN, TARGET_ID, TARGET_PASSWORD FROM TABLE_MAPPING");
        List<Map<String, Object>> columnMapping = tableRepository.tableData("SELECT TABLE_KEY, SOURCE_COLUMNNAME, TARGET_COLUMNNAME, IS_PRIMARYKEY FROM COLUMN_MAPPING");

        for (int i = 0; i < tableMapping.size(); i++) {
            Map<String, Object> table = tableMapping.get(i);
            String targetTableName = String.valueOf(table.get("TARGET_TABLENAME"));

            ConnectionDTO sourceConnect = new ConnectionDTO(String.valueOf(table.get("SOURCE_CONN")), String.valueOf(table.get("SOURCE_ID")), String.valueOf(table.get("SOURCE_PASSWORD")));
            ConnectionDTO targetConnect = new ConnectionDTO(String.valueOf(table.get("TARGET_CONN")), String.valueOf(table.get("TARGET_ID")), String.valueOf(table.get("TARGET_PASSWORD")));

            log.info("targetConnect = {}", targetConnect);

            List<Map<String, Object>> filterColumn = columnMapping.stream().filter(x -> x.get("TABLE_KEY").equals(table.get("TABLE_KEY"))).collect(Collectors.toList());
            Map<String, Object> pkColumn = filterColumn.stream().filter(x -> (boolean) x.get("IS_PRIMARYKEY")).findFirst().get();
            log.info("pkColumn={}",pkColumn);



            List<String> sourceColumnName = new ArrayList<>();
            List<String> targetColumnName = new ArrayList<>();
            String sourceColumns = "";
            String targetColumns = "";

            //filterColumn에서 source_columnname 과 ,target_columnname을 순서대로 꺼내서
            for(int k = 0; k < filterColumn.size(); k++){
                sourceColumnName.add(String.valueOf(filterColumn.get(k).get("SOURCE_COLUMNNAME")));
                targetColumnName.add(String.valueOf(filterColumn.get(k).get("TARGET_COLUMNNAME")));
            }
            for(int k = 0; k < sourceColumnName.size(); k++){
                if(sourceColumns == "" && targetColumns == ""){
                    sourceColumns += sourceColumnName.get(k);
                    targetColumns += targetColumnName.get(k);
                } else {
                    sourceColumns += ", " + sourceColumnName.get(k);
                    targetColumns += ", " + targetColumnName.get(k);
                }
            }
            log.info("sourceColumnName={}", String.valueOf(sourceColumnName));
            log.info("sourceColumns={}", String.valueOf(sourceColumns));
            log.info("targetColumnName={}", String.valueOf(targetColumnName));
            log.info("targetColumns={}", String.valueOf(targetColumns));

            String sourceSql = "SELECT " + sourceColumns + " FROM " + String.valueOf(table.get("SOURCE_TABLENAME"));
            String targetSql = "SELECT " + targetColumns + " FROM " + String.valueOf(table.get("TARGET_TABLENAME"));

            log.info("sourceSql :: " + sourceSql);
            log.info("targetSql :: " + targetSql);

            List<Map<String, Object>> sourceTable = tableRepository.tableData(sourceSql, sourceConnect);
            List<Map<String, Object>> targetTable = tableRepository.tableData(targetSql, targetConnect);

            log.info("sourceTable = {}" , sourceTable);
            log.info("targetTable = {}", targetTable);

            for (int j = 0; j < sourceTable.size(); j++) {
                Map<String, Object> source = sourceTable.get(j);
                log.info("source={}" , source);

                if(targetTable == null || targetTable.stream().filter(x -> String.valueOf(x.get(String.valueOf(pkColumn.get("TARGET_COLUMNNAME")))).equals(String.valueOf(source.get(String.valueOf(pkColumn.get("SOURCE_COLUMNNAME")))))).count() == 0){
                   tableRepository.targetDataInsert(source, targetTableName, targetColumnName, sourceColumnName, targetConnect);
                   log.info(targetTableName + "insert");
                } else {
                    tableRepository.targetDataUpdate(source, targetTableName, targetColumnName, sourceColumnName, pkColumn, targetConnect);
                    log.info(targetTableName + "update");
                }
            }
        }
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
        }*/
    }
}
