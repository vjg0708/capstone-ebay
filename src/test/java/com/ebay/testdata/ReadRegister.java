package com.ebay.testdata;

import com.ebay.datasource.ReadData;
import org.testng.annotations.DataProvider;

import java.io.IOException;

public class ReadRegister {

    @DataProvider(name = "registerCredentials")
    public Object[][] register() throws IOException {

        ReadData readExcel = new ReadData("C:\\Users\\harrish.vijay\\OneDrive - ascendion\\Desktop\\Capstone projects\\capstone_project-EBAY\\automation-ebay\\src\\test\\java\\com\\ebay\\testdata\\EBAY.xlsx");
        return readExcel.readData("Register");
    }
}
