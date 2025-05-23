package com.guilhermezuriel.style;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

public record BorderCellStyle(
        BorderStyle borderStyle,
        IndexedColors borderColor
) {
    public static BorderCellStyle title() {
        return new BorderCellStyle(BorderStyle.THIN, IndexedColors.BLACK);
    }

    public static BorderCellStyle header() {
        return new BorderCellStyle(BorderStyle.THIN, IndexedColors.BLACK);
    }

    public static BorderCellStyle data() {
        return new BorderCellStyle(BorderStyle.NONE, null);
    }
}
