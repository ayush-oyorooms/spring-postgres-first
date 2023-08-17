package com.example.springpostgres.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
public class PlayEntity {

    public String name;

    public String sellerName;
   private Long playId;
}
