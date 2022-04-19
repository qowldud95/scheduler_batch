//package jiyoung.scheduling.repository;
//
//import jiyoung.scheduling.domain.Member;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//
//import java.sql.SQLException;
//import java.util.NoSuchElementException;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//@Slf4j
//public class MemberRepositoryV0Test {
//    MembrRepositoryV0 repository = new MembrRepositoryV0();
//
//    @Test
//    void crud() throws SQLException {
//        //save
//        Member member = new Member("jiyoung1", 30000);
//        repository.save(member);
//
//        //findById
//        Member findMember = repository.findById(member.getMemberId());
//        log.info("findMember={}", findMember);
//        assertThat(findMember).isEqualTo(member);
//
//        //update: money: 30000 -> 20000
//        repository.update(member.getMemberId(), 20000);
//        Member updatedMember = repository.findById(member.getMemberId());
//        assertThat(updatedMember.getMoney()).isEqualTo(30000);
//
//        //delete
//        repository.delete(member.getMemberId());
//        assertThatThrownBy(() -> repository.findById(member.getMemberId())).isInstanceOf(NoSuchElementException.class);
//    }
//}
