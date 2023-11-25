package com.cau12am.laundryservice.domain.Laundry;

import com.cau12am.laundryservice.domain.Laundry.tag.*;
import lombok.Data;

import java.util.List;

@Data
public class LaundryRequestDto {
    Gender gender;
    String laundryId;
    String email;
    List<ClothesColor> clothesColorList;
    Weight weight;
    List<MachineType> machineTypes;
    ExtraInfoType extraInfoType;
    String message;
}
