package domain.account.entity;

import common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Account")
public class Account extends BaseEntity {
    private final static int PHONE_SIZE = 11;
    private final static int NAME_SIZE = 30;

    @Column(nullable = false)
    private String email; //이메일

    @Column(nullable = false, length = PHONE_SIZE)
    private String phone; //전화번호

    @Column(nullable = false, length = NAME_SIZE)
    private String name; //성명
    
    @Column(nullable = false, length = NAME_SIZE)
    private String gender; //성별

    @Column(nullable = false)
    private String subject; //과목명

    @Column(nullable = false)
    private String subjectCode; //과목코드

    @Column(nullable = false)
    private String userId; //아이디

    @Column(nullable = false)
    private String userPw; //비밀번호

    private int failCount; //비밀번호

    private LocalDateTime lastFailLogin;

    public void failLogin(int count, LocalDateTime date){
        this.failCount = count;
        this.lastFailLogin = date;
    }
    @Builder
    public Account(String email, String phone, String name, String gender, String subject, String subjectCode, String userId, String userPw) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.gender = gender;
        this.subject = subject;
        this.subjectCode = subjectCode;
        this.userId = userId;
        this.userPw = userPw;
        this.failCount = 0;
        this.lastFailLogin = null;
    }
}