package jiyoung.scheduling.dto;

import lombok.Data;

@Data
public class Member {
    private String memberId;
    private String memberName;
    private int money;

    public Member() {
    }

    public Member(String memberId, String memberName, int money) {
        this.memberName = memberName;
        this.memberId = memberId;
        this.money = money;
    }

}
