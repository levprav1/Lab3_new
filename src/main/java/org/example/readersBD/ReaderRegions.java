package org.example.readersBD;

import org.example.Connector;
import org.example.dataBD.Region;
import org.example.collections.StorageBD;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReaderRegions implements ReaderBD{
    @Override
    public void getData(Connector connector, StorageBD storageBD) throws SQLException {
        ArrayList<Region> regions = new ArrayList<>();
        try (Statement st = connector.createStatement()) {
            String sql = "SELECT * FROM public.regions";
            ResultSet resultSet = st.executeQuery(sql);

            while (resultSet.next()) {
                regions.add(new Region(
                        resultSet.getInt("id"),
                        resultSet.getString("region")
                ));

                storageBD.setRegions(regions);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ошибка получения данных из БД"+e.getMessage(), "Oшибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
