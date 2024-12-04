package com.ebay.datasource;

import com.ebay.utilities.ExcelManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadData {

    ExcelManager useExcel;
    XSSFWorkbook workbook;
    XSSFSheet worksheet;
    String path;

    public ReadData(String path){

        this.path = path;
    }

    public Object[][] readData(String sheetName) throws IOException {

        useExcel = new ExcelManager(path);
        workbook = useExcel.getWorkBook();
        worksheet = useExcel.getSheetByName(sheetName, workbook);

        List<Object[]> dataList = new ArrayList<>();

        int rowSize = worksheet.getPhysicalNumberOfRows();
        Row row = worksheet.getRow(0);

        int colSize = row.getPhysicalNumberOfCells();

        for(int rsize = 1; rsize<rowSize; rsize++){

            List<Object> rowData = new ArrayList<>();

            for(int csize =0; csize<colSize; csize++){

                rowData.add(getCellValue(worksheet.getRow(rsize).getCell(csize)));
            }

            dataList.add(rowData.toArray());
        }

        return dataList.toArray(new Object[0][]);
    }

    public Object getCellValue(Cell cell){

        Object cellValue = null;

        switch (cell.getCellType()){

            case STRING -> cellValue = cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)){
                    cellValue = cell.getDateCellValue();
                }
                else {
                    cellValue = cell.getNumericCellValue();
                }
            }
            case BOOLEAN -> cellValue = cell.getBooleanCellValue();
        }
        return cellValue;
    }
}
