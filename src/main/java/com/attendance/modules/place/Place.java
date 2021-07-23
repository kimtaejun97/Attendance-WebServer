package com.attendance.modules.place;

import com.attendance.modules.userplace.UserPlace;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Place {

    @Id
    private String location;

    @Column(nullable = false)
    private String alias;

    @Column(nullable = false)
    private String creator;

    @Column(nullable = false)
    private String isPublic;

    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE)
    private Set<UserPlace> userPlaces = new HashSet<>();


}
