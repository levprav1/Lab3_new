package org.example.readersBD;

import org.example.manipulationBD.Connector;
import org.example.dataBD.Country;
import org.example.collections.StorageBD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReaderCountries implements ReaderBD{
    @Override
    public void getData(Connector connector, StorageBD storageBD) throws SQLException {
        ArrayList<Country> countries = new ArrayList<>();
        Statement st = connector.createStatement();
        String sql = "SELECT * FROM public.countries";
        ResultSet resultSet = st.executeQuery(sql);

        while (resultSet.next()) {
            countries.add(new Country(resultSet.getInt("id"),
                    resultSet.getString("country"),
                    resultSet.getInt("region_id")
            ));

            storageBD.setCountries(countries);
        }

    }
}
