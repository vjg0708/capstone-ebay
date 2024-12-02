package com.ebay.utilities;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class ExcelManager {

    protected String path;

    public ExcelManager(String path){

        this.path = path;
    }

    public XSSFWorkbook getWorkBook() throws IOException {

        return new XSSFWorkbook(path);
    }

    public XSSFSheet getSheetByName(String sheetName,
                                    XSSFWorkbook workbook){

        return workbook.getSheet(sheetName);
    }

    public XSSFSheet getSheetByIndex(int index,
                                     XSSFWorkbook workbook){

        return workbook.getSheetAt(index);
    }
}
