
package projeto_escritorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class Login_Controller_User {
    
    private Connection con;

    public Login_Controller_User(Connection connection) {
        this.con = connection;
        

    }

    public boolean login(Login_User modelLogin) {
        String query = "SELECT * FROM Contas_user WHERE Usuário_adv=? AND Senha_adv=?";
        try {
            PreparedStatement pst = ConnectDB.getConnection().prepareStatement(query);
            pst.setString(1, modelLogin.getUsername());
            pst.setString(2, modelLogin.getUserpass());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {

                
                return true;
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login_Controller_User.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }

}
