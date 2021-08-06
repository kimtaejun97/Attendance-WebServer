package com.attendance.modules.place;

import com.attendance.modules.attendance.Attendance;
import com.attendance.modules.beacon.Beacon;
import com.attendance.modules.accountplace.AccountPlace;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Place {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String alias;

    @Column(nullable = false)
    private String creator;

    @Column(nullable = false)
    private String isPublic;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE)
    private Set<AccountPlace> accountPlaces = new HashSet<>();

    @OneToOne
    private Beacon beacon;

    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE)
    private Set<Attendance> attendances = new HashSet<>();


}
