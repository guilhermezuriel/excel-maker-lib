package com.guilhermezuriel.dtos;

import java.util.AbstractList;


public record RequestExcelTable(
     String name,
    AbstractList<?> data,
    StyleExcelTable style){
}
