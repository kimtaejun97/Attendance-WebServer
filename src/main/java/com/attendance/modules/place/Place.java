package com.attendance.modules.place;

import com.attendance.modules.account.Account;
import com.attendance.modules.attendance.Attendance;
import com.attendance.modules.beacon.Beacon;
import com.attendance.modules.accountplace.AccountPlace;
import lombok.*;

import javax.persistence.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class Place {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String alias;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne
    private Account creator;

    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE)
    private Set<AccountPlace> accountPlaces = new HashSet<>();

    @OneToOne
    private Beacon beacon;

    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE)
    private Set<Attendance> attendances = new HashSet<>();

    public String getEncodedLocation() {
        return URLEncoder.encode(this.beacon.getLocation(), StandardCharsets.UTF_8);
    }

    public String getLocation() {
        return this.beacon.getLocation();
    }

    public void validateEqualsToCreator(Account account) throws IllegalAccessException {
        if(!this.creator.equals(account)){
            throw new IllegalAccessException("접근 권한이 없습니다.");
        }

    }
}
