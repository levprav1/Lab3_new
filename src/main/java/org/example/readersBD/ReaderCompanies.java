package org.example.readersBD;

import org.example.manipulationBD.Connector;
import org.example.dataBD.Company;
import org.example.collections.StorageBD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReaderCompanies implements ReaderBD {
    @Override
    public void getData(Connector connector, StorageBD storageBD) throws SQLException {
        ArrayList<Company> companies = new ArrayList<>();
        Statement st = connector.createStatement();
        ResultSet resultSet = st.executeQuery("SELECT * FROM public.companies");

        while (resultSet.next()) {
            companies.add(new Company(resultSet.getInt("id"),
                    resultSet.getString("company")));
        }
        storageBD.setCompanies(companies);

    }
}
