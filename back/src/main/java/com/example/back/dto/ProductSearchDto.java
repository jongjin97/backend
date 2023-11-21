package com.example.back.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchDto {

    private String searchDateType;
    private String searchBy;
    private String searchQuery = "";
}
