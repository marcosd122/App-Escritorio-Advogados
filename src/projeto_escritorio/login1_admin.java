package projeto_escritorio;

import java.awt.Color;
import java.awt.geom.RoundRectangle2D;
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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class login1_admin extends javax.swing.JFrame {

    private Connection conn;
    private ResultSet rst;
    private PreparedStatement pst;

    public login1_admin() {
        conn = ConnectDB.getConnection();
        
        setUndecorated(true);
        initComponents();
        getContentPane().setBackground(new Color(255, 199, 113));
        setLocationRelativeTo(null);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 50, 80));
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/img/icojustice3.png")).getImage());

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        kGradientPanel2 = new keeptoo.KGradientPanel();
        titulo = new javax.swing.JLabel();
        pnl_adv = new javax.swing.JPanel();
        fechar = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        txtAdmin = new javax.swing.JTextField();
        txtSenhaAdmin = new javax.swing.JPasswordField();
        login3 = new javax.swing.JLabel();
        minimizar = new javax.swing.JLabel();
        login = new javax.swing.JLabel();
        login1 = new javax.swing.JLabel();
        btnlogin_admin = new javax.swing.JButton();
        Voltar = new javax.swing.JLabel();
        mostrarsenha_admin = new javax.swing.JCheckBox();
        separador1 = new javax.swing.JSeparator();
        separador2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setBorder(new javax.swing.border.MatteBorder(null));
        bg.setForeground(new java.awt.Color(255, 255, 255));
        bg.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        kGradientPanel2.setBackground(new java.awt.Color(255, 199, 113));
        kGradientPanel2.setkEndColor(new java.awt.Color(255, 199, 113));
        kGradientPanel2.setkGradientFocus(600);
        kGradientPanel2.setkStartColor(new java.awt.Color(255, 199, 113));

        titulo.setBackground(new java.awt.Color(255, 255, 255));
        titulo.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        titulo.setText("Projeto Java NetBeans");

        pnl_adv.setBackground(new java.awt.Color(255, 255, 255));

        fechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fechar.png"))); // NOI18N
        fechar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fecharMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fecharMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                fecharMouseExited(evt);
            }
        });

        txtAdmin.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtAdmin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAdmin.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));

        txtSenhaAdmin.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtSenhaAdmin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSenhaAdmin.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));

        login3.setBackground(new java.awt.Color(255, 255, 255));
        login3.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        login3.setForeground(new java.awt.Color(102, 102, 102));
        login3.setText("Administrador");

        minimizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/minimizar1.png"))); // NOI18N
        minimizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minimizarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                minimizarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                minimizarMouseExited(evt);
            }
        });

        login.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/login.png"))); // NOI18N

        login1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/login (1).png"))); // NOI18N

        btnlogin_admin.setBackground(new java.awt.Color(0, 153, 204));
        btnlogin_admin.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btnlogin_admin.setForeground(new java.awt.Color(255, 255, 255));
        btnlogin_admin.setText("LOGIN");
        btnlogin_admin.setActionCommand("");
        btnlogin_admin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlogin_adminActionPerformed(evt);
            }
        });

        Voltar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/voltar1.png"))); // NOI18N
        Voltar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VoltarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                VoltarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                VoltarMouseExited(evt);
            }
        });

        mostrarsenha_admin.setBackground(new java.awt.Color(255, 255, 255));
        mostrarsenha_admin.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        mostrarsenha_admin.setText("Mostrar a senha");
        mostrarsenha_admin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarsenha_adminActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_advLayout = new javax.swing.GroupLayout(pnl_adv);
        pnl_adv.setLayout(pnl_advLayout);
        pnl_advLayout.setHorizontalGroup(
            pnl_advLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_advLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_advLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_advLayout.createSequentialGroup()
                        .addGroup(pnl_advLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_advLayout.createSequentialGroup()
                                .addComponent(Voltar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(minimizar, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fechar, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator2))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_advLayout.createSequentialGroup()
                        .addGap(0, 53, Short.MAX_VALUE)
                        .addGroup(pnl_advLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_advLayout.createSequentialGroup()
                                .addComponent(btnlogin_admin, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_advLayout.createSequentialGroup()
                                .addGroup(pnl_advLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(mostrarsenha_admin)
                                    .addGroup(pnl_advLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtSenhaAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                                        .addComponent(txtAdmin)
                                        .addComponent(login1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(55, 55, 55))))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_advLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(login3)
                .addGap(155, 155, 155))
        );
        pnl_advLayout.setVerticalGroup(
            pnl_advLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_advLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_advLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnl_advLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(fechar, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(minimizar, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Voltar))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(login3)
                .addGap(23, 23, 23)
                .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(login1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSenhaAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(mostrarsenha_admin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(btnlogin_admin)
                .addGap(51, 51, 51))
        );

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(titulo)
                .addContainerGap())
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGap(431, 431, 431)
                .addComponent(pnl_adv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(440, Short.MAX_VALUE))
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(titulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addComponent(pnl_adv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        bg.add(kGradientPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-11, -40, 1410, 770));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(separador2)
                    .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(separador1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(separador1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(separador2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    int positionX = 0, positionY = 0;

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

    }//GEN-LAST:event_formMouseClicked

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed

        positionX = evt.getX();
        positionY = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        setLocation(evt.getXOnScreen() - positionX, evt.getYOnScreen() - positionY);
    }//GEN-LAST:event_formMouseDragged

    private void fecharMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fecharMouseClicked
        System.exit(0);
    }//GEN-LAST:event_fecharMouseClicked

    private void minimizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizarMouseClicked
        this.setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_minimizarMouseClicked

    private void fecharMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fecharMouseEntered
        fechar.setIcon(new ImageIcon(getClass().getResource("/img/fechar1.png")));
    }//GEN-LAST:event_fecharMouseEntered

    private void fecharMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fecharMouseExited
        fechar.setIcon(new ImageIcon(getClass().getResource("/img/fechar.png")));
    }//GEN-LAST:event_fecharMouseExited

    private void minimizarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizarMouseEntered
        minimizar.setIcon(new ImageIcon(getClass().getResource("/img/minimizar.png")));
    }//GEN-LAST:event_minimizarMouseEntered

    private void minimizarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizarMouseExited
        minimizar.setIcon(new ImageIcon(getClass().getResource("/img/minimizar1.png")));
    }//GEN-LAST:event_minimizarMouseExited

    private void VoltarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VoltarMouseClicked
        this.setVisible(false);
        new login().setVisible(true);
    }//GEN-LAST:event_VoltarMouseClicked

    private void VoltarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VoltarMouseEntered
        Voltar.setIcon(new ImageIcon(getClass().getResource("/img/voltar.png")));
    }//GEN-LAST:event_VoltarMouseEntered

    private void VoltarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VoltarMouseExited
        Voltar.setIcon(new ImageIcon(getClass().getResource("/img/voltar1.png")));
    }//GEN-LAST:event_VoltarMouseExited

    private void btnlogin_adminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlogin_adminActionPerformed
        
        try {
            Date currentDate = GregorianCalendar.getInstance().getTime();
            DateFormat df = DateFormat.getDateInstance();
            String dateString = df.format(currentDate);

            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String timeString = sdf.format(d);

            String value0 = timeString;
            String values = dateString;

            String value = txtAdmin.getText();

            conn = ConnectDB.getConnectionCliente();
            String reg = "insert into Log (Usuário,Data,Status) values ('" + value + "','" + value0 + " / " + values + "','Logado')";
            pst = conn.prepareStatement(reg);
            pst.execute();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        } finally {

            try {
                rst.close();
                pst.close();

            } catch (Exception e) {

            }
        }

        onLoginClick();
    }//GEN-LAST:event_btnlogin_adminActionPerformed

    private void mostrarsenha_adminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrarsenha_adminActionPerformed

        if (mostrarsenha_admin.isSelected()) {
            txtSenhaAdmin.setEchoChar((char) 0);
        } else {
            txtSenhaAdmin.setEchoChar('*');
        }

    }//GEN-LAST:event_mostrarsenha_adminActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login1_admin().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Voltar;
    private javax.swing.JPanel bg;
    private javax.swing.JButton btnlogin_admin;
    private javax.swing.JLabel fechar;
    private javax.swing.JSeparator jSeparator2;
    private keeptoo.KGradientPanel kGradientPanel2;
    private javax.swing.JLabel login;
    private javax.swing.JLabel login1;
    private javax.swing.JLabel login3;
    private javax.swing.JLabel minimizar;
    private javax.swing.JCheckBox mostrarsenha_admin;
    private javax.swing.JPanel pnl_adv;
    private javax.swing.JSeparator separador1;
    private javax.swing.JSeparator separador2;
    private javax.swing.JLabel titulo;
    private javax.swing.JTextField txtAdmin;
    private javax.swing.JPasswordField txtSenhaAdmin;
    // End of variables declaration//GEN-END:variables

    private void onLoginClick() {

        String uname, pwd;
        boolean status = false;

        Home_admin frame = new Home_admin();

        uname = txtAdmin.getText();
        pwd = String.valueOf(txtSenhaAdmin.getPassword());

        Login_Admin loginAdmin = new Login_Admin();
        loginAdmin.setUname(uname);
        loginAdmin.setPass(pwd);

        Login_Controller_Admin model = new Login_Controller_Admin(conn);
        status = model.login(loginAdmin);

        if (status == true) {

            Home_admin.lbl_Usuario_BD.setText(txtAdmin.getText());

            JOptionPane.showMessageDialog(rootPane, "Sucesso",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

            frame.setVisible(true);
            this.dispose();

        } else if (txtAdmin.getText().equals("")
                && String.valueOf(txtSenhaAdmin.getPassword()).equals("")) {
            JOptionPane.showMessageDialog(rootPane, "O campo Usuário e Senha estão vazios",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

        } else if (txtAdmin.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "O campo Usuário está vazio",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

        } else if (String.valueOf(txtSenhaAdmin.getPassword()).equals("")) {
            JOptionPane.showMessageDialog(rootPane, "O campo Senha está vazio",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

        } else if (!txtAdmin.getText().equals(uname)) {
            isUserExist();

        } else if (!String.valueOf(txtSenhaAdmin.getPassword()).equals(pwd)) {
            IsPasswordExist();

        } else {
            JOptionPane.showMessageDialog(rootPane, "Usuário ou Senha incorreto",
                    "Info", JOptionPane.ERROR_MESSAGE);
        }

    }

    private boolean isUserExist() {
        boolean status = false;
        try {
            String sql = "SELECT Usuário_admin FROM Contas_admin";
            pst = ConnectDB.getConnection().prepareStatement(sql);
            pst.setString(1, txtAdmin.getText());
            rst = pst.executeQuery();

            if (rst.next()) {
                status = true;
            } else {
                JOptionPane.showMessageDialog(rootPane, "Usuário Inválido");
            }
            rst.close();
        } catch (SQLException ex) {
            Logger.getLogger(login1_admin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    private boolean IsPasswordExist() {
        boolean status = false;
        try {
            String sql = "SELECT Senha_admin FROM Contas_admin";
            pst = ConnectDB.getConnection().prepareStatement(sql);
            pst.setString(1, txtSenhaAdmin.getPassword().toString());
            rst = pst.executeQuery();

            if (rst.next()) {
                status = true;
            } else {
                JOptionPane.showMessageDialog(rootPane, "Senha Inválida");
            }
            rst.close();
        } catch (SQLException ex) {
            Logger.getLogger(login1_admin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }
}
