package com.cau12am.laundryservice.domain.Match;

import com.cau12am.laundryservice.domain.Laundry.tag.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "match")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {
    @Id
    private String _id;
    private String requestId;
    private String url;
    private List<String> users;
    private Date date;
    @Version
    private long version;
}
