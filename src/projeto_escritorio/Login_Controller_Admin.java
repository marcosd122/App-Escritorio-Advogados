
package projeto_escritorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Login_Controller_Admin {
    
    private Connection conn;

    public Login_Controller_Admin(Connection connection) {
        this.conn = connection;
        
    }

    public boolean login(Login_Admin modelLogin) {
        String query = "SELECT * FROM Contas_admin WHERE Usuário_admin=? AND Senha_admin=?";
        try {
            PreparedStatement pst = ConnectDB.getConnection().prepareStatement(query);
            pst.setString(1, modelLogin.getUname());
            pst.setString(2, modelLogin.getPass());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return true;
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login_Controller_Admin.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }
}
