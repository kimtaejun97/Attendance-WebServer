package com.attendance.modules.account;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Builder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(of= "id")
@Entity
public class Account {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String password;

    //학생이라면 학번
    private String code;

    @Column(unique = true)
    private String email;

    private boolean emailVerified;

    private String emailCheckToken;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private Role role;


    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }

    public void completeSignUp() {
        this.emailVerified=true;
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }
}
