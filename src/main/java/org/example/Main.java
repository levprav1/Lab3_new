package org.example;

import java.io.IOException;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        //new JFrameProgram().setVisible(true);
        BuilderBD bd = new BuilderBD();
        bd.deleteBD();
        bd.createBD();
        bd.fillBD();
    }
}