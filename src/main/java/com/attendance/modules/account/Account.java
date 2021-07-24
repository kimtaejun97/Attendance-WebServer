package com.attendance.modules.account;

import com.attendance.modules.userplace.AccountPlace;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(of= "id")
@Entity
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private boolean emailVerified;

    private String emailCheckToken;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private Role role;

    private LocalDateTime emailTokenLastGeneration;

    @OneToMany(mappedBy = "account")
    private Set<AccountPlace> accountPlaces = new HashSet<>();


    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailTokenLastGeneration = LocalDateTime.now();
    }

    public void completeSignUp() {
        this.emailVerified=true;
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public boolean canSendEmail(){
        if(this.emailTokenLastGeneration == null){
            return true;
        }
        return this.emailTokenLastGeneration.isBefore(LocalDateTime.now().minusMinutes(10));
    }
}
