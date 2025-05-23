package com.guilhermezuriel.impl;

import com.guilhermezuriel.annotations.ExcelColumn;
import com.guilhermezuriel.annotations.ExcelTable;
import com.guilhermezuriel.base.BaseExcel;
import com.guilhermezuriel.dtos.StyleExcelTable;
import org.apache.poi.xssf.usermodel.*;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class ExcelGeneratorLocal extends BaseExcel {
    /**
     * Method to create an Excel spreadsheet. The data can be loaded from any DTO, as long as it is not a wrapper class.
     * The sheet name is retrieved through the @ExcelTable annotation, present in the data class.
     * The column names are directly related to the @ExcelColumn annotation.
     *
     * @param data - List containing the data to be filled
     */
    @Override
    public byte[] generateExcelTable(AbstractList<?> data, StyleExcelTable style) throws IOException {
        Class<?> dataClass = data.getFirst().getClass();
        validateDataList(dataClass);
        String fileName = extractSheetNameByAnnotations(dataClass);
        Set<String> columns = exctractColumnsByAnnotations(dataClass);
        return this.createExcelSheet(fileName, data, columns, style);
    }

    @Override
    public byte[] generateExcelTable(AbstractList<?> data, String sheetName, StyleExcelTable style) throws IOException {
        Class<?> dataClass = data.getFirst().getClass();
        validateDataList(dataClass);
        Set<String> columns = exctractColumnsByAnnotations(dataClass);
        return this.createExcelSheet(sheetName, data, columns, style);
    }

    @Override
    public byte[] generateExcelTableWithComplexObjects(AbstractList<?> data, String sheetName) throws IOException {
        return new byte[0];
    }

    @Override
    public void applyDataToSheet(AbstractList<?> data, Set<String> columns, XSSFWorkbook workbook, XSSFSheet sheet, int rowCount, XSSFCellStyle style)  {
        Field[] fields = Arrays.stream(data.getFirst().getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(ExcelColumn.class)).toArray(Field[]::new);
        for (Object object : data) {
            XSSFRow row = sheet.createRow(rowCount++);
            for (int i = 0; i < fields.length; i++) {
                XSSFCell cell = row.createCell(i);
                Object value = this.getFieldByIndex(object, fields, i);
                this.setDataCellWithSyle(style, cell, value);
            }
        }
    }

    private void validateDataList(Class<?> aClass) {
        if (aClass.getAnnotation(ExcelTable.class) == null) {
            throw new RuntimeException("Could not create excel sheet, the class is not meant to be processed. Data is not annotated with @ExcelTable");
        }
    }


    private String extractSheetNameByAnnotations(Class<?> aClass) {
        String sheetName = aClass.getAnnotation(ExcelTable.class).name();
        if (sheetName == null || sheetName.isEmpty()) return aClass.getSimpleName();
        return sheetName;
    }

    private Set<String> exctractColumnsByAnnotations(Class<?> aClass) {
        Field[] fields = aClass.getDeclaredFields();
        Set<String> columns = new LinkedHashSet<>();
        for (Field field : fields) {
            Optional<ExcelColumn> annotation = Optional.ofNullable(field.getAnnotation(ExcelColumn.class));
            if (annotation.isPresent()) {
                String definedColumnName = annotation.get().name();
                if (definedColumnName.isEmpty()) {
                    columns.add(field.getName());
                    continue;
                }
                columns.add(definedColumnName);
            }
        }
        return columns;
    }

    private Object getFieldByIndex(Object object, Field[] fields, int index) {
        try {
            if (index < 0 || index >= fields.length) {
                throw new IndexOutOfBoundsException("Index out of range");
            }
            fields[index].setAccessible(true);
            return fields[index].get(object);
        }catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
