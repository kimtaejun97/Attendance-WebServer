package com.attendance.modules.place;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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


}
