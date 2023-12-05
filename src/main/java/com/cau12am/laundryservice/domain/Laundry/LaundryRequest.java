package com.cau12am.laundryservice.domain.Laundry;

import com.cau12am.laundryservice.domain.Laundry.tag.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
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
    private String laundryName;
    private String email;
    private Gender gender;
    private List<ColorType> colorTypes;
    private Weight weight;
    private List<MachineType> machineTypes;
    private ExtraInfoType extraInfoType;
    private String message;
    private LocalDateTime date;
    private LocalDateTime expireDate;
    private boolean matched;
    @Version
    private long version;
}
