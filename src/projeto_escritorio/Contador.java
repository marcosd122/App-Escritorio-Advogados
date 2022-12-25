
package projeto_escritorio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;

public class Contador {
    
 public static int countDB (String tableNome)
    {
        int total = 0;
        
        Connection con = ConnectDB.getConnectionCliente();
        Statement st;
        
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery ("Select COUNT (*) as 'Total' FROM "+tableNome);
            while (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Contador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }
    
}

