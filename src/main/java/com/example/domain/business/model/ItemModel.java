package com.example.domain.business.model;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder(toBuilder = true)
@Data
@Getter
@Setter
public class ItemModel {
    private final String bono;
    private final String date;


}
