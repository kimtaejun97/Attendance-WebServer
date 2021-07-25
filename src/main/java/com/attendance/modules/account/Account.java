package com.attendance.modules.account;

import com.attendance.modules.beacon.Beacon;
import com.attendance.modules.accountplace.AccountPlace;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Account {

    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    private String phoneNumber;

    private String address;

    private Role role;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime emailTokenLastGeneration;


    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
    private Set<AccountPlace> accountPlaces = new HashSet<>();

    @OneToMany(cascade = CascadeType.REMOVE)
    private Set<Beacon> beacons = new HashSet<>();


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
