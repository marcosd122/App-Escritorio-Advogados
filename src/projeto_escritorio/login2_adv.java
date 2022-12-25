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
import static projeto_escritorio.Home_adv.lbl_Usuario_BD;

public class login2_adv extends javax.swing.JFrame {

    private Connection conn;
    private ResultSet rst;
    private PreparedStatement pst;

    public login2_adv() {
        conn = ConnectDB.getConnectionCliente();
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
        jLabel7 = new javax.swing.JLabel();
        pnl_adv = new javax.swing.JPanel();
        fechar = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        txtnome_adv = new javax.swing.JTextField();
        txtSenha_adv = new javax.swing.JPasswordField();
        login3 = new javax.swing.JLabel();
        minimizar = new javax.swing.JLabel();
        login = new javax.swing.JLabel();
        login1 = new javax.swing.JLabel();
        btnlogin_adv = new javax.swing.JButton();
        Voltar = new javax.swing.JLabel();
        mostrarsenha_adv = new javax.swing.JCheckBox();
        separador1 = new javax.swing.JSeparator();
        separador2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(79, 195, 247));
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

        jLabel7.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel7.setText("Banco de Dados Aplicação (3º ADS) - Professor Leandro Ferrarezi");

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

        txtnome_adv.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtnome_adv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtnome_adv.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));

        txtSenha_adv.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtSenha_adv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSenha_adv.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));

        login3.setBackground(new java.awt.Color(255, 255, 255));
        login3.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        login3.setForeground(new java.awt.Color(102, 102, 102));
        login3.setText("Advogado");

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

        btnlogin_adv.setBackground(new java.awt.Color(0, 153, 204));
        btnlogin_adv.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btnlogin_adv.setForeground(new java.awt.Color(255, 255, 255));
        btnlogin_adv.setText("LOGIN");
        btnlogin_adv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlogin_advActionPerformed(evt);
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

        mostrarsenha_adv.setBackground(new java.awt.Color(255, 255, 255));
        mostrarsenha_adv.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        mostrarsenha_adv.setText("Mostrar a senha");
        mostrarsenha_adv.setAlignmentX(0.5F);
        mostrarsenha_adv.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mostrarsenha_adv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarsenha_advActionPerformed(evt);
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
                                .addGroup(pnl_advLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_advLayout.createSequentialGroup()
                                        .addComponent(login3)
                                        .addGap(136, 136, 136))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_advLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(mostrarsenha_adv)
                                        .addGroup(pnl_advLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtSenha_adv, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                                            .addComponent(txtnome_adv)
                                            .addComponent(login1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(55, 55, 55))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_advLayout.createSequentialGroup()
                                .addComponent(btnlogin_adv, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46))))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtnome_adv, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(login1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSenha_adv, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(mostrarsenha_adv)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(btnlogin_adv)
                .addGap(51, 51, 51))
        );

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(titulo)))
                .addContainerGap())
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addContainerGap(433, Short.MAX_VALUE)
                .addComponent(pnl_adv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(438, Short.MAX_VALUE))
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(titulo)
                .addGap(65, 65, 65)
                .addComponent(pnl_adv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(35, 35, 35))
        );

        bg.add(kGradientPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-11, -40, 1410, 770));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(separador2)
                    .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(separador1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(separador1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, 724, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(separador2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static String showname;

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

    private void btnlogin_advActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlogin_advActionPerformed

        try {
            Date currentDate = GregorianCalendar.getInstance().getTime();
            DateFormat df = DateFormat.getDateInstance();
            String dateString = df.format(currentDate);

            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String timeString = sdf.format(d);

            String value0 = timeString;
            String values = dateString;

            String value = txtnome_adv.getText();

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

    }//GEN-LAST:event_btnlogin_advActionPerformed

    private void mostrarsenha_advActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrarsenha_advActionPerformed

        if (mostrarsenha_adv.isSelected()) {
            txtSenha_adv.setEchoChar((char) 0);
        } else {

            txtSenha_adv.setEchoChar('*');
        }

    }//GEN-LAST:event_mostrarsenha_advActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login2_adv().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Voltar;
    private javax.swing.JPanel bg;
    private javax.swing.JButton btnlogin_adv;
    private javax.swing.JLabel fechar;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator2;
    private keeptoo.KGradientPanel kGradientPanel2;
    private javax.swing.JLabel login;
    private javax.swing.JLabel login1;
    private javax.swing.JLabel login3;
    private javax.swing.JLabel minimizar;
    private javax.swing.JCheckBox mostrarsenha_adv;
    private javax.swing.JPanel pnl_adv;
    private javax.swing.JSeparator separador1;
    private javax.swing.JSeparator separador2;
    private javax.swing.JLabel titulo;
    private javax.swing.JPasswordField txtSenha_adv;
    private javax.swing.JTextField txtnome_adv;
    // End of variables declaration//GEN-END:variables

    private void onLoginClick() {

        String name, upwd;

        boolean status = false;

        Home_adv frame = new Home_adv();

        name = txtnome_adv.getText();
        upwd = String.valueOf(txtSenha_adv.getPassword());

        Login_User loginUser = new Login_User();
        loginUser.setUsername(name);
        loginUser.setUserpass(upwd);

        Login_Controller_User model = new Login_Controller_User(conn);
        status = model.login(loginUser);

        if (status == true) {

           Home_adv.lbl_Usuario_BD.setText(txtnome_adv.getText());
           
            JOptionPane.showMessageDialog(rootPane, "Sucesso",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

            frame.setVisible(true);
            this.dispose();

        } else if (txtnome_adv.getText().equals("")
                && String.valueOf(txtSenha_adv.getPassword()).equals("")) {
            JOptionPane.showMessageDialog(rootPane, "O campo Usuário e Senha estão vazios",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

        } else if (txtnome_adv.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "O campo Usuário está vazio",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

        } else if (String.valueOf(txtSenha_adv.getPassword()).equals("")) {
            JOptionPane.showMessageDialog(rootPane, "O campo Senha está vazio",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

        } else if (!txtnome_adv.getText().equals(name)) {
            isUserExist();

        } else if (!String.valueOf(txtSenha_adv.getPassword()).equals(upwd)) {
            IsPasswordExist();

        } else {

            JOptionPane.showMessageDialog(rootPane, "Usuário ou Senha incorreto",
                    "Info", JOptionPane.ERROR_MESSAGE);

        }

    }

    private boolean isUserExist() {

        boolean status = false;

        try {
            String sql = "SELECT Usuário_adv FROM Contas_user";
            pst = ConnectDB.getConnection().prepareStatement(sql);
            pst.setString(1, txtnome_adv.getText());
            rst = pst.executeQuery();

            if (rst.next()) {

                status = true;

            } else {

                JOptionPane.showMessageDialog(rootPane, "Usuário Inválido");
            }
            rst.close();

        } catch (SQLException ex) {
            Logger.getLogger(login2_adv.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    private boolean IsPasswordExist() {

        boolean status = false;

        try {
            String sql = "SELECT Senha_adv FROM Contas_user";
            pst = ConnectDB.getConnection().prepareStatement(sql);
            pst.setString(2, txtSenha_adv.getPassword().toString());
            rst = pst.executeQuery();

            if (rst.next()) {

                status = true;

            } else {
                JOptionPane.showMessageDialog(rootPane, "Senha Inválida");
            }
            rst.close();

        } catch (SQLException ex) {
            Logger.getLogger(login2_adv.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public String getUser() {

        try {
            String sql = "SELECT Usuário_adv FROM Contas_user";
            pst = ConnectDB.getConnection().prepareStatement(sql);
            pst.setString(1, txtnome_adv.getText());
            rst = pst.executeQuery();

            if (rst.next()) {

            } else {

                JOptionPane.showMessageDialog(rootPane, "Usuário Inválido");
            }
            rst.close();

        } catch (SQLException ex) {
            Logger.getLogger(login2_adv.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
