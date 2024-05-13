package org.example.readersBD;

import org.example.Connector;
import org.example.dataBD.Site;
import org.example.collections.StorageBD;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReaderSites implements ReaderBD{
    @Override
    public void getData(Connector connector, StorageBD storageBD) throws SQLException {
        ArrayList<Site> sites = new ArrayList<>();
        try (Statement st = connector.createStatement()) {
            String sql = "SELECT * FROM public.sites";
            ResultSet resultSet = st.executeQuery(sql);

            while (resultSet.next()) {
                sites.add(new Site(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("location"),
                        resultSet.getInt("country_id")
                ));

                storageBD.setSites(sites);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ошибка получения данных из БД"+e.getMessage(), "Oшибка", JOptionPane.ERROR_MESSAGE);
        }

    }
}
