
package projeto_escritorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB {
    
   private static Connection conn = null;
    private ResultSet rst;
    private PreparedStatement pst;
    private Statement st = null;

    public ConnectDB() {
    }

    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:DB_Login.db");
            System.out.println("SqLite - Conectado com suscesso");
            return conn;
        } catch (Exception e) {
            System.out.println("Falha na conexão" + e);
            return null;

        }
    }

    public static Connection getConnectionCliente() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:DB_Cliente.db");
           // System.out.println("SqLite - Conectado com suscesso");
            return conn;
        } catch (Exception e) {
            System.out.println("Falha na conexão" + e);
            return null;
        }

    }

}
