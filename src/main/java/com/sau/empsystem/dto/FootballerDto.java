package com.sau.empsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FootballerDto implements Serializable {

    private Integer footballerid;
    private String full_name;
    private String teamname;
    private String city;
    private Integer shirtnumber;
    private Long marketvalue;
    private String photo;
}