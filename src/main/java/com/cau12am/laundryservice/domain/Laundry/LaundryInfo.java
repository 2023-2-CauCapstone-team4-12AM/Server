package com.cau12am.laundryservice.domain.Laundry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "laundry_info")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaundryInfo {

    @Id
    private String _id;
    private String name;
    private String roadAddress;
    private String time;
    private GeoJsonPoint location;
}
