package com.cau12am.laundryservice.domain.Ban;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "ban")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ban {
    @Id
    private String email;
    private List<String> banList;
}
