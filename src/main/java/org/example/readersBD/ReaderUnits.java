package org.example.readersBD;

import org.example.manipulationBD.Connector;
import org.example.collections.StorageBD;
import org.example.dataBD.Unit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReaderUnits implements ReaderBD{
    @Override
    public void getData(Connector connector, StorageBD storageBD) throws SQLException {
        ArrayList<Unit> units = new ArrayList<>();
        Statement st = connector.createStatement();
        String sql = "SELECT * FROM public.units";
        ResultSet result = st.executeQuery(sql);

        while (result.next()) {
            units.add(new Unit(
                    result.getInt("id"),
                    result.getInt("site_id"),
                    result.getString("name"),
                    result.getString("status"),
                    result.getString("type"),
                    result.getString("model"),
                    result.getInt("owner_id"),
                    result.getInt("operator_id"),
                    result.getInt("net_capacity"),
                    result.getInt("design_net_capacity"),
                    result.getInt("gross_capacity"),
                    result.getInt("thermal_capacity"),
                    result.getString("construction_start_date"),
                    result.getString("first_criticaly_date"),
                    result.getString("first_grid_connection"),
                    result.getString("commercial_operation_date"),
                    result.getString("suspended_operation_date"),
                    result.getString("end_suspended_operation_date"),
                    result.getString("permanent_shutdown_date")
            ));

            storageBD.setUnits(units);
        }
    }
}
