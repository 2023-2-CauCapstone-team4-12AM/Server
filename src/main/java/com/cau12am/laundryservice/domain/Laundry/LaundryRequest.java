package com.cau12am.laundryservice.domain.Laundry;

import com.cau12am.laundryservice.domain.Laundry.tag.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "laundry_request")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaundryRequest {
    @Id
    private String _id;
    private String laundryId;
    private String email;
    private Gender gender;
    private List<ClothesColor> clothesColorList;
    private Weight weight;
    private List<MachineType> machineTypes;
    private ExtraInfoType extraInfoType;
    private String message;
    private Date date;
    private boolean matched;
}
