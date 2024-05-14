package org.example.manipulationBD;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;


public class ExcelToDatabase {
	public void createTables(Connector connector) throws IOException, SQLException {
			InputStream inputStream = getClass().getResourceAsStream("/Locations.xlsx");
            Workbook workbook = new XSSFWorkbook(inputStream);
            Connection connection = connector.getConnection();
            // Обработка листа regions
            Sheet regionsSheet = workbook.getSheet("regions");
            PreparedStatement regionStmt = connection.prepareStatement("INSERT INTO public.regions (region) VALUES (?)");

            Iterator<Row> rowIterator = regionsSheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getCell(0) != null) {
                    String region = row.getCell(1).getStringCellValue();
                    regionStmt.setString(1, region);
                    regionStmt.executeUpdate();
                }
            }

            // Обработка листа countries
            Sheet countriesSheet = workbook.getSheet("countries");
            PreparedStatement countryStmt = connection.prepareStatement("INSERT INTO public.countries (country, region_id) VALUES (?, ?)");

        Iterator<Row> rowIteratorCountry = countriesSheet.iterator();
        rowIteratorCountry.next();
        while (rowIteratorCountry.hasNext()) {
            Row row = rowIteratorCountry.next();
                if (row.getCell(0) != null) {
                String country = row.getCell(1).getStringCellValue();
                int regionId = (int) row.getCell(2).getNumericCellValue();
                countryStmt.setString(1, country);
                countryStmt.setInt(2, regionId);
                countryStmt.executeUpdate();
                }
            }

	}
}
