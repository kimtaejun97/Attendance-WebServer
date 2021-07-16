package com.attendance.modules.place;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlaceListResponseDto {
    private String location;
    private String alias;
    private String creator;


    public PlaceListResponseDto(Place place){
        this.location = place.getLocation();
        this.alias = place.getAlias();
        this.creator = place.getCreator();
    }


}
