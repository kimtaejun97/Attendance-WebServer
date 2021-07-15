package com.attendance.modules.lecture;

import com.attendance.modules.lecture.Place;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlaceListResponseDto {
    private String location;
    private String alias;
    private String constructor;


    public PlaceListResponseDto(Place place){
        this.location = place.getLocation();
        this.alias = place.getAlias();
        this.constructor = place.getConstructor();
    }


}
