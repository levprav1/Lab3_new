package org.example.readersBD;

import org.example.manipulationBD.Connector;
import org.example.dataBD.Region;
import org.example.collections.StorageBD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReaderRegions implements ReaderBD {
    @Override
    public void getData(Connector connector, StorageBD storageBD) throws SQLException {
        ArrayList<Region> regions = new ArrayList<>();
        Statement st = connector.createStatement();
        String sql = "SELECT * FROM public.regions";
        ResultSet resultSet = st.executeQuery(sql);

        while (resultSet.next()) {
            regions.add(new Region(
                    resultSet.getInt("id"),
                    resultSet.getString("region")
            ));

            storageBD.setRegions(regions);
        }

    }
}
