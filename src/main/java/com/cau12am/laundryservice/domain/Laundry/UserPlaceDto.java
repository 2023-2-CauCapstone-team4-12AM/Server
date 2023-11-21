package com.cau12am.laundryservice.domain.Laundry;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserPlaceDto {
    @NotBlank(message = "lat값이 비어있습니다.")
    String lat;
    @NotBlank(message = "lon값이 비어있습니다.")
    String lon;
    @NotBlank(message = "distance값이 비어있습니다.")
    String distance;
}
