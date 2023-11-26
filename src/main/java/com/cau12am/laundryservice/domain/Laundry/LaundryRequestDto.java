package com.cau12am.laundryservice.domain.Laundry;

import com.cau12am.laundryservice.domain.Laundry.tag.*;
import lombok.Data;

import java.util.List;

@Data
public class LaundryRequestDto {
    String _id;
    Gender gender;
    String laundryId;
    String email;
    List<ColorType> colorTypes;
    Weight weight;
    List<MachineType> machineTypes;
    ExtraInfoType extraInfoType;
    String message;
}
