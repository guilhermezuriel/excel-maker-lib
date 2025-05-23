package com.guilhermezuriel.base;

import com.guilhermezuriel.dtos.StyleExcelTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.AbstractList;
import java.util.Set;

public interface BaseExcelService {
   void applyDataToSheet(AbstractList<?> data, Set<String> columns, XSSFWorkbook workbook, XSSFSheet sheet, int rowCount, XSSFCellStyle cellStyle);
   byte[] generateExcelTable(AbstractList<?> data, StyleExcelTable style) throws IOException;
   byte[] generateExcelTable(AbstractList<?> data, String sheetName, StyleExcelTable style) throws IOException;
   byte[] generateExcelTableWithComplexObjects(AbstractList<?> data, String sheetName) throws IOException;
}
