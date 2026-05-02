package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_funcionarios?useSSL=false&serverTimezone=America/Bogota";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "123456";   

    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de MySQL. Verifica que tienes el conector añadido al proyecto.", e);
        }
    }
}