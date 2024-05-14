package org.example.readersBD;

import org.example.manipulationBD.Connector;
import org.example.dataBD.OperatingHistory;
import org.example.collections.StorageBD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReaderOperatingHistory implements ReaderBD{
    @Override
    public void getData(Connector connector, StorageBD storageBD) throws SQLException {
        ArrayList<OperatingHistory> operatingHistories = new ArrayList<>();
        Statement st = connector.createStatement();
        String sql = "SELECT * FROM public.operating_history";
        ResultSet resultSet = st.executeQuery(sql);

        while (resultSet.next()) {
            operatingHistories.add(new OperatingHistory(
                    resultSet.getInt("id"),
                    resultSet.getInt("year"),
                    resultSet.getDouble("electricity_supplied"),
                    resultSet.getInt("preference_unit_power"),
                    resultSet.getInt("annual_time_line"),
                    resultSet.getDouble("operating_factor"),
                    resultSet.getDouble("energy_av_factor_annual"),
                    resultSet.getDouble("energy_av_factor_cummulative"),
                    resultSet.getDouble("load_factor_annual"),
                    resultSet.getDouble("load_factor_cummulative"),
                    resultSet.getInt("unit_id")
            ));

            storageBD.setOperatingHistories(operatingHistories);
        }


    }
}
