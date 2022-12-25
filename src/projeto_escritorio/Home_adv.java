package projeto_escritorio;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;

import java.awt.Color;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.JColorChooser;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import rojerusan.RSPanelsSlider;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.GregorianCalendar;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.proteanit.sql.DbUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Home_adv extends javax.swing.JFrame {

    Connection conn = null;
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rst = null;
    Statement st = null;

    String logradouro = "";
    String Bairro = "";
    String cidade = "";
    String uf = "";

    public Home_adv() {

        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");

        setUndecorated(true);

        initComponents();

        conn = ConnectDB.getConnectionCliente();
        con = ConnectDB.getConnection();

        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.black);
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/img/icojustice3.png")).getImage());
        adv_Combo();
        numClientes();
        mostrarData();
        mostrarHora();
        Update_Clientes();
        Update_Pesquisa();
        Update_Agenda();
        get_Total();

    }

    void mostrarData() {
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date();
        exibir_data.setText(s.format(d));
    }

    void mostrarHora() {
        new Timer(0, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");
                Date d = new Date();
                exibir_hora.setText(s.format(d));
            }
        }).start();
    }

    private void Update_Clientes() {

        try {
            String sql = "select * from Clientes";
            pst = conn.prepareStatement(sql);
            rst = pst.executeQuery();
            tblClientes.setModel(DbUtils.resultSetToTableModel(rst));
            numClientes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {

            try {
                rst.close();
                pst.close();

            } catch (Exception e) {

            }
        }
    }

    private void Update_Pesquisa() {

        try {
            String sql = "Select Nome, Processo, Audiência, Telefone from Clientes where Nome LIKE ?" + "Order by Nome ASC";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtPesquisarCliente.getText() + "%");
            rst = pst.executeQuery();
            tblClientesPesquisa.setModel(DbUtils.resultSetToTableModel(rst));
            numClientes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {

            try {
                rst.close();
                pst.close();

            } catch (Exception e) {

            }
        }
    }

    private void Update_Agenda() {

        try {
            String sql = "select Audiência, Nome, Processo, Telefone from Agenda_Clientes Order by Audiência DESC";
            pst = conn.prepareStatement(sql);
            rst = pst.executeQuery();
            tblClientesAgenda.setModel(DbUtils.resultSetToTableModel(rst));
            numClientes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {

            try {
                rst.close();
                pst.close();

            } catch (Exception e) {

            }
        }
    }

    private void adv_Combo() {

        try {
            String sql = "SELECT * from Contas_user";
            pst = con.prepareStatement(sql);
            rst = pst.executeQuery();

            while (rst.next()) {
                String name = rst.getString("Usuário_adv");
                adv_Interno.addItem(name);
                adv_Interno1.addItem(name);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void getImagePesquisa() {

        try {
            int row = tblClientesPesquisa.getSelectedRow();
            String tc = tblClientesPesquisa.getModel().getValueAt(row, 0).toString();

            String sql = "Select * from Clientes where Nome = '" + tc + "' " + "Order by Nome ASC";

            pst = conn.prepareStatement(sql);

            rst = pst.executeQuery();

            byte[] img_DB = rst.getBytes("Foto");
            format = new ImageIcon(img_DB);
            ImageIcon format = new ImageIcon(scaledImage(img_DB, lbl_Pesquisar.getWidth(), lbl_Pesquisar.getHeight()));
            lbl_Pesquisar.setIcon(format);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Cadastro sem foto");
        } finally {
            try {
                rst.close();
                pst.close();

            } catch (Exception e) {
            }
        }
    }

    private void pesquisar_Cliente() {

        String sql = "Select Nome, Processo, Audiência, Telefone from Clientes where Nome LIKE ? OR Processo LIKE ?" + "Order by Nome ASC";

        try {
            pst = conn.prepareStatement(sql);

            pst.setString(1, txtPesquisarCliente.getText() + "%");
            pst.setString(2, txtPesquisarCliente.getText() + "%");

            rst = pst.executeQuery();

            tblClientesPesquisa.setModel(DbUtils.resultSetToTableModel(rst));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        } finally {
            try {
                rst.close();
                pst.close();
            } catch (Exception e) {

            }
        }

        lbl_Pesquisar.setIcon(null);

    }

    private void getEdit() {

        if (!tblClientes.getSelectionModel().isSelectionEmpty()) {

            int rowIndex = tblClientes.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) tblClientes.getModel();

            txtIDClientes.setText(model.getValueAt(rowIndex, 0).toString());

            txtNomeCliente1.setText(model.getValueAt(rowIndex, 1).toString());
            txtProcesso1.setText(model.getValueAt(rowIndex, 2).toString());

            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(model.getValueAt(rowIndex, 3).toString());
                txtAudiencia1.setDate(date);
            } catch (ParseException ex) {
                Logger.getLogger(Home_adv.class.getName()).log(Level.SEVERE, null, ex);
            }

            txtCPF1.setText(model.getValueAt(rowIndex, 4).toString());

            try {
                Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(model.getValueAt(rowIndex, 5).toString());
                txtDtnasc1.setDate(date1);
            } catch (ParseException ex) {
                Logger.getLogger(Home_adv.class.getName()).log(Level.SEVERE, null, ex);
            }

            txtEstcivil1.setSelectedItem(model.getValueAt(rowIndex, 6).toString());
            txtEmail1.setText(model.getValueAt(rowIndex, 7).toString());
            txtFone2.setText(model.getValueAt(rowIndex, 8).toString());
            txtCEP1.setText(model.getValueAt(rowIndex, 9).toString());
            txtEndereco1.setText(model.getValueAt(rowIndex, 10).toString());
            txtComp1.setText(model.getValueAt(rowIndex, 11).toString());
            txtNum1.setText(model.getValueAt(rowIndex, 12).toString());
            txtBairro1.setText(model.getValueAt(rowIndex, 13).toString());
            txtCidade1.setText(model.getValueAt(rowIndex, 14).toString());
            txtEstado1.setSelectedItem(model.getValueAt(rowIndex, 15).toString());

            String combo;
            combo = model.getValueAt(rowIndex, 16).toString();
            adv_Interno1.setSelectedItem(combo);

            lblStatus1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/verdadeiro.png")));

            try {

                byte[] byteImg = (byte[]) tblClientes.getValueAt(rowIndex, 17);
                format = new ImageIcon(byteImg);
                ImageIcon format = new ImageIcon(scaledImage(byteImg, lbl_img1.getWidth(), lbl_img1.getHeight()));
                lbl_img1.setIcon(format);

            } catch (Exception e) {

            } finally {

                try {

                    rst.close();
                    pst.close();

                } catch (Exception e) {

                }
            }

        }
    }
    
      private void get_Total() {
   
          try{
              
         pst = conn.prepareStatement("select count(*) from Clientes");
          rst = pst.executeQuery();
         while(rst.next()) {
                  int count = rst.getInt(1);
          lbl_Clientes.setText(String.valueOf(count));    
         }
 

             try{ 
         
          pst = con.prepareStatement("select count(*) from Contas_user");
          rst = pst.executeQuery();
               while(rst.next()) {
              int count1 = rst.getInt(1);
          lbl_advogado.setText(String.valueOf(count1));
         }
          
         
          
      }catch (Exception e){
}
             
                   
      }catch (Exception e){
}
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        Barralt = new javax.swing.JPanel();
        pnlMenu = new javax.swing.JPanel();
        exibir_hora = new javax.swing.JLabel();
        exibir_data = new javax.swing.JLabel();
        Barramenu = new javax.swing.JPanel();
        home = new javax.swing.JPanel();
        icon1 = new javax.swing.JLabel();
        btn_home = new javax.swing.JButton();
        novo = new javax.swing.JPanel();
        icon2 = new javax.swing.JLabel();
        btn_novo = new javax.swing.JButton();
        editar = new javax.swing.JPanel();
        icon3 = new javax.swing.JLabel();
        btn_editar = new javax.swing.JButton();
        pesquisar = new javax.swing.JPanel();
        icon4 = new javax.swing.JLabel();
        btn_pesquisar = new javax.swing.JButton();
        agenda = new javax.swing.JPanel();
        icon6 = new javax.swing.JLabel();
        btn_agenda = new javax.swing.JButton();
        configuracao = new javax.swing.JPanel();
        icon8 = new javax.swing.JLabel();
        btn_configuracao = new javax.swing.JButton();
        sobre = new javax.swing.JPanel();
        icon7 = new javax.swing.JLabel();
        btn_sobre = new javax.swing.JButton();
        btnLogof = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        txtTotalCliBD = new javax.swing.JLabel();
        lbl_Usuario_BD = new javax.swing.JLabel();
        msg = new javax.swing.JLabel();
        pnlMenu1 = new javax.swing.JPanel();
        BG = new javax.swing.JPanel();
        pnlBSup = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        balanca = new javax.swing.JLabel();
        titulo1 = new javax.swing.JLabel();
        minimizar = new javax.swing.JLabel();
        fechar = new javax.swing.JLabel();
        maximizar = new javax.swing.JLabel();
        bgrodape = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        rSPanelsSlider1 = new rojerusan.RSPanelsSlider();
        Pnl_Home = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        labelTit = new javax.swing.JLabel();
        labeltext1 = new javax.swing.JLabel();
        labeltext2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lbl_Clientes = new javax.swing.JLabel();
        lbl_advogado = new javax.swing.JLabel();
        Pnl_Novo = new javax.swing.JPanel();
        pnlCad2 = new javax.swing.JPanel();
        cep = new javax.swing.JLabel();
        txtEndereco = new javax.swing.JTextField();
        end = new javax.swing.JLabel();
        cid = new javax.swing.JLabel();
        txtCidade = new javax.swing.JTextField();
        txtNum = new javax.swing.JTextField();
        Num = new javax.swing.JLabel();
        est = new javax.swing.JLabel();
        txtComp = new javax.swing.JTextField();
        comp = new javax.swing.JLabel();
        txtBairro = new javax.swing.JTextField();
        bairro = new javax.swing.JLabel();
        btn_BuscarCEP = new javax.swing.JButton();
        txtEstado = new javax.swing.JComboBox<>();
        txtCEP = new javax.swing.JFormattedTextField();
        lblStatus = new javax.swing.JLabel();
        btnSalvarCliente = new javax.swing.JButton();
        btnLimparClient = new javax.swing.JButton();
        pnlCad1 = new javax.swing.JPanel();
        Nome = new javax.swing.JLabel();
        Estado_civil = new javax.swing.JLabel();
        CPF = new javax.swing.JLabel();
        DtNasc = new javax.swing.JLabel();
        txtNomeCliente = new javax.swing.JTextField();
        txtCPF = new javax.swing.JFormattedTextField();
        txtEstcivil = new javax.swing.JComboBox<>();
        Tel1 = new javax.swing.JLabel();
        txtFone1 = new javax.swing.JFormattedTextField();
        email = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        proces = new javax.swing.JLabel();
        txtProcesso = new javax.swing.JFormattedTextField();
        txtDtnasc = new com.toedter.calendar.JDateChooser();
        txtAudiencia = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        lbl_img = new javax.swing.JLabel();
        btn_img = new javax.swing.JButton();
        adv_Interno = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        Pnl_Atualizar = new javax.swing.JPanel();
        pnlCad4 = new javax.swing.JPanel();
        Nome2 = new javax.swing.JLabel();
        Estado_civil2 = new javax.swing.JLabel();
        CPF2 = new javax.swing.JLabel();
        DtNasc2 = new javax.swing.JLabel();
        txtNomeCliente1 = new javax.swing.JTextField();
        txtCPF1 = new javax.swing.JFormattedTextField();
        txtEstcivil1 = new javax.swing.JComboBox<>();
        Tel3 = new javax.swing.JLabel();
        txtFone2 = new javax.swing.JFormattedTextField();
        email2 = new javax.swing.JLabel();
        txtEmail1 = new javax.swing.JTextField();
        proces2 = new javax.swing.JLabel();
        txtProcesso1 = new javax.swing.JFormattedTextField();
        txtDtnasc1 = new com.toedter.calendar.JDateChooser();
        txtAudiencia1 = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        lbl_img1 = new javax.swing.JLabel();
        btn_img1 = new javax.swing.JButton();
        adv_Interno1 = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        pnlCad5 = new javax.swing.JPanel();
        cep1 = new javax.swing.JLabel();
        txtEndereco1 = new javax.swing.JTextField();
        end1 = new javax.swing.JLabel();
        cid1 = new javax.swing.JLabel();
        txtCidade1 = new javax.swing.JTextField();
        txtNum1 = new javax.swing.JTextField();
        Num1 = new javax.swing.JLabel();
        est1 = new javax.swing.JLabel();
        txtComp1 = new javax.swing.JTextField();
        comp1 = new javax.swing.JLabel();
        txtBairro1 = new javax.swing.JTextField();
        bairro1 = new javax.swing.JLabel();
        btn_Buscar_Update = new javax.swing.JButton();
        txtEstado1 = new javax.swing.JComboBox<>();
        txtCEP1 = new javax.swing.JFormattedTextField();
        lblStatus1 = new javax.swing.JLabel();
        btnUpdateCliente = new javax.swing.JButton();
        btnLimparCli_Update = new javax.swing.JButton();
        txtIDClientes = new javax.swing.JTextField();
        Pnl_Editar = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        btn_Editar_Cli = new javax.swing.JButton();
        btnExcluirCliente = new javax.swing.JButton();
        btnExportarExcel = new javax.swing.JButton();
        lbl_TotalClientes = new javax.swing.JLabel();
        Pnl_Pesquisar = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblClientesPesquisa = new javax.swing.JTable();
        txtPesquisarCliente = new javax.swing.JTextField();
        lbl_Pesquisar = new javax.swing.JLabel();
        Total_Cli_Up = new javax.swing.JLabel();
        Pnl_Agenda = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblClientesAgenda = new javax.swing.JTable();
        btnImprimir = new javax.swing.JButton();
        btn_Finalizar_Agenda = new javax.swing.JButton();
        Pnl_Configuracao = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        btnConfCor = new javax.swing.JButton();
        btnConfCor1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jSlider1 = new javax.swing.JSlider();
        jLabel9 = new javax.swing.JLabel();
        lblOpaci = new javax.swing.JLabel();
        Pnl_Sobre = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        jLabel1.setText("jLabel1");

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
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

        Barralt.setBackground(new java.awt.Color(0, 137, 123));

        javax.swing.GroupLayout BarraltLayout = new javax.swing.GroupLayout(Barralt);
        Barralt.setLayout(BarraltLayout);
        BarraltLayout.setHorizontalGroup(
            BarraltLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        BarraltLayout.setVerticalGroup(
            BarraltLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        pnlMenu.setBackground(new java.awt.Color(255, 199, 113));

        exibir_hora.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        exibir_hora.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        exibir_hora.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        exibir_data.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        exibir_data.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        exibir_data.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        Barramenu.setBackground(new java.awt.Color(255, 255, 255));
        Barramenu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Barramenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        home.setBackground(new java.awt.Color(255, 255, 255));

        icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/home.png"))); // NOI18N
        icon1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                icon1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                icon1MouseExited(evt);
            }
        });

        btn_home.setBackground(new java.awt.Color(255, 255, 255));
        btn_home.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        btn_home.setText("Home");
        btn_home.setBorder(null);
        btn_home.setContentAreaFilled(false);
        btn_home.setDoubleBuffered(true);
        btn_home.setFocusCycleRoot(true);
        btn_home.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_homeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(icon1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_home)
                .addContainerGap(94, Short.MAX_VALUE))
        );
        homeLayout.setVerticalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(icon1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(homeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_home)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Barramenu.add(home, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 13, 220, -1));

        novo.setBackground(new java.awt.Color(255, 255, 255));

        icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/user.png"))); // NOI18N
        icon2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                icon2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                icon2MouseExited(evt);
            }
        });

        btn_novo.setBackground(new java.awt.Color(255, 255, 255));
        btn_novo.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        btn_novo.setText("Novo Cliente");
        btn_novo.setToolTipText("");
        btn_novo.setBorder(null);
        btn_novo.setContentAreaFilled(false);
        btn_novo.setDoubleBuffered(true);
        btn_novo.setFocusCycleRoot(true);
        btn_novo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_novo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_novo.setName("Novo"); // NOI18N
        btn_novo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_novoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout novoLayout = new javax.swing.GroupLayout(novo);
        novo.setLayout(novoLayout);
        novoLayout.setHorizontalGroup(
            novoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(novoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(icon2)
                .addGap(6, 6, 6)
                .addComponent(btn_novo)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        novoLayout.setVerticalGroup(
            novoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(icon2, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, novoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_novo)
                .addGap(4, 4, 4))
        );

        Barramenu.add(novo, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 88, 220, -1));

        editar.setBackground(new java.awt.Color(255, 255, 255));

        icon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"))); // NOI18N
        icon3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                icon3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                icon3MouseExited(evt);
            }
        });

        btn_editar.setBackground(new java.awt.Color(255, 255, 255));
        btn_editar.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        btn_editar.setText("Editar Cliente");
        btn_editar.setBorder(null);
        btn_editar.setContentAreaFilled(false);
        btn_editar.setDoubleBuffered(true);
        btn_editar.setFocusCycleRoot(true);
        btn_editar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_editar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_editar.setName("Editar"); // NOI18N
        btn_editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout editarLayout = new javax.swing.GroupLayout(editar);
        editar.setLayout(editarLayout);
        editarLayout.setHorizontalGroup(
            editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(icon3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_editar)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        editarLayout.setVerticalGroup(
            editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(icon3, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_editar)
                .addGap(4, 4, 4))
        );

        Barramenu.add(editar, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 163, 220, -1));

        pesquisar.setBackground(new java.awt.Color(255, 255, 255));

        icon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pesquisa.png"))); // NOI18N
        icon4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                icon4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                icon4MouseExited(evt);
            }
        });

        btn_pesquisar.setBackground(new java.awt.Color(255, 255, 255));
        btn_pesquisar.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        btn_pesquisar.setText("Pesquisar");
        btn_pesquisar.setBorder(null);
        btn_pesquisar.setContentAreaFilled(false);
        btn_pesquisar.setDoubleBuffered(true);
        btn_pesquisar.setFocusCycleRoot(true);
        btn_pesquisar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_pesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pesquisarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pesquisarLayout = new javax.swing.GroupLayout(pesquisar);
        pesquisar.setLayout(pesquisarLayout);
        pesquisarLayout.setHorizontalGroup(
            pesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pesquisarLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(icon4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_pesquisar)
                .addContainerGap(53, Short.MAX_VALUE))
        );
        pesquisarLayout.setVerticalGroup(
            pesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(icon4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pesquisarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_pesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Barramenu.add(pesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 238, 220, -1));

        agenda.setBackground(new java.awt.Color(255, 255, 255));

        icon6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/agenda.png"))); // NOI18N
        icon6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                icon6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                icon6MouseExited(evt);
            }
        });

        btn_agenda.setBackground(new java.awt.Color(255, 255, 255));
        btn_agenda.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        btn_agenda.setText("Agenda");
        btn_agenda.setBorder(null);
        btn_agenda.setContentAreaFilled(false);
        btn_agenda.setDoubleBuffered(true);
        btn_agenda.setFocusCycleRoot(true);
        btn_agenda.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_agenda.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_agenda.setName("agenda"); // NOI18N
        btn_agenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_agendaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout agendaLayout = new javax.swing.GroupLayout(agenda);
        agenda.setLayout(agendaLayout);
        agendaLayout.setHorizontalGroup(
            agendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(agendaLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(icon6, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_agenda)
                .addContainerGap(75, Short.MAX_VALUE))
        );
        agendaLayout.setVerticalGroup(
            agendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(icon6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(agendaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_agenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Barramenu.add(agenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 313, 220, -1));

        configuracao.setBackground(new java.awt.Color(255, 255, 255));

        icon8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/config.png"))); // NOI18N
        icon8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                icon8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                icon8MouseExited(evt);
            }
        });

        btn_configuracao.setBackground(new java.awt.Color(255, 255, 255));
        btn_configuracao.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        btn_configuracao.setText("Configuração");
        btn_configuracao.setBorder(null);
        btn_configuracao.setContentAreaFilled(false);
        btn_configuracao.setDoubleBuffered(true);
        btn_configuracao.setFocusCycleRoot(true);
        btn_configuracao.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_configuracao.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_configuracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_configuracaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout configuracaoLayout = new javax.swing.GroupLayout(configuracao);
        configuracao.setLayout(configuracaoLayout);
        configuracaoLayout.setHorizontalGroup(
            configuracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configuracaoLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(icon8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_configuracao)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        configuracaoLayout.setVerticalGroup(
            configuracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(icon8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(configuracaoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_configuracao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Barramenu.add(configuracao, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 388, 220, -1));

        sobre.setBackground(new java.awt.Color(255, 255, 255));

        icon7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/info.png"))); // NOI18N
        icon7.setMaximumSize(new java.awt.Dimension(64, 64));
        icon7.setMinimumSize(new java.awt.Dimension(64, 64));
        icon7.setPreferredSize(new java.awt.Dimension(64, 64));
        icon7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                icon7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                icon7MouseExited(evt);
            }
        });

        btn_sobre.setBackground(new java.awt.Color(255, 255, 255));
        btn_sobre.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        btn_sobre.setText("Sobre");
        btn_sobre.setBorder(null);
        btn_sobre.setContentAreaFilled(false);
        btn_sobre.setDoubleBuffered(true);
        btn_sobre.setFocusCycleRoot(true);
        btn_sobre.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_sobre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_sobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sobreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sobreLayout = new javax.swing.GroupLayout(sobre);
        sobre.setLayout(sobreLayout);
        sobreLayout.setHorizontalGroup(
            sobreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sobreLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(icon7, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_sobre)
                .addContainerGap(92, Short.MAX_VALUE))
        );
        sobreLayout.setVerticalGroup(
            sobreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(icon7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sobreLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_sobre)
                .addContainerGap())
        );

        Barramenu.add(sobre, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 467, 220, 60));

        btnLogof.setBackground(new java.awt.Color(255, 255, 255));
        btnLogof.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/desligar.png"))); // NOI18N
        btnLogof.setBorder(null);
        btnLogof.setBorderPainted(false);
        btnLogof.setContentAreaFilled(false);
        btnLogof.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogofActionPerformed(evt);
            }
        });
        Barramenu.add(btnLogof, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 570, -1, -1));
        Barramenu.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 530, 230, 10));

        txtTotalCliBD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbl_Usuario_BD.setBackground(new java.awt.Color(51, 51, 51));
        lbl_Usuario_BD.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N

        msg.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        msg.setText("Bem Vindo - ");

        javax.swing.GroupLayout pnlMenuLayout = new javax.swing.GroupLayout(pnlMenu);
        pnlMenu.setLayout(pnlMenuLayout);
        pnlMenuLayout.setHorizontalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuLayout.createSequentialGroup()
                .addGroup(pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMenuLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Barramenu, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
                    .addGroup(pnlMenuLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtTotalCliBD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlMenuLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(exibir_hora, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(msg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlMenuLayout.createSequentialGroup()
                                .addGap(0, 28, Short.MAX_VALUE)
                                .addComponent(exibir_data, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlMenuLayout.createSequentialGroup()
                                .addComponent(lbl_Usuario_BD, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(18, 18, 18))
        );
        pnlMenuLayout.setVerticalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_Usuario_BD, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(msg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(exibir_hora, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exibir_data, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(txtTotalCliBD, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Barramenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlMenu1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BG.setBackground(new java.awt.Color(255, 255, 255));

        pnlBSup.setBackground(new java.awt.Color(255, 199, 113));

        jPanel2.setBackground(new java.awt.Color(255, 199, 113));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        balanca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/balança.png"))); // NOI18N
        jPanel2.add(balanca, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 49, 66));

        titulo1.setBackground(new java.awt.Color(255, 255, 255));
        titulo1.setFont(new java.awt.Font("Calibri", 1, 48)); // NOI18N
        titulo1.setText("Controle de Escritório");
        jPanel2.add(titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, 56));

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

        maximizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/maximizar1.png"))); // NOI18N
        maximizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                maximizarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                maximizarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                maximizarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnlBSupLayout = new javax.swing.GroupLayout(pnlBSup);
        pnlBSup.setLayout(pnlBSupLayout);
        pnlBSupLayout.setHorizontalGroup(
            pnlBSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBSupLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
                .addGap(272, 272, 272)
                .addComponent(minimizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(maximizar, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fechar)
                .addContainerGap())
        );
        pnlBSupLayout.setVerticalGroup(
            pnlBSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBSupLayout.createSequentialGroup()
                .addGroup(pnlBSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBSupLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlBSupLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(minimizar, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlBSupLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(pnlBSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fechar, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                            .addComponent(maximizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        bgrodape.setBackground(new java.awt.Color(255, 255, 255));
        bgrodape.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel7.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Linguagem de Programação Visual (3º ADS) - Professor Leandro Ferrarezi");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout bgrodapeLayout = new javax.swing.GroupLayout(bgrodape);
        bgrodape.setLayout(bgrodapeLayout);
        bgrodapeLayout.setHorizontalGroup(
            bgrodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        bgrodapeLayout.setVerticalGroup(
            bgrodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgrodapeLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
        );

        rSPanelsSlider1.setBackground(new java.awt.Color(255, 255, 255));

        Pnl_Home.setBackground(new java.awt.Color(255, 255, 255));
        Pnl_Home.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Home", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        Pnl_Home.setName("Pnl_Home"); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        labelTit.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        labelTit.setForeground(new java.awt.Color(255, 153, 0));
        labelTit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTit.setText("Software Jurídico");

        labeltext1.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        labeltext1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labeltext1.setText("Utilize o software líder de mercado para aprimorar as rotinas do seu escritório.");

        labeltext2.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        labeltext2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labeltext2.setText("Cadastrar e controlar processos, cadastrar clientes e processos vinculados a eles entre outras funcionalidades.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(355, 355, 355)
                .addComponent(labelTit, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                .addGap(402, 402, 402))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labeltext1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labeltext2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(labelTit, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labeltext1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(labeltext2)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/user1.png"))); // NOI18N

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/adv3.png"))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("ADVOGADOS");

        jLabel12.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("CLIENTES");

        lbl_Clientes.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        lbl_Clientes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Clientes.setText("CLIENTES");

        lbl_advogado.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        lbl_advogado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_advogado.setText("ADVOGADO");

        javax.swing.GroupLayout Pnl_HomeLayout = new javax.swing.GroupLayout(Pnl_Home);
        Pnl_Home.setLayout(Pnl_HomeLayout);
        Pnl_HomeLayout.setHorizontalGroup(
            Pnl_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_HomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_HomeLayout.createSequentialGroup()
                .addGap(265, 265, 265)
                .addGroup(Pnl_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_HomeLayout.createSequentialGroup()
                        .addGroup(Pnl_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(Pnl_HomeLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(Pnl_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addGroup(Pnl_HomeLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(Pnl_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lbl_advogado, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(290, 290, 290))
                    .addGroup(Pnl_HomeLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_Clientes, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        Pnl_HomeLayout.setVerticalGroup(
            Pnl_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_HomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(77, 77, 77)
                .addGroup(Pnl_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Pnl_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Pnl_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Clientes)
                    .addComponent(lbl_advogado))
                .addContainerGap(86, Short.MAX_VALUE))
        );

        rSPanelsSlider1.add(Pnl_Home, "card1");

        Pnl_Novo.setBackground(new java.awt.Color(255, 255, 255));
        Pnl_Novo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de Clientes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        Pnl_Novo.setName("Pnl_Novo"); // NOI18N

        pnlCad2.setBackground(new java.awt.Color(255, 255, 255));
        pnlCad2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cep.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        cep.setText("CEP");

        txtEndereco.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        end.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        end.setText("Endereço:");

        cid.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        cid.setText("Cidade:");

        txtCidade.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        txtNum.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        Num.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        Num.setText("Número:");

        est.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        est.setText("Estado:");

        txtComp.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        comp.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        comp.setText("Complemento:");

        txtBairro.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        bairro.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        bairro.setText("Bairro:");

        btn_BuscarCEP.setBackground(new java.awt.Color(255, 255, 255));
        btn_BuscarCEP.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btn_BuscarCEP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/endereço-50.png"))); // NOI18N
        btn_BuscarCEP.setText("BUSCAR");
        btn_BuscarCEP.setBorder(null);
        btn_BuscarCEP.setContentAreaFilled(false);
        btn_BuscarCEP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarCEPActionPerformed(evt);
            }
        });

        txtEstado.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));

        try {
            txtCEP.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCEP.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtCEP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCEPKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnlCad2Layout = new javax.swing.GroupLayout(pnlCad2);
        pnlCad2.setLayout(pnlCad2Layout);
        pnlCad2Layout.setHorizontalGroup(
            pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCad2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCad2Layout.createSequentialGroup()
                        .addGroup(pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCad2Layout.createSequentialGroup()
                                .addComponent(cep)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCEP, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_BuscarCEP)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCad2Layout.createSequentialGroup()
                        .addGroup(pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(end)
                            .addGroup(pnlCad2Layout.createSequentialGroup()
                                .addGroup(pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Num)
                                    .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(77, 77, 77)
                                .addGroup(pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bairro)))
                            .addComponent(txtEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addGroup(pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlCad2Layout.createSequentialGroup()
                                .addGroup(pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cid))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(est)
                                    .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(148, 148, 148))
                            .addGroup(pnlCad2Layout.createSequentialGroup()
                                .addGroup(pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comp)
                                    .addComponent(txtComp, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(74, Short.MAX_VALUE))))))
        );
        pnlCad2Layout.setVerticalGroup(
            pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCad2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlCad2Layout.createSequentialGroup()
                        .addComponent(comp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtComp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlCad2Layout.createSequentialGroup()
                        .addGroup(pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(pnlCad2Layout.createSequentialGroup()
                                .addGroup(pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cep)
                                    .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCEP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2))
                            .addComponent(btn_BuscarCEP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(end)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCad2Layout.createSequentialGroup()
                        .addComponent(cid)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlCad2Layout.createSequentialGroup()
                        .addComponent(Num)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlCad2Layout.createSequentialGroup()
                        .addComponent(bairro, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(est))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        btnSalvarCliente.setBackground(new java.awt.Color(255, 255, 255));
        btnSalvarCliente.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btnSalvarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save.png"))); // NOI18N
        btnSalvarCliente.setText("SALVAR");
        btnSalvarCliente.setBorder(null);
        btnSalvarCliente.setContentAreaFilled(false);
        btnSalvarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarClienteActionPerformed(evt);
            }
        });

        btnLimparClient.setBackground(new java.awt.Color(255, 255, 255));
        btnLimparClient.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btnLimparClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/limpar.png"))); // NOI18N
        btnLimparClient.setText("LIMPAR");
        btnLimparClient.setBorder(null);
        btnLimparClient.setContentAreaFilled(false);
        btnLimparClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparClientActionPerformed(evt);
            }
        });

        pnlCad1.setBackground(new java.awt.Color(255, 255, 255));
        pnlCad1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Nome.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        Nome.setText("Nome:");

        Estado_civil.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        Estado_civil.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        Estado_civil.setText("Estado Civil");
        Estado_civil.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        CPF.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        CPF.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        CPF.setText("CPF:");
        CPF.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        DtNasc.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        DtNasc.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        DtNasc.setText("Data de Nascimento");
        DtNasc.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        txtNomeCliente.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        try {
            txtCPF.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCPF.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        txtEstcivil.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtEstcivil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Solteiro(a)", "Casado(a)", "Divorciado(a)", "Separado(a) de Fato / Judicialmente", "Viúvo(a)", "União Estável" }));

        Tel1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        Tel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        Tel1.setText("Telefone 1:");
        Tel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        try {
            txtFone1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#.####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtFone1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        email.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        email.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        email.setText("Email:");
        email.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        txtEmail.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        proces.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        proces.setText("Processo N.:");

        try {
            txtProcesso.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#######-##.####.#.##.#### ")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtProcesso.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        txtDtnasc.setDateFormatString("dd/MM/yyyy"); // NOI18N

        txtAudiencia.setDateFormatString("dd/MM/yyyy"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel2.setText("Data da Audiência");

        lbl_img.setBackground(new java.awt.Color(51, 51, 51));
        lbl_img.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_img.setAlignmentY(0.0F);
        lbl_img.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lbl_img.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lbl_img.setMaximumSize(new java.awt.Dimension(140, 140));
        lbl_img.setMinimumSize(new java.awt.Dimension(140, 140));

        btn_img.setBackground(new java.awt.Color(255, 255, 255));
        btn_img.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btn_img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/upload-24.png"))); // NOI18N
        btn_img.setText("UPLOAD");
        btn_img.setBorder(null);
        btn_img.setContentAreaFilled(false);
        btn_img.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_imgActionPerformed(evt);
            }
        });

        adv_Interno.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        adv_Interno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        jLabel3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel3.setText("Advogado Interno");

        javax.swing.GroupLayout pnlCad1Layout = new javax.swing.GroupLayout(pnlCad1);
        pnlCad1.setLayout(pnlCad1Layout);
        pnlCad1Layout.setHorizontalGroup(
            pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCad1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(email)
                    .addComponent(Nome)
                    .addGroup(pnlCad1Layout.createSequentialGroup()
                        .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCPF, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CPF))
                        .addGap(60, 60, 60)
                        .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DtNasc)
                            .addComponent(txtDtnasc, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtNomeCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Tel1)
                    .addComponent(txtFone1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlCad1Layout.createSequentialGroup()
                        .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Estado_civil)
                            .addComponent(proces)
                            .addComponent(txtProcesso, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEstcivil, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)
                        .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(txtAudiencia, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(adv_Interno, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCad1Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(btn_img)
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCad1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_img, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        pnlCad1Layout.setVerticalGroup(
            pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCad1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCad1Layout.createSequentialGroup()
                        .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCad1Layout.createSequentialGroup()
                                .addComponent(proces)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtProcesso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Estado_civil)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtEstcivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(adv_Interno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(Tel1))
                            .addComponent(lbl_img, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                        .addComponent(btn_img))
                    .addGroup(pnlCad1Layout.createSequentialGroup()
                        .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Nome)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAudiencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlCad1Layout.createSequentialGroup()
                                .addComponent(DtNasc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtDtnasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlCad1Layout.createSequentialGroup()
                                .addComponent(CPF)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCPF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(email)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFone1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout Pnl_NovoLayout = new javax.swing.GroupLayout(Pnl_Novo);
        Pnl_Novo.setLayout(Pnl_NovoLayout);
        Pnl_NovoLayout.setHorizontalGroup(
            Pnl_NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_NovoLayout.createSequentialGroup()
                .addGap(428, 428, 428)
                .addComponent(btnSalvarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(111, 111, 111)
                .addComponent(btnLimparClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(439, 439, 439))
            .addGroup(Pnl_NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_NovoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlCad2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(Pnl_NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_NovoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlCad1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        Pnl_NovoLayout.setVerticalGroup(
            Pnl_NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_NovoLayout.createSequentialGroup()
                .addContainerGap(502, Short.MAX_VALUE)
                .addGroup(Pnl_NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSalvarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimparClient))
                .addContainerGap())
            .addGroup(Pnl_NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_NovoLayout.createSequentialGroup()
                    .addGap(249, 249, 249)
                    .addComponent(pnlCad2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(77, Short.MAX_VALUE)))
            .addGroup(Pnl_NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_NovoLayout.createSequentialGroup()
                    .addComponent(pnlCad1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 323, Short.MAX_VALUE)))
        );

        rSPanelsSlider1.add(Pnl_Novo, "card2");

        Pnl_Atualizar.setBackground(new java.awt.Color(255, 255, 255));
        Pnl_Atualizar.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Atualizar Cadastro de Clientes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        Pnl_Atualizar.setName("Pnl_Novo"); // NOI18N

        pnlCad4.setBackground(new java.awt.Color(255, 255, 255));
        pnlCad4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Nome2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        Nome2.setText("Nome:");

        Estado_civil2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        Estado_civil2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        Estado_civil2.setText("Estado Civil");
        Estado_civil2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        CPF2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        CPF2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        CPF2.setText("CPF:");
        CPF2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        DtNasc2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        DtNasc2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        DtNasc2.setText("Data de Nascimento");
        DtNasc2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        txtNomeCliente1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        try {
            txtCPF1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCPF1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        txtEstcivil1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtEstcivil1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Solteiro(a)", "Casado(a)", "Divorciado(a)", "Separado(a) de Fato / Judicialmente", "Viúvo(a)", "União Estável" }));

        Tel3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        Tel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        Tel3.setText("Telefone 1:");
        Tel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        try {
            txtFone2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#.####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtFone2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        email2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        email2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        email2.setText("Email:");
        email2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        txtEmail1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        proces2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        proces2.setText("Processo N.:");

        try {
            txtProcesso1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#######-##.####.#.##.#### ")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtProcesso1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        txtDtnasc1.setDateFormatString("dd/MM/yyyy"); // NOI18N

        txtAudiencia1.setDateFormatString("dd/MM/yyyy"); // NOI18N

        jLabel6.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel6.setText("Data da Audiência");

        lbl_img1.setBackground(new java.awt.Color(51, 51, 51));
        lbl_img1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_img1.setAlignmentY(0.0F);
        lbl_img1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lbl_img1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lbl_img1.setMaximumSize(new java.awt.Dimension(140, 140));
        lbl_img1.setMinimumSize(new java.awt.Dimension(140, 140));

        btn_img1.setBackground(new java.awt.Color(255, 255, 255));
        btn_img1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btn_img1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/upload-24.png"))); // NOI18N
        btn_img1.setText("UPLOAD");
        btn_img1.setBorder(null);
        btn_img1.setContentAreaFilled(false);
        btn_img1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_img1ActionPerformed(evt);
            }
        });

        adv_Interno1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        adv_Interno1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        jLabel8.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel8.setText("Advogado Interno");

        javax.swing.GroupLayout pnlCad4Layout = new javax.swing.GroupLayout(pnlCad4);
        pnlCad4.setLayout(pnlCad4Layout);
        pnlCad4Layout.setHorizontalGroup(
            pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCad4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(email2)
                    .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Nome2)
                        .addGroup(pnlCad4Layout.createSequentialGroup()
                            .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtCPF1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(CPF2))
                            .addGap(60, 60, 60)
                            .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(DtNasc2)
                                .addComponent(txtDtnasc1, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)))
                        .addComponent(txtNomeCliente1)
                        .addComponent(txtEmail1)))
                .addGap(40, 40, 40)
                .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Tel3)
                    .addComponent(txtFone2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlCad4Layout.createSequentialGroup()
                        .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Estado_civil2)
                            .addComponent(proces2)
                            .addComponent(txtProcesso1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEstcivil1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)
                        .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(txtAudiencia1, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                            .addComponent(adv_Interno1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(32, 32, 32)
                .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCad4Layout.createSequentialGroup()
                        .addComponent(btn_img1)
                        .addGap(31, 31, 31))
                    .addComponent(lbl_img1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pnlCad4Layout.setVerticalGroup(
            pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCad4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCad4Layout.createSequentialGroup()
                        .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCad4Layout.createSequentialGroup()
                                .addComponent(proces2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtProcesso1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Estado_civil2)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtEstcivil1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(adv_Interno1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Tel3))
                            .addComponent(lbl_img1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFone2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_img1)))
                    .addGroup(pnlCad4Layout.createSequentialGroup()
                        .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Nome2)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNomeCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAudiencia1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlCad4Layout.createSequentialGroup()
                                .addComponent(DtNasc2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtDtnasc1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlCad4Layout.createSequentialGroup()
                                .addComponent(CPF2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCPF1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(email2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmail1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCad5.setBackground(new java.awt.Color(255, 255, 255));
        pnlCad5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cep1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        cep1.setText("CEP");

        txtEndereco1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        end1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        end1.setText("Endereço:");

        cid1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        cid1.setText("Cidade:");

        txtCidade1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        txtNum1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        Num1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        Num1.setText("Número:");

        est1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        est1.setText("Estado:");

        txtComp1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        comp1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        comp1.setText("Complemento:");

        txtBairro1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        bairro1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        bairro1.setText("Bairro:");

        btn_Buscar_Update.setBackground(new java.awt.Color(255, 255, 255));
        btn_Buscar_Update.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btn_Buscar_Update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/endereço-50.png"))); // NOI18N
        btn_Buscar_Update.setText("BUSCAR");
        btn_Buscar_Update.setBorder(null);
        btn_Buscar_Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Buscar_UpdateActionPerformed(evt);
            }
        });

        txtEstado1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtEstado1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));

        try {
            txtCEP1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCEP1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        javax.swing.GroupLayout pnlCad5Layout = new javax.swing.GroupLayout(pnlCad5);
        pnlCad5.setLayout(pnlCad5Layout);
        pnlCad5Layout.setHorizontalGroup(
            pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCad5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCad5Layout.createSequentialGroup()
                        .addGroup(pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCad5Layout.createSequentialGroup()
                                .addComponent(cep1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStatus1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCEP1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Buscar_Update)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCad5Layout.createSequentialGroup()
                        .addGroup(pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(end1)
                            .addGroup(pnlCad5Layout.createSequentialGroup()
                                .addGroup(pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Num1)
                                    .addComponent(txtNum1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(77, 77, 77)
                                .addGroup(pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtBairro1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bairro1)))
                            .addComponent(txtEndereco1, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlCad5Layout.createSequentialGroup()
                                .addGroup(pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cid1)
                                    .addComponent(txtCidade1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(154, 154, 154)
                                .addGroup(pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(est1)
                                    .addComponent(txtEstado1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(148, 148, 148))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlCad5Layout.createSequentialGroup()
                                .addGroup(pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtComp1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comp1, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(82, 82, 82))))))
        );
        pnlCad5Layout.setVerticalGroup(
            pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCad5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlCad5Layout.createSequentialGroup()
                        .addComponent(comp1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtComp1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlCad5Layout.createSequentialGroup()
                        .addGroup(pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(pnlCad5Layout.createSequentialGroup()
                                .addGroup(pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cep1)
                                    .addComponent(lblStatus1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCEP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2))
                            .addComponent(btn_Buscar_Update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(end1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEndereco1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCad5Layout.createSequentialGroup()
                        .addComponent(cid1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCad5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCidade1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEstado1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlCad5Layout.createSequentialGroup()
                        .addComponent(Num1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNum1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlCad5Layout.createSequentialGroup()
                        .addComponent(bairro1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBairro1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(est1))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        btnUpdateCliente.setBackground(new java.awt.Color(255, 255, 255));
        btnUpdateCliente.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btnUpdateCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/editarususario.png"))); // NOI18N
        btnUpdateCliente.setText("SALVAR");
        btnUpdateCliente.setBorder(null);
        btnUpdateCliente.setContentAreaFilled(false);
        btnUpdateCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateClienteActionPerformed(evt);
            }
        });

        btnLimparCli_Update.setBackground(new java.awt.Color(255, 255, 255));
        btnLimparCli_Update.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btnLimparCli_Update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/limpar.png"))); // NOI18N
        btnLimparCli_Update.setText("LIMPAR");
        btnLimparCli_Update.setBorder(null);
        btnLimparCli_Update.setContentAreaFilled(false);
        btnLimparCli_Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparCli_UpdateActionPerformed(evt);
            }
        });

        txtIDClientes.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtIDClientes.setForeground(new java.awt.Color(255, 255, 255));
        txtIDClientes.setBorder(null);

        javax.swing.GroupLayout Pnl_AtualizarLayout = new javax.swing.GroupLayout(Pnl_Atualizar);
        Pnl_Atualizar.setLayout(Pnl_AtualizarLayout);
        Pnl_AtualizarLayout.setHorizontalGroup(
            Pnl_AtualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_AtualizarLayout.createSequentialGroup()
                .addGap(363, 363, 363)
                .addComponent(btnUpdateCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                .addComponent(btnLimparCli_Update)
                .addGap(400, 400, 400))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_AtualizarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtIDClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(522, 522, 522))
            .addGroup(Pnl_AtualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_AtualizarLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlCad4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(15, 15, 15)))
            .addGroup(Pnl_AtualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_AtualizarLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlCad5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        Pnl_AtualizarLayout.setVerticalGroup(
            Pnl_AtualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_AtualizarLayout.createSequentialGroup()
                .addComponent(txtIDClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 484, Short.MAX_VALUE)
                .addGroup(Pnl_AtualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLimparCli_Update)
                    .addComponent(btnUpdateCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(Pnl_AtualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_AtualizarLayout.createSequentialGroup()
                    .addGap(55, 55, 55)
                    .addComponent(pnlCad4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(311, Short.MAX_VALUE)))
            .addGroup(Pnl_AtualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_AtualizarLayout.createSequentialGroup()
                    .addGap(270, 270, 270)
                    .addComponent(pnlCad5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(72, Short.MAX_VALUE)))
        );

        rSPanelsSlider1.add(Pnl_Atualizar, "card3");

        Pnl_Editar.setBackground(new java.awt.Color(255, 255, 255));
        Pnl_Editar.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        Pnl_Editar.setName("Pnl_Editar"); // NOI18N

        tblClientes.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Processo", "Audiência", "CPF", "Data de Nascimento", "Estado Civil", "E-mail", "Telefone", "CEP", "Endereço", "Complemento", "Numero", "Bairro", "Cidade", "Estado", "Advogado Interno", "Foto"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblClientes.setGridColor(new java.awt.Color(0, 0, 0));
        tblClientes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblClientes.getTableHeader().setReorderingAllowed(false);
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        tblClientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblClientesKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblClientes);
        if (tblClientes.getColumnModel().getColumnCount() > 0) {
            tblClientes.getColumnModel().getColumn(0).setResizable(false);
            tblClientes.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblClientes.getColumnModel().getColumn(1).setResizable(false);
            tblClientes.getColumnModel().getColumn(2).setResizable(false);
            tblClientes.getColumnModel().getColumn(3).setResizable(false);
            tblClientes.getColumnModel().getColumn(4).setResizable(false);
            tblClientes.getColumnModel().getColumn(5).setResizable(false);
            tblClientes.getColumnModel().getColumn(6).setResizable(false);
            tblClientes.getColumnModel().getColumn(7).setResizable(false);
            tblClientes.getColumnModel().getColumn(8).setResizable(false);
            tblClientes.getColumnModel().getColumn(9).setResizable(false);
            tblClientes.getColumnModel().getColumn(10).setResizable(false);
            tblClientes.getColumnModel().getColumn(11).setResizable(false);
            tblClientes.getColumnModel().getColumn(12).setResizable(false);
            tblClientes.getColumnModel().getColumn(13).setResizable(false);
            tblClientes.getColumnModel().getColumn(14).setResizable(false);
            tblClientes.getColumnModel().getColumn(15).setResizable(false);
            tblClientes.getColumnModel().getColumn(16).setResizable(false);
            tblClientes.getColumnModel().getColumn(17).setResizable(false);
        }

        btn_Editar_Cli.setBackground(new java.awt.Color(255, 255, 255));
        btn_Editar_Cli.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btn_Editar_Cli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/editarususario.png"))); // NOI18N
        btn_Editar_Cli.setText("EDITAR CADASTRO DE CLIENTE");
        btn_Editar_Cli.setBorder(null);
        btn_Editar_Cli.setContentAreaFilled(false);
        btn_Editar_Cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Editar_CliActionPerformed(evt);
            }
        });

        btnExcluirCliente.setBackground(new java.awt.Color(255, 255, 255));
        btnExcluirCliente.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btnExcluirCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/deletarususario.png"))); // NOI18N
        btnExcluirCliente.setText("EXCLUIR CLIENTE");
        btnExcluirCliente.setBorder(null);
        btnExcluirCliente.setContentAreaFilled(false);
        btnExcluirCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirClienteActionPerformed(evt);
            }
        });

        btnExportarExcel.setBackground(new java.awt.Color(255, 255, 255));
        btnExportarExcel.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btnExportarExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exportarexcel.png"))); // NOI18N
        btnExportarExcel.setText("EXPORTAR (EXCEL)");
        btnExportarExcel.setBorder(null);
        btnExportarExcel.setContentAreaFilled(false);
        btnExportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarExcelActionPerformed(evt);
            }
        });

        lbl_TotalClientes.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        javax.swing.GroupLayout Pnl_EditarLayout = new javax.swing.GroupLayout(Pnl_Editar);
        Pnl_Editar.setLayout(Pnl_EditarLayout);
        Pnl_EditarLayout.setHorizontalGroup(
            Pnl_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_EditarLayout.createSequentialGroup()
                .addGap(137, 137, 137)
                .addComponent(btn_Editar_Cli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(104, 104, 104)
                .addComponent(btnExcluirCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(123, 123, 123)
                .addComponent(btnExportarExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(126, 126, 126))
            .addGroup(Pnl_EditarLayout.createSequentialGroup()
                .addGap(416, 416, 416)
                .addComponent(lbl_TotalClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(Pnl_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_EditarLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1)
                    .addContainerGap()))
        );
        Pnl_EditarLayout.setVerticalGroup(
            Pnl_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_EditarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_TotalClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 446, Short.MAX_VALUE)
                .addGroup(Pnl_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_Editar_Cli)
                    .addGroup(Pnl_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnExcluirCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnExportarExcel)))
                .addGap(27, 27, 27))
            .addGroup(Pnl_EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_EditarLayout.createSequentialGroup()
                    .addGap(75, 75, 75)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(93, Short.MAX_VALUE)))
        );

        rSPanelsSlider1.add(Pnl_Editar, "card4");

        Pnl_Pesquisar.setBackground(new java.awt.Color(255, 255, 255));
        Pnl_Pesquisar.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pesquisar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        Pnl_Pesquisar.setName("Pnl_Pesquisar"); // NOI18N

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));

        tblClientesPesquisa.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        tblClientesPesquisa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Processo", "Audiência", "Telefone"
            }
        ));
        tblClientesPesquisa.setGridColor(new java.awt.Color(255, 255, 255));
        tblClientesPesquisa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesPesquisaMouseClicked(evt);
            }
        });
        tblClientesPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblClientesPesquisaKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblClientesPesquisa);

        txtPesquisarCliente.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtPesquisarCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarClienteKeyReleased(evt);
            }
        });

        lbl_Pesquisar.setBackground(new java.awt.Color(255, 255, 255));
        lbl_Pesquisar.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        lbl_Pesquisar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lbl_Pesquisar.setMaximumSize(new java.awt.Dimension(197, 234));
        lbl_Pesquisar.setMinimumSize(new java.awt.Dimension(197, 234));

        Total_Cli_Up.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        javax.swing.GroupLayout Pnl_PesquisarLayout = new javax.swing.GroupLayout(Pnl_Pesquisar);
        Pnl_Pesquisar.setLayout(Pnl_PesquisarLayout);
        Pnl_PesquisarLayout.setHorizontalGroup(
            Pnl_PesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_PesquisarLayout.createSequentialGroup()
                .addContainerGap(773, Short.MAX_VALUE)
                .addComponent(Total_Cli_Up, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(242, 242, 242))
            .addGroup(Pnl_PesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_PesquisarLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(Pnl_PesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(Pnl_PesquisarLayout.createSequentialGroup()
                            .addComponent(txtPesquisarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(Pnl_PesquisarLayout.createSequentialGroup()
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 924, Short.MAX_VALUE)
                            .addGap(18, 18, 18)
                            .addComponent(lbl_Pesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap()))
        );
        Pnl_PesquisarLayout.setVerticalGroup(
            Pnl_PesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_PesquisarLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(Total_Cli_Up, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(466, Short.MAX_VALUE))
            .addGroup(Pnl_PesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_PesquisarLayout.createSequentialGroup()
                    .addGap(79, 79, 79)
                    .addComponent(txtPesquisarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addGroup(Pnl_PesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                        .addComponent(lbl_Pesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(79, 79, 79)))
        );

        rSPanelsSlider1.add(Pnl_Pesquisar, "card5");

        Pnl_Agenda.setBackground(new java.awt.Color(255, 255, 255));
        Pnl_Agenda.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agenda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        Pnl_Agenda.setName("Pnl_Agenda"); // NOI18N

        tblClientesAgenda.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        tblClientesAgenda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Audiência", "Nome", "Processo", "Telefone"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblClientesAgenda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblClientesAgendaKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tblClientesAgenda);
        if (tblClientesAgenda.getColumnModel().getColumnCount() > 0) {
            tblClientesAgenda.getColumnModel().getColumn(0).setResizable(false);
            tblClientesAgenda.getColumnModel().getColumn(0).setPreferredWidth(2);
        }

        btnImprimir.setBackground(new java.awt.Color(255, 255, 255));
        btnImprimir.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/imprimir - 1.png"))); // NOI18N
        btnImprimir.setText("IMPRIMIR");
        btnImprimir.setBorder(null);
        btnImprimir.setContentAreaFilled(false);
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        btn_Finalizar_Agenda.setBackground(new java.awt.Color(255, 255, 255));
        btn_Finalizar_Agenda.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btn_Finalizar_Agenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/editar.png"))); // NOI18N
        btn_Finalizar_Agenda.setText("FINALIZAR AGENDAMENTO");
        btn_Finalizar_Agenda.setToolTipText("");
        btn_Finalizar_Agenda.setBorder(null);
        btn_Finalizar_Agenda.setBorderPainted(false);
        btn_Finalizar_Agenda.setContentAreaFilled(false);
        btn_Finalizar_Agenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Finalizar_AgendaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Pnl_AgendaLayout = new javax.swing.GroupLayout(Pnl_Agenda);
        Pnl_Agenda.setLayout(Pnl_AgendaLayout);
        Pnl_AgendaLayout.setHorizontalGroup(
            Pnl_AgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_AgendaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pnl_AgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(Pnl_AgendaLayout.createSequentialGroup()
                        .addGap(285, 285, 285)
                        .addComponent(btn_Finalizar_Agenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(103, 103, 103)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(303, 303, 303)))
                .addContainerGap())
        );
        Pnl_AgendaLayout.setVerticalGroup(
            Pnl_AgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_AgendaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(Pnl_AgendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Finalizar_Agenda, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        rSPanelsSlider1.add(Pnl_Agenda, "card6");

        Pnl_Configuracao.setBackground(new java.awt.Color(255, 255, 255));
        Pnl_Configuracao.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configuração", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        Pnl_Configuracao.setName("Pnl_Configuracao"); // NOI18N

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnConfCor.setBackground(new java.awt.Color(255, 255, 255));
        btnConfCor.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btnConfCor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/mudarcor.png"))); // NOI18N
        btnConfCor.setText("MUDAR COR DE FUNDO");
        btnConfCor.setBorder(null);
        btnConfCor.setContentAreaFilled(false);
        btnConfCor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfCorActionPerformed(evt);
            }
        });

        btnConfCor1.setBackground(new java.awt.Color(255, 255, 255));
        btnConfCor1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btnConfCor1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/mudarcor.png"))); // NOI18N
        btnConfCor1.setText("MUDAR COR DO MENU");
        btnConfCor1.setBorder(null);
        btnConfCor1.setContentAreaFilled(false);
        btnConfCor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfCor1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Selecione a opção de mudança de cor desejada");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(238, 238, 238)
                .addComponent(btnConfCor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(192, 192, 192)
                .addComponent(btnConfCor1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(167, 167, 167))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel5)
                .addGap(48, 48, 48)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConfCor)
                    .addComponent(btnConfCor1))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jSlider1.setBackground(new java.awt.Color(255, 255, 255));
        jSlider1.setValue(100);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Selecione a opacidade desejada");
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblOpaci.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        lblOpaci.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(363, 363, 363)
                .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(382, 382, 382))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblOpaci, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel9)
                .addGap(30, 30, 30)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblOpaci, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                .addGap(33, 33, 33))
        );

        javax.swing.GroupLayout Pnl_ConfiguracaoLayout = new javax.swing.GroupLayout(Pnl_Configuracao);
        Pnl_Configuracao.setLayout(Pnl_ConfiguracaoLayout);
        Pnl_ConfiguracaoLayout.setHorizontalGroup(
            Pnl_ConfiguracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_ConfiguracaoLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(Pnl_ConfiguracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Pnl_ConfiguracaoLayout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(11, 11, 11)))
                .addGap(28, 28, 28))
        );
        Pnl_ConfiguracaoLayout.setVerticalGroup(
            Pnl_ConfiguracaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_ConfiguracaoLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );

        rSPanelsSlider1.add(Pnl_Configuracao, "card7");

        Pnl_Sobre.setBackground(new java.awt.Color(255, 255, 255));
        Pnl_Sobre.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sobre", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        Pnl_Sobre.setName("Pnl_Sobre"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("  \n  Projeto de controle de escritório\n\n  Como avaliação nas disciplinas \n\n  Banco de Dados Aplicação e Linguagem de Programação Visual (3º ADS)\n\n  Ministrada pelo professor Leandro Ferrarezi\n\n\n  Desenvolvido por Fabio e Marcos");
        jScrollPane4.setViewportView(jTextArea1);

        javax.swing.GroupLayout Pnl_SobreLayout = new javax.swing.GroupLayout(Pnl_Sobre);
        Pnl_Sobre.setLayout(Pnl_SobreLayout);
        Pnl_SobreLayout.setHorizontalGroup(
            Pnl_SobreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_SobreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1150, Short.MAX_VALUE)
                .addContainerGap())
        );
        Pnl_SobreLayout.setVerticalGroup(
            Pnl_SobreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_SobreLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(171, Short.MAX_VALUE))
        );

        rSPanelsSlider1.add(Pnl_Sobre, "card8");

        javax.swing.GroupLayout BGLayout = new javax.swing.GroupLayout(BG);
        BG.setLayout(BGLayout);
        BGLayout.setHorizontalGroup(
            BGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BGLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlBSup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BGLayout.createSequentialGroup()
                        .addGroup(BGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(rSPanelsSlider1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(bgrodape, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        BGLayout.setVerticalGroup(
            BGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BGLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlBSup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(rSPanelsSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(bgrodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Barralt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlMenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMenu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(Barralt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    int positionX = 0, positionY = 0;

    private void icon3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon3MouseEntered
        icon3.setIcon(new ImageIcon(getClass().getResource("/img/edit1.gif")));
    }//GEN-LAST:event_icon3MouseEntered

    private void icon3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon3MouseExited
        icon3.setIcon(new ImageIcon(getClass().getResource("/img/edit.png")));
    }//GEN-LAST:event_icon3MouseExited

    private void icon2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon2MouseEntered
        icon2.setIcon(new ImageIcon(getClass().getResource("/img/user1.gif")));
    }//GEN-LAST:event_icon2MouseEntered

    private void icon2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon2MouseExited
        icon2.setIcon(new ImageIcon(getClass().getResource("/img/user.png")));
    }//GEN-LAST:event_icon2MouseExited

    private void icon1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon1MouseEntered
        icon1.setIcon(new ImageIcon(getClass().getResource("/img/home1.gif")));
    }//GEN-LAST:event_icon1MouseEntered

    private void icon1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon1MouseExited
        icon1.setIcon(new ImageIcon(getClass().getResource("/img/home.png")));
    }//GEN-LAST:event_icon1MouseExited

    private void icon4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon4MouseEntered
        icon4.setIcon(new ImageIcon(getClass().getResource("/img/pesquisa1.gif")));
    }//GEN-LAST:event_icon4MouseEntered

    private void icon4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon4MouseExited
        icon4.setIcon(new ImageIcon(getClass().getResource("/img/pesquisa.png")));
    }//GEN-LAST:event_icon4MouseExited

    private void icon6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon6MouseEntered
        icon6.setIcon(new ImageIcon(getClass().getResource("/img/agenda1.gif")));
    }//GEN-LAST:event_icon6MouseEntered

    private void icon6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon6MouseExited
        icon6.setIcon(new ImageIcon(getClass().getResource("/img/agenda.png")));
    }//GEN-LAST:event_icon6MouseExited

    private void icon8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon8MouseEntered
        icon8.setIcon(new ImageIcon(getClass().getResource("/img/config1.gif")));
    }//GEN-LAST:event_icon8MouseEntered

    private void icon8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon8MouseExited
        icon8.setIcon(new ImageIcon(getClass().getResource("/img/config.png")));
    }//GEN-LAST:event_icon8MouseExited

    private void icon7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon7MouseEntered
        icon7.setIcon(new ImageIcon(getClass().getResource("/img/info1.gif")));
    }//GEN-LAST:event_icon7MouseEntered

    private void icon7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon7MouseExited
        icon7.setIcon(new ImageIcon(getClass().getResource("/img/info.png")));
    }//GEN-LAST:event_icon7MouseExited

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        setLocation(evt.getXOnScreen() - positionX, evt.getYOnScreen() - positionY);
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        positionX = evt.getX();
        positionY = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void fecharMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fecharMouseExited
        fechar.setIcon(new ImageIcon(getClass().getResource("/img/fechar.png")));
    }//GEN-LAST:event_fecharMouseExited

    private void fecharMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fecharMouseEntered
        fechar.setIcon(new ImageIcon(getClass().getResource("/img/fechar1.png")));
    }//GEN-LAST:event_fecharMouseEntered

    private void fecharMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fecharMouseClicked

        int sair = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja sair", "Atenção", JOptionPane.YES_NO_OPTION);

        if (sair == JOptionPane.YES_OPTION)
            
            
                 
        try {
            Date currentDate = GregorianCalendar.getInstance().getTime();
            DateFormat df = DateFormat.getDateInstance();
            String dateString = df.format(currentDate);

            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String timeString = sdf.format(d);

            String value0 = timeString;
            String values = dateString;

            String value = lbl_Usuario_BD.getText();
            String reg = "insert into Log (Usuário,Data,Status) values ('" + value + "','" + value0 + " / " + values + "','Desconectado')";
            pst = conn.prepareStatement(reg);
            pst.execute();
            this.dispose();
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        } finally {

            try {
                rst.close();
                pst.close();

            } catch (Exception e) {

            }
            System.exit(0);
        }


    }//GEN-LAST:event_fecharMouseClicked

    private void minimizarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizarMouseExited
        minimizar.setIcon(new ImageIcon(getClass().getResource("/img/minimizar1.png")));
    }//GEN-LAST:event_minimizarMouseExited

    private void minimizarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizarMouseEntered
        minimizar.setIcon(new ImageIcon(getClass().getResource("/img/minimizar.png")));
    }//GEN-LAST:event_minimizarMouseEntered

    private void minimizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizarMouseClicked
        this.setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_minimizarMouseClicked



    private void btn_BuscarCEPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarCEPActionPerformed
        buscarCep(txtCEP.getText());
    }//GEN-LAST:event_btn_BuscarCEPActionPerformed

    private void btnSalvarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarClienteActionPerformed

       if (txtNomeCliente.getText().isEmpty() || txtProcesso.getText().isEmpty() || txtAudiencia.getDate() == null || txtCPF.getText().isEmpty()
                || txtDtnasc.getDate() == null || txtEstcivil.getSelectedItem().toString().isEmpty() || txtEmail.getText().isEmpty()
                || txtFone1.getText().isEmpty() || txtCEP.getText().isEmpty() || txtEndereco.getText().isEmpty()
                || txtNum.getText().isEmpty() || txtBairro.getText().isEmpty()
                || txtCidade.getText().isEmpty() || txtEstado.getSelectedItem().toString().isEmpty()
                || adv_Interno.getSelectedItem().toString().isEmpty()|| person_image == null) {

            JOptionPane.showMessageDialog(this, "Favor preencher todos os campos");

        } else {

            int p = JOptionPane.showConfirmDialog(null, "Gostaria de adicionar estes dados?", "Adicionar Cliente", JOptionPane.YES_NO_OPTION);
            if (p == 0) {

                try {
                    String sql = "insert into Clientes (Nome, Processo, Audiência, CPF, Data_de_Nascimento, Estado_Civil, Email, Telefone,CEP,Endereço,Complemento,Numero,Bairro,Cidade,Estado,Responsável,Foto) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    pst = conn.prepareStatement(sql);

                    pst.setString(1, txtNomeCliente.getText());
                    pst.setString(2, txtProcesso.getText());

                    Date audiencia = txtAudiencia.getDate();
                    SimpleDateFormat sdfDT = new SimpleDateFormat("dd/MM/yyyy");
                    String dataaudiencia = sdfDT.format(txtAudiencia.getDate());

                    Date data = txtDtnasc.getDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String date = sdf.format(txtDtnasc.getDate());

                    pst.setString(3, dataaudiencia);
                    pst.setString(4, txtCPF.getText());
                    pst.setString(5, date);
                    pst.setString(6, txtEstcivil.getSelectedItem().toString());
                    pst.setString(7, txtEmail.getText());
                    pst.setString(8, txtFone1.getText());
                    pst.setString(9, txtCEP.getText());
                    pst.setString(10, txtEndereco.getText());
                    pst.setString(11, txtComp.getText());
                    pst.setString(12, txtNum.getText());
                    pst.setString(13, txtBairro.getText());
                    pst.setString(14, txtCidade.getText());
                    pst.setString(15, txtEstado.getSelectedItem().toString());
                    pst.setString(16, adv_Interno.getSelectedItem().toString());
                    pst.setBytes(17, person_image);

                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Cliente adicionado com sucesso!!");

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                Date audiencia = txtAudiencia.getDate();
                SimpleDateFormat sdfDT = new SimpleDateFormat("dd/MM/yyyy");
                String dataaudiencia = sdfDT.format(txtAudiencia.getDate());

                String value1 = dataaudiencia;
                String value2 = txtNomeCliente.getText();
                String value3 = txtProcesso.getText();
                String value4 = txtFone2.getText();

                try {

                    String reg = "insert into Agenda_Clientes (Audiência, Nome, Processo, Telefone) values ('" + value1 + "','" + value2 + "','" + value3 + "','" + value4 + "')";
                    pst = conn.prepareStatement(reg);
                    pst.execute();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
           
                Date currentDate = GregorianCalendar.getInstance().getTime();
                DateFormat df = DateFormat.getDateInstance();
                String dateString = df.format(currentDate);

                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String timeString = sdf.format(d);

                String value0 = timeString;
                String values = dateString;
                String val = lbl_Usuario_BD.getText().toString();
                try {
                    String reg = "insert into Log (Usuário,Data,Status) values ('" + val + "','" + value0 + " / " + values + "','Registro adicionado')";
                    pst = conn.prepareStatement(reg);
                    pst.execute();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                } finally {

                    try {
                        rst.close();
                        pst.close();

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                }

                JFileChooser dialog = new JFileChooser();
                dialog.setSelectedFile(new File(txtNomeCliente.getText() + " " + txtProcesso.getText() + "-Contrato" + ".pdf"));
                int dialogResult = dialog.showSaveDialog(null);
                if (dialogResult == JFileChooser.APPROVE_OPTION) {
                    String filePath = dialog.getSelectedFile().getPath();

                    try {

                        Document Contrato = new Document();
                        PdfWriter.getInstance(Contrato, new FileOutputStream(filePath));

                        Contrato.open();

                        com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance((getClass().getResource("/img/icojustice2.png")));

                        image.scaleAbsolute(80f, 80f);
                        image.setAbsolutePosition(250f, 750f);

                        Contrato.add(image);

                        com.itextpdf.text.Image image1 = com.itextpdf.text.Image.getInstance(person_image);
                        image1.setAbsolutePosition(473f, 750f);
                        image1.scaleAbsolute(80f, 70f);

                        Contrato.add(image1);

                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Contrato.add(new Paragraph("CONTRATO DE PRESTAÇÃO DE SERVIÇOS JURÍDICOS", FontFactory.getFont(FontFactory.TIMES_BOLD, 15, Font.BOLD, BaseColor.BLACK)));

                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Contrato.add(new Paragraph("Pelo presente instrumento de Contrato de Prestação de Serviços Jurídicos de uma lado:", FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, BaseColor.BLACK)));

                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Paragraph texto1 = new Paragraph(txtNomeCliente.getText() + ", brasileiro(a), " + txtEstcivil.getSelectedItem().toString() + ", portador(a) do CPF N. " + txtCPF.getText() + ", residente e domiciliado na " + txtEndereco.getText() + ", Numero: " + txtNum.getText() + ", Bairro: " + txtBairro.getText() + ", na Cidade: " + txtCidade.getText() + ", no Estado de: " + txtEstado.getSelectedItem().toString() + ", CEP: " + txtCEP.getText() + ", Complemento: " + txtComp.getText() + ".", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                        texto1.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                        Contrato.add(texto1);

                        Contrato.add(new Paragraph("Denominado CONTRATANTE.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Paragraph texto2 = new Paragraph("De outro lado, ESCRITORIO DE ADVOCACIA, CNPJ 01.001.001/0001-00, localizado no Estado Rondônia. Doravante denominado CONTRATADO, acordam o que segue.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                        texto2.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                        Contrato.add(texto2);

                        Contrato.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));

                        Paragraph texto3 = new Paragraph("CLÁUSULA PRIMEIRA - OBJETO DO CONTRATO - O presente instrumento tem como objeto a prestação de serviços advocatícios, pelo CONTRATADO, consistente na elaboração, ajuizamento e acompanhamento de ação judicial pleiteando.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                        texto3.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                        Contrato.add(texto3);

                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Paragraph texto4 = new Paragraph("PARÁGRAFO ÚNICO: ATIVIDADES: As atividades inclusas na prestação de serviço objeto deste instrumento, são todas aquelas inerentes à profissão, quais sejam: a) Praticar todos os atos inerentes ao exercício da advocacia e aqueles constantes no Estatuto da Ordem dos Advogados do Brasil, bem como os especificados no INSTRUMENTO PROCURATÓRIO.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                        texto4.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                        Contrato.add(texto4);

                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Paragraph texto5 = new Paragraph("CLÁUSULA SEGUNDA – REMUNERAÇÃO - Fica acordado entre as partes que os honorários a título de prestação de serviços, o pagamento de 20% (vinte por cento) do valor bruto da condenação ou acordo realizado (judicial ou administrativo).", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                        texto5.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                        Contrato.add(texto5);

                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Paragraph texto6 = new Paragraph("Fica eleito o foro de Porto Velho-RO para as questões judiciais ou administrativas resultantes do ajuste e a avença e constitui titulo executivo extrajudicial, nos termos do artigo N. 585, II do Código de Processo Civil. Ressalte-se que o presente contrato obriga não só as partes contratantes, mas também os seus herdeiros ou sucessores. E por assim estarem justos e contratados, o presente instrumento, em duas vias para um só efeito, na presença das testemunhas que o subscrevem.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                        texto6.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                        Contrato.add(texto6);

                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Contrato.add(new Paragraph(exibir_data.getText(), FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.PLAIN, BaseColor.BLACK)));
                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Paragraph texto7 = new Paragraph("____________________________", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                        texto7.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
                        Contrato.add(texto7);
                        Contrato.add(new Paragraph("CONTRATADO.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));

                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Paragraph texto8 = new Paragraph("____________________________", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                        texto8.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
                        Contrato.add(texto8);
                        Contrato.add(new Paragraph("CONTRATANTE.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));

                        Contrato.close();

                        JOptionPane.showMessageDialog(null, "Contrato gerado com sucesso");

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    } finally {

                        try {
                            rst.close();
                            pst.close();

                        } catch (Exception e) {

                        }
                    }

                }

            }

            txtNomeCliente.setText(null);
            txtCPF.setText(null);
            txtDtnasc.setDate(null);
            txtEstcivil.setSelectedItem(null);
            txtEmail.setText(null);
            txtFone1.setText(null);
            txtCEP.setText(null);
            txtEndereco.setText(null);
            txtComp.setText(null);
            txtNum.setText(null);
            txtBairro.setText(null);
            txtCidade.setText(null);
            txtEstado.setSelectedItem(null);
            lblStatus.setIcon(null);
            lbl_img.setIcon(null);

        }

        Update_Clientes();
        Update_Pesquisa();
        Update_Agenda();
        numClientes();
        get_Total();
    }//GEN-LAST:event_btnSalvarClienteActionPerformed

    private void btnLimparClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparClientActionPerformed

        txtNomeCliente.setText("");
        txtCPF.setText("");
        txtDtnasc.setDate(null);
        txtEstcivil.setSelectedItem(null);
        txtEmail.setText("");
        txtFone1.setText("");
        txtCEP.setText("");
        txtEndereco.setText("");
        txtComp.setText("");
        txtNum.setText("");
        txtBairro.setText("");
        txtCidade.setText("");
        txtEstado.setSelectedItem(null);
        lblStatus.setIcon(null);
        lbl_img.setIcon(null);
        

    }//GEN-LAST:event_btnLimparClientActionPerformed


    private void btn_img1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_img1ActionPerformed

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("4 extensões suportadas", "jpg", "jpeg", "gif", "png");
        chooser.setFileFilter(filter);
        int result = chooser.showSaveDialog(chooser);
        if (result == JFileChooser.APPROVE_OPTION) {

            File f = chooser.getSelectedFile();

            lbl_img1.setIcon(new ImageIcon(f.toString()));

            filename = f.getAbsolutePath();

            ImageIcon imageIcon = new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(lbl_img1.getWidth(), lbl_img1.getHeight(), Image.SCALE_SMOOTH));

            lbl_img1.setIcon(imageIcon);

            try {
                File image = new File(filename);
                FileInputStream fis = new FileInputStream(image);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum);

                }

                person_image = bos.toByteArray();

            } catch (Exception e) {

            }
        } else if (result == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(this, "Nenhum arquivo selecionado");
        }

    }//GEN-LAST:event_btn_img1ActionPerformed

    private void btn_Buscar_UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Buscar_UpdateActionPerformed

        buscarCep(txtCEP1.getText());

    }//GEN-LAST:event_btn_Buscar_UpdateActionPerformed

    private void btnUpdateClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateClienteActionPerformed

        if (txtNomeCliente1.getText().isEmpty() || txtProcesso1.getText().isEmpty() || txtAudiencia1.getDate() == null || txtCPF1.getText().isEmpty()
                || txtDtnasc1.getDate() == null || txtEstcivil1.getSelectedItem().toString().isEmpty() || txtEmail1.getText().isEmpty()
                || txtFone2.getText().isEmpty() || txtCEP1.getText().isEmpty() || txtEndereco1.getText().isEmpty()
                || txtComp1.getText().isEmpty() || txtNum1.getText().isEmpty() || txtBairro1.getText().isEmpty()
                || txtCidade1.getText().isEmpty() || txtEstado1.getSelectedItem().toString().isEmpty()
                || adv_Interno1.getSelectedItem().toString().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Favor preencher todos os campos");

        } else {

            Date audUp = txtAudiencia1.getDate();
            SimpleDateFormat sdfT = new SimpleDateFormat("dd/MM/yyyy");
            String dataaudUp = sdfT.format(txtAudiencia1.getDate());

            Date dtNasc = txtDtnasc1.getDate();
            SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
            String dataNasc = sdfD.format(txtDtnasc1.getDate());

            String value1 = txtNomeCliente1.getText();
            String value2 = txtProcesso1.getText();
            String value3 = dataaudUp;
            String value4 = txtCPF1.getText();
            String value5 = dataNasc;
            String value6 = txtEstcivil1.getSelectedItem().toString();
            String value7 = txtEmail1.getText();
            String value8 = txtFone2.getText();
            String value9 = txtCEP1.getText();
            String value10 = txtEndereco1.getText();
            String value11 = txtComp1.getText();
            String value12 = txtNum1.getText();
            String value13 = txtBairro1.getText();
            String value14 = txtCidade1.getText();
            String value15 = txtEstado1.getSelectedItem().toString();
            String value16 = adv_Interno1.getSelectedItem().toString();
            String value17 = txtIDClientes.getText();

            int p = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja atualizar o cadastro?", "Atualizar dados", JOptionPane.YES_NO_OPTION);
            if (p == 0) {

                try {

                    String sql = "update Clientes set Nome='" + value1 + "',Processo='" + value2 + "', Audiência='" + value3 + "', CPF='" + value4 + "',"
                            + "Data_de_Nascimento='" + value5 + "',Estado_Civil='" + value6 + "',Email='" + value7 + "',Telefone= '" + value8 + "', "
                            + "CEP='" + value9 + "',Endereço='" + value10 + "',Complemento='" + value11 + "', Numero='" + value12 + "', Bairro='" + value13 + "',"
                            + " Cidade='" + value14 + "', Estado='" + value15 + "', Responsável='" + value16 + "', ID='" + value17 + "' where id='" + value17 + "' ";

                    pst = conn.prepareStatement(sql);

                    pst.execute();

                    JOptionPane.showMessageDialog(null, "Cliente atualizado com sucesso!!");

                } catch (Exception e) {

                }

                try {

                    File file = new File(filename);
                    FileInputStream fis = new FileInputStream(file);
                    byte[] image = new byte[(int) file.length()];
                    fis.read(image);
                    String sql = "update Clientes SET Foto = ? where ID = '" + value17 + "'";
                    pst = conn.prepareStatement(sql);
                    pst.setBytes(1, image);
                    pst.executeUpdate();
                    pst.close();;

                } catch (Exception ex) {

                }

                try {

                    String reg = "UPDATE Agenda_Clientes SET Audiência='" + value3 + "', Nome='" + value1 + "', Processo='" + value2 + "', Telefone='" + value8 + "' where id='" + value1 + "' ";
                    pst = conn.prepareStatement(reg);
                    pst.execute();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                Date currentDate = GregorianCalendar.getInstance().getTime();
                DateFormat df = DateFormat.getDateInstance();
                String dateString = df.format(currentDate);

                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String timeString = sdf.format(d);

                String value0 = timeString;
                String values = dateString;
                String val = lbl_Usuario_BD.getText().toString();
                try {
                    String reg = "insert into Log (Usuário,Data,Status) values ('" + val + "','" + value0 + " / " + values + "','Registro atualizado')";
                    pst = conn.prepareStatement(reg);
                    pst.execute();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                } finally {

                    try {
                        rst.close();
                        pst.close();

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }

                    JFileChooser dialog = new JFileChooser();
                    dialog.setSelectedFile(new File(txtNomeCliente1.getText() + " " + txtProcesso1.getText() + "-Contrato" + ".pdf"));
                    int dialogResult = dialog.showSaveDialog(null);
                    if (dialogResult == JFileChooser.APPROVE_OPTION) {
                        String filePath = dialog.getSelectedFile().getPath();

                        try {

                            Document Contrato = new Document();
                            PdfWriter.getInstance(Contrato, new FileOutputStream(filePath));

                            Contrato.open();

                            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance((getClass().getResource("/img/icojustice2.png")));

                            image.scaleAbsolute(80f, 80f);
                            image.setAbsolutePosition(250f, 750f);

                            Contrato.add(image);
            
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Contrato.add(new Paragraph("CONTRATO DE PRESTAÇÃO DE SERVIÇOS JURÍDICOS", FontFactory.getFont(FontFactory.TIMES_BOLD, 15, Font.BOLD, BaseColor.BLACK)));

                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Contrato.add(new Paragraph("Pelo presente instrumento de Contrato de Prestação de Serviços Jurídicos de uma lado:", FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, BaseColor.BLACK)));

                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Paragraph texto1 = new Paragraph(txtNomeCliente1.getText() + ", brasileiro(a), " + txtEstcivil1.getSelectedItem().toString() + ", portador(a) do CPF N. " + txtCPF1.getText() + ", residente e domiciliado na " + txtEndereco1.getText() + ", Numero: " + txtNum1.getText() + ", Bairro: " + txtBairro1.getText() + ", na Cidade: " + txtCidade1.getText() + ", no Estado de: " + txtEstado1.getSelectedItem().toString() + ", CEP: " + txtCEP1.getText() + ", Complemento: " + txtComp1.getText() + ".", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto1.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                            Contrato.add(texto1);

                            Contrato.add(new Paragraph("Denominado CONTRATANTE.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Paragraph texto2 = new Paragraph("De outro lado, ESCRITORIO DE ADVOCACIA, CNPJ 01.001.001/0001-00, localizado no Estado Rondônia. Doravante denominado CONTRATADO, acordam o que segue.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto2.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                            Contrato.add(texto2);

                            Contrato.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));

                            Paragraph texto3 = new Paragraph("CLÁUSULA PRIMEIRA - OBJETO DO CONTRATO - O presente instrumento tem como objeto a prestação de serviços advocatícios, pelo CONTRATADO, consistente na elaboração, ajuizamento e acompanhamento de ação judicial pleiteando.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto3.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                            Contrato.add(texto3);

                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Paragraph texto4 = new Paragraph("PARÁGRAFO ÚNICO: ATIVIDADES: As atividades inclusas na prestação de serviço objeto deste instrumento, são todas aquelas inerentes à profissão, quais sejam: a) Praticar todos os atos inerentes ao exercício da advocacia e aqueles constantes no Estatuto da Ordem dos Advogados do Brasil, bem como os especificados no INSTRUMENTO PROCURATÓRIO.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto4.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                            Contrato.add(texto4);

                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Paragraph texto5 = new Paragraph("CLÁUSULA SEGUNDA – REMUNERAÇÃO - Fica acordado entre as partes que os honorários a título de prestação de serviços, o pagamento de 20% (vinte por cento) do valor bruto da condenação ou acordo realizado (judicial ou administrativo).", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto5.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                            Contrato.add(texto5);

                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Paragraph texto6 = new Paragraph("Fica eleito o foro de Porto Velho-RO para as questões judiciais ou administrativas resultantes do ajuste e a avença e constitui titulo executivo extrajudicial, nos termos do artigo N. 585, II do Código de Processo Civil. Ressalte-se que o presente contrato obriga não só as partes contratantes, mas também os seus herdeiros ou sucessores. E por assim estarem justos e contratados, o presente instrumento, em duas vias para um só efeito, na presença das testemunhas que o subscrevem.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto6.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                            Contrato.add(texto6);

                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Contrato.add(new Paragraph(exibir_data.getText(), FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.PLAIN, BaseColor.BLACK)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Paragraph texto7 = new Paragraph("____________________________", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto7.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
                            Contrato.add(texto7);
                            Contrato.add(new Paragraph("CONTRATADO.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));

                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Paragraph texto8 = new Paragraph("____________________________", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto8.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
                            Contrato.add(texto8);
                            Contrato.add(new Paragraph("CONTRATANTE.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));

                            Contrato.close();

                            JOptionPane.showMessageDialog(null, "Contrato atualizado com sucesso");

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e);
                        } finally {

                            try {
                                rst.close();
                                pst.close();

                            } catch (Exception e) {

                            }

                        }

                    }

                    txtNomeCliente1.setText("");
                    txtProcesso1.setText("");
                    txtAudiencia1.setDate(null);
                    txtCPF1.setText("");
                    txtDtnasc1.setDate(null);
                    txtEstcivil1.setSelectedItem(null);
                    txtEmail1.setText("");
                    txtFone2.setText("");
                    txtCEP1.setText("");
                    txtEndereco1.setText("");
                    txtComp1.setText("");
                    txtNum1.setText("");
                    txtBairro1.setText("");
                    txtCidade1.setText("");
                    txtEstado1.setSelectedItem(null);
                    lblStatus1.setIcon(null);
                    lbl_img1.setIcon(null);
                    adv_Interno1.setSelectedItem(null);

                }
            }
    }//GEN-LAST:event_btnUpdateClienteActionPerformed
        Update_Clientes();
        Update_Pesquisa();
        Update_Agenda();
        numClientes();
        get_Total();
    }

    private void btnLimparCli_UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparCli_UpdateActionPerformed

        txtNomeCliente1.setText("");
        txtProcesso1.setText("");
        txtAudiencia1.setDate(null);
        txtCPF1.setText("");
        txtDtnasc1.setDate(null);
        txtEstcivil1.setSelectedItem(null);
        txtEmail1.setText("");
        txtFone2.setText("");
        txtCEP1.setText("");
        txtEndereco1.setText("");
        txtComp1.setText("");
        txtNum1.setText("");
        txtBairro1.setText("");
        txtCidade1.setText("");
        txtEstado1.setSelectedItem(null);
        lblStatus1.setIcon(null);
        lbl_img1.setIcon(null);
        adv_Interno1.setSelectedItem(null);
    }//GEN-LAST:event_btnLimparCli_UpdateActionPerformed


    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked


    }//GEN-LAST:event_tblClientesMouseClicked

    private void numClientes() {

        try {

            lbl_TotalClientes.setText("Total de Clientes = " + tblClientes.getRowCount() + " ");

            Total_Cli_Up.setText("Total de Clientes = " + tblClientes.getRowCount() + " ");

            Home_adv.txtTotalCliBD.setText("Total de Clientes = " + Integer.toString(Contador.countDB("Clientes")));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void btn_Editar_CliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Editar_CliActionPerformed

        try {

            int finaliza = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja editar o cadastro?", "Atenção", JOptionPane.YES_NO_OPTION);

            if (finaliza == JOptionPane.YES_OPTION) {

                if (!this.btn_Editar_Cli.isSelected()) {
                    this.btn_novo.setSelected(true);
                    this.btn_editar.setSelected(false);
                    getEdit();
                    rSPanelsSlider1.slidPanel(20, Pnl_Atualizar, RSPanelsSlider.direct.Right);

                } else if (finaliza == JOptionPane.NO_OPTION) {

                    this.setVisible(false);
                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }//GEN-LAST:event_btn_Editar_CliActionPerformed


    private void btnExcluirClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirClienteActionPerformed

        int p = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o Cliente?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (p == 0) {

            try {

                int del = tblClientes.getSelectedRow();
                String cel = (tblClientes.getModel().getValueAt(del, 0).toString());

                String sql = "DELETE from Clientes where ID=" + cel;

                pst = conn.prepareStatement(sql);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Cliente Excluído com sucesso");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            Date currentDate = GregorianCalendar.getInstance().getTime();
            DateFormat df = DateFormat.getDateInstance();
            String dateString = df.format(currentDate);

            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String timeString = sdf.format(d);

            String value0 = timeString;
            String values = dateString;
            String val = lbl_Usuario_BD.getText().toString();
            try {
                String reg = "insert into Log (Usuário,Data,Status) values ('" + val + "','" + value0 + " / " + values + "','Registro apagado')";
                pst = conn.prepareStatement(reg);
                pst.execute();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            } finally {

                try {
                    rst.close();
                    pst.close();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }
        }
        Update_Clientes();
        Update_Pesquisa();
        Update_Agenda();
        numClientes();
    }//GEN-LAST:event_btnExcluirClienteActionPerformed

    private void tblClientesPesquisaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesPesquisaMouseClicked

        getImagePesquisa();
    }//GEN-LAST:event_tblClientesPesquisaMouseClicked

    private void tblClientesPesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblClientesPesquisaKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            getImagePesquisa();
        }
    }//GEN-LAST:event_tblClientesPesquisaKeyReleased


    private void txtPesquisarClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarClienteKeyReleased

        pesquisar_Cliente();

    }//GEN-LAST:event_txtPesquisarClienteKeyReleased

    private void btn_Finalizar_AgendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Finalizar_AgendaActionPerformed

        int p = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja finalizar o agendamento?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (p == 0) {

       
            try {

                int del = tblClientesAgenda.getSelectedRow();
                String cel = (tblClientesAgenda.getModel().getValueAt(del, 0).toString());

                String sql = "DELETE from Agenda_Clientes where ID=" + cel;

                pst = conn.prepareStatement(sql);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Agendamento finalizado com sucesso");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

            Date currentDate = GregorianCalendar.getInstance().getTime();
            DateFormat df = DateFormat.getDateInstance();
            String dateString = df.format(currentDate);

            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String timeString = sdf.format(d);

            String value0 = timeString;
            String values = dateString;
            String val = lbl_Usuario_BD.getText().toString();
            try {
                String reg = "insert into Log (Usuário,Data,Status) values ('" + val + "','" + value0 + " / " + values + "','Agendamento Finalizado')";
                pst = conn.prepareStatement(reg);
                pst.execute();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            } finally {

                try {
                    rst.close();
                    pst.close();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }
            }

        }
        Update_Agenda();
        Update_Clientes();
        Update_Pesquisa();
        
        numClientes();
    }//GEN-LAST:event_btn_Finalizar_AgendaActionPerformed


    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        try {
            Date currentDate = GregorianCalendar.getInstance().getTime();
            DateFormat df = DateFormat.getDateInstance();
            String dateString = df.format(currentDate);

            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String timeString = sdf.format(d);

            String value0 = timeString;
            String values = dateString;

            String value = lbl_Usuario_BD.getText();

            String reg = "insert into Log (Usuário,Data,Status) values ('" + value + "','" + value0 + " / " + values + "','Arquivo impresso')";
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

        try {

            boolean imprimir = tblClientesAgenda.print();
            if (!imprimir) {
                JOptionPane.showMessageDialog(null, "Não foi possível imprimir");
            }

        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

    }//GEN-LAST:event_btnImprimirActionPerformed

    private void btnConfCorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfCorActionPerformed

        JColorChooser obj = new JColorChooser();
        Object a = obj;
        int b = JOptionPane.showConfirmDialog(null, a, "Escolha a cor de fundo", JOptionPane.OK_CANCEL_OPTION);
        if (b == 0) {
            getContentPane().setBackground(obj.getColor());

        }

    }//GEN-LAST:event_btnConfCorActionPerformed

    private void btnConfCor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfCor1ActionPerformed

        JColorChooser obj = new JColorChooser();
        Object a = obj;
        int b = JOptionPane.showConfirmDialog(null, a, "Escolha a cor de fundo", JOptionPane.OK_CANCEL_OPTION);
        if (b == 0) {

            Barramenu.setBackground(obj.getColor());
            bgrodape.setBackground(obj.getColor());
            pnlBSup.setBackground(obj.getColor());
            btn_home.setBackground(obj.getColor());
            home.setBackground(obj.getColor());
            btn_novo.setBackground(obj.getColor());
            novo.setBackground(obj.getColor());
            btn_editar.setBackground(obj.getColor());
            editar.setBackground(obj.getColor());
            btn_pesquisar.setBackground(obj.getColor());
            pesquisar.setBackground(obj.getColor());
            btn_agenda.setBackground(obj.getColor());
            agenda.setBackground(obj.getColor());
            btn_configuracao.setBackground(obj.getColor());
            configuracao.setBackground(obj.getColor());
            btn_sobre.setBackground(obj.getColor());
            sobre.setBackground(obj.getColor());
            jPanel2.setBackground(obj.getColor());
            pnlMenu.setBackground(obj.getColor());
            btnLogof.setBackground(obj.getColor());

        }
    }//GEN-LAST:event_btnConfCor1ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged

        int sliderV = jSlider1.getValue();
        float opacity = (float) sliderV / 100;
        this.setOpacity(opacity);
        lblOpaci.setText(String.valueOf(sliderV) + "% Opacidade");
    }//GEN-LAST:event_jSlider1StateChanged

    private void btn_homeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_homeActionPerformed
        if (!this.btn_home.isSelected()) {
            this.btn_home.setSelected(true);
            this.btn_novo.setSelected(false);
            this.btn_editar.setSelected(false);
            this.btn_pesquisar.setSelected(false);
            this.btn_agenda.setSelected(false);
            this.btn_configuracao.setSelected(false);
            this.btn_sobre.setSelected(false);

            rSPanelsSlider1.slidPanel(20, Pnl_Home, RSPanelsSlider.direct.Right);
        }
    }//GEN-LAST:event_btn_homeActionPerformed

    private void btn_novoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_novoActionPerformed
        if (!this.btn_novo.isSelected()) {
            this.btn_home.setSelected(false);
            this.btn_novo.setSelected(true);
            this.btn_editar.setSelected(false);
            this.btn_pesquisar.setSelected(false);
            this.btn_agenda.setSelected(false);
            this.btn_configuracao.setSelected(false);
            this.btn_sobre.setSelected(false);

            rSPanelsSlider1.slidPanel(20, Pnl_Novo, RSPanelsSlider.direct.Right);
        }
    }//GEN-LAST:event_btn_novoActionPerformed

    private void btn_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarActionPerformed

        if (!this.btn_editar.isSelected()) {
            this.btn_home.setSelected(false);
            this.btn_novo.setSelected(false);
            this.btn_editar.setSelected(true);
            this.btn_pesquisar.setSelected(false);
            this.btn_agenda.setSelected(false);
            this.btn_configuracao.setSelected(false);
            this.btn_sobre.setSelected(false);

            rSPanelsSlider1.slidPanel(20, Pnl_Editar, RSPanelsSlider.direct.Right);
        }
    }//GEN-LAST:event_btn_editarActionPerformed

    private void btn_pesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pesquisarActionPerformed
        if (!this.btn_pesquisar.isSelected()) {
            this.btn_home.setSelected(false);
            this.btn_novo.setSelected(false);
            this.btn_editar.setSelected(false);
            this.btn_pesquisar.setSelected(true);
            this.btn_agenda.setSelected(false);
            this.btn_configuracao.setSelected(false);
            this.btn_sobre.setSelected(false);

            rSPanelsSlider1.slidPanel(20, Pnl_Pesquisar, RSPanelsSlider.direct.Right);
        }
    }//GEN-LAST:event_btn_pesquisarActionPerformed

    private void btn_agendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agendaActionPerformed
        if (!this.btn_agenda.isSelected()) {
            this.btn_home.setSelected(false);
            this.btn_novo.setSelected(false);
            this.btn_editar.setSelected(false);
            this.btn_pesquisar.setSelected(false);
            this.btn_agenda.setSelected(true);
            this.btn_configuracao.setSelected(false);
            this.btn_sobre.setSelected(false);

            rSPanelsSlider1.slidPanel(20, Pnl_Agenda, RSPanelsSlider.direct.Right);
        }
    }//GEN-LAST:event_btn_agendaActionPerformed

    private void btn_configuracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_configuracaoActionPerformed
        if (!this.btn_configuracao.isSelected()) {
            this.btn_home.setSelected(false);
            this.btn_novo.setSelected(false);
            this.btn_editar.setSelected(false);
            this.btn_pesquisar.setSelected(false);
            this.btn_agenda.setSelected(false);
            this.btn_configuracao.setSelected(true);
            this.btn_sobre.setSelected(false);

            rSPanelsSlider1.slidPanel(20, Pnl_Configuracao, RSPanelsSlider.direct.Right);
        }
    }//GEN-LAST:event_btn_configuracaoActionPerformed

    private void btn_sobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sobreActionPerformed
        if (!this.btn_sobre.isSelected()) {
            this.btn_home.setSelected(false);
            this.btn_novo.setSelected(false);
            this.btn_editar.setSelected(false);
            this.btn_pesquisar.setSelected(false);
            this.btn_agenda.setSelected(false);
            this.btn_configuracao.setSelected(false);
            this.btn_sobre.setSelected(true);

            rSPanelsSlider1.slidPanel(20, Pnl_Sobre, RSPanelsSlider.direct.Right);
        }
    }//GEN-LAST:event_btn_sobreActionPerformed


    private void btnExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcelActionPerformed

        int finaliza = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja exportar o registro?", "Atenção", JOptionPane.YES_NO_OPTION);

        if (finaliza == JOptionPane.YES_OPTION) {

            FileOutputStream excelFOU = null;
            BufferedOutputStream excelBOU = null;
            XSSFWorkbook excelTabela = null;

            JFileChooser excelFile = new JFileChooser();
            excelFile.setDialogTitle("Salvar como");
            FileNameExtensionFilter fnef = new FileNameExtensionFilter("Excel Files", "xls", "xlsx", "xlsm");
            excelFile.setFileFilter(fnef);
            int excelPick = excelFile.showSaveDialog(null);

            if (excelPick == JFileChooser.APPROVE_OPTION);

            try {

                excelTabela = new XSSFWorkbook();
                XSSFSheet excelTela = excelTabela.createSheet("Jtable");

                for (int i = 0; i < tblClientes.getRowCount(); i++) {

                    XSSFRow excelLinha = excelTela.createRow(i);
                    for (int j = 0; j < tblClientes.getColumnCount(); j++) {
                        XSSFCell excelCelula = excelLinha.createCell(j);

                        excelCelula.setCellValue(tblClientes.getValueAt(i, j).toString());

                    }

                }

                excelFOU = new FileOutputStream(excelFile.getSelectedFile() + ".xlsx");
                excelBOU = new BufferedOutputStream(excelFOU);
                excelTabela.write(excelBOU);

                JOptionPane.showMessageDialog(null, "Arquivo exportador com sucesso");

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();

            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {

                try {
                    if (excelBOU != null) {
                        excelBOU.close();
                    }
                    if (excelFOU != null) {
                        excelFOU.close();
                    }
                    if (excelTabela != null) {
                        //excelTabela.close();
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            Date currentDate = GregorianCalendar.getInstance().getTime();
            DateFormat df = DateFormat.getDateInstance();
            String dateString = df.format(currentDate);

            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String timeString = sdf.format(d);

            String value0 = timeString;
            String values = dateString;
            String val = lbl_Usuario_BD.getText().toString();
            try {
                String reg = "insert into Log (Usuário,Data,Status) values ('" + val + "','" + value0 + " / " + values + "','Registro exportado')";
                pst = conn.prepareStatement(reg);
                pst.execute();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            } finally {

                try {
                    rst.close();
                    pst.close();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }
            }
        }
    }//GEN-LAST:event_btnExportarExcelActionPerformed

    private void txtCEPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCEPKeyReleased

        String cp = txtCEP.getText();

        cp = cp.replaceAll("\\D*", "");
        int cont = cp.length();

        txtEndereco.setText("Aguarde...");

        if (cont == 8) {
            buscarCep(txtCEP.getText());
        }

    }//GEN-LAST:event_txtCEPKeyReleased

    private void btnLogofActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogofActionPerformed
        this.setVisible(false);
        new login().setVisible(true);

        try {
            Date currentDate = GregorianCalendar.getInstance().getTime();
            DateFormat df = DateFormat.getDateInstance();
            String dateString = df.format(currentDate);

            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String timeString = sdf.format(d);

            String value0 = timeString;
            String values = dateString;

            String value = lbl_Usuario_BD.getText();
            String reg = "insert into Log (Usuário,Data,Status) values ('" + value + "','" + value0 + " / " + values + "','Desconectado')";
            pst = conn.prepareStatement(reg);
            pst.execute();
            this.dispose();
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        } finally {

            try {
                rst.close();
                pst.close();

            } catch (Exception e) {

            }
        }


    }//GEN-LAST:event_btnLogofActionPerformed

    private void tblClientesAgendaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblClientesAgendaKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {

        }
    }//GEN-LAST:event_tblClientesAgendaKeyReleased

    private void tblClientesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblClientesKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {

        }
    }//GEN-LAST:event_tblClientesKeyReleased

    private void maximizarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maximizarMouseExited
        maximizar.setIcon(new ImageIcon(getClass().getResource("/img/maximizar1.png")));
    }//GEN-LAST:event_maximizarMouseExited

    private void maximizarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maximizarMouseEntered
        maximizar.setIcon(new ImageIcon(getClass().getResource("/img/maximizar.png")));
    }//GEN-LAST:event_maximizarMouseEntered

    private void maximizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maximizarMouseClicked
        if (this.getExtendedState() != Home_adv.MAXIMIZED_BOTH) {
            this.setExtendedState(Home_adv.MAXIMIZED_BOTH);

        } else {
            this.setExtendedState(Home_adv.NORMAL);
        }
    }//GEN-LAST:event_maximizarMouseClicked

    private void btn_imgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imgActionPerformed

        JFileChooser chooser = new JFileChooser();

        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("4 extensões suportadas", "jpg", "jpeg", "gif", "png");
        chooser.setFileFilter(filter);
        int result = chooser.showSaveDialog(chooser);
        if (result == JFileChooser.APPROVE_OPTION) {

            File f = chooser.getSelectedFile();

            lbl_img.setIcon(new ImageIcon(f.toString()));

            filename = f.getAbsolutePath();

            ImageIcon imageIcon = new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(lbl_img.getWidth(), lbl_img.getHeight(), Image.SCALE_SMOOTH));

            lbl_img.setIcon(imageIcon);

            try {
                File image = new File(filename);

                FileInputStream fis = new FileInputStream(image);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum);

                }

                person_image = bos.toByteArray();

            } catch (Exception e) {

            }
        } else if (result == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(this, "Nenhum arquivo selecionado");
        }

    }//GEN-LAST:event_btn_imgActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home_adv().setVisible(true);
            }
        });
    }

    public void buscarCep(String cep) {

        String json;

        try {
            URL url = new URL("http://viacep.com.br/ws/" + cep + "/json");
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder jsonSb = new StringBuilder();

            br.lines().forEach(l -> jsonSb.append(l.trim()));
            json = jsonSb.toString();

            json = json.replaceAll("[{},:]", "");
            json = json.replaceAll("\"", "\n");
            String array[] = new String[30];
            array = json.split("\n");

            logradouro = array[7];
            Bairro = array[15];
            cidade = array[19];
            uf = array[23];

            txtEndereco.setText(logradouro);
            txtBairro.setText(Bairro);
            txtCidade.setText(cidade);
            txtEstado.setSelectedItem(uf);

            lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/verdadeiro.png")));

            txtEndereco1.setText(logradouro);
            txtBairro1.setText(Bairro);
            txtCidade1.setText(cidade);
            txtEstado1.setSelectedItem(uf);

            lblStatus1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/verdadeiro.png")));

            if (json.equals("{\"erro\": true}")) {

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "CEP não localizado!\n"
                    + "Verifique se digitou o CEP corretamente", "Mensagem", JOptionPane.INFORMATION_MESSAGE);

            lblStatus.setIcon(null);
            txtCEP.setText(null);
            txtEndereco.setText(null);
            txtComp.setText(null);
            txtNum.setText(null);
            txtBairro.setText(null);
            txtCidade.setText(null);
            txtEstado.setSelectedItem(null);

            return;

        }
    }

    private Image scaledImage(byte[] img, int w, int h) {

        BufferedImage resizedImgae = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        try {
            Graphics2D g2 = resizedImgae.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            ByteArrayInputStream in = new ByteArrayInputStream(img);
            BufferedImage ImageConvert = ImageIO.read(in);
            g2.drawImage(ImageConvert, 0, 0, w, h, null);
            g2.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane, ex);
        }

        return resizedImgae;

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BG;
    private javax.swing.JPanel Barralt;
    private javax.swing.JPanel Barramenu;
    private javax.swing.JLabel CPF;
    private javax.swing.JLabel CPF2;
    private javax.swing.JLabel DtNasc;
    private javax.swing.JLabel DtNasc2;
    private javax.swing.JLabel Estado_civil;
    private javax.swing.JLabel Estado_civil2;
    private javax.swing.JLabel Nome;
    private javax.swing.JLabel Nome2;
    private javax.swing.JLabel Num;
    private javax.swing.JLabel Num1;
    private javax.swing.JPanel Pnl_Agenda;
    private javax.swing.JPanel Pnl_Atualizar;
    private javax.swing.JPanel Pnl_Configuracao;
    private javax.swing.JPanel Pnl_Editar;
    private javax.swing.JPanel Pnl_Home;
    private javax.swing.JPanel Pnl_Novo;
    private javax.swing.JPanel Pnl_Pesquisar;
    private javax.swing.JPanel Pnl_Sobre;
    private javax.swing.JLabel Tel1;
    private javax.swing.JLabel Tel3;
    private javax.swing.JLabel Total_Cli_Up;
    private javax.swing.JComboBox<String> adv_Interno;
    private javax.swing.JComboBox<String> adv_Interno1;
    private javax.swing.JPanel agenda;
    private javax.swing.JLabel bairro;
    private javax.swing.JLabel bairro1;
    private javax.swing.JLabel balanca;
    private javax.swing.JPanel bgrodape;
    private javax.swing.JButton btnConfCor;
    private javax.swing.JButton btnConfCor1;
    private javax.swing.JButton btnExcluirCliente;
    private javax.swing.JButton btnExportarExcel;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnLimparCli_Update;
    private javax.swing.JButton btnLimparClient;
    private javax.swing.JButton btnLogof;
    private javax.swing.JButton btnSalvarCliente;
    private javax.swing.JButton btnUpdateCliente;
    private javax.swing.JButton btn_BuscarCEP;
    private javax.swing.JButton btn_Buscar_Update;
    private javax.swing.JButton btn_Editar_Cli;
    private javax.swing.JButton btn_Finalizar_Agenda;
    private javax.swing.JButton btn_agenda;
    private javax.swing.JButton btn_configuracao;
    private javax.swing.JButton btn_editar;
    private javax.swing.JButton btn_home;
    private javax.swing.JButton btn_img;
    private javax.swing.JButton btn_img1;
    private javax.swing.JButton btn_novo;
    private javax.swing.JButton btn_pesquisar;
    private javax.swing.JButton btn_sobre;
    private javax.swing.JLabel cep;
    private javax.swing.JLabel cep1;
    private javax.swing.JLabel cid;
    private javax.swing.JLabel cid1;
    private javax.swing.JLabel comp;
    private javax.swing.JLabel comp1;
    private javax.swing.JPanel configuracao;
    private javax.swing.JPanel editar;
    private javax.swing.JLabel email;
    private javax.swing.JLabel email2;
    private javax.swing.JLabel end;
    private javax.swing.JLabel end1;
    private javax.swing.JLabel est;
    private javax.swing.JLabel est1;
    private javax.swing.JLabel exibir_data;
    private javax.swing.JLabel exibir_hora;
    private javax.swing.JLabel fechar;
    private javax.swing.JPanel home;
    private javax.swing.JLabel icon1;
    private javax.swing.JLabel icon2;
    private javax.swing.JLabel icon3;
    private javax.swing.JLabel icon4;
    private javax.swing.JLabel icon6;
    private javax.swing.JLabel icon7;
    private javax.swing.JLabel icon8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel labelTit;
    private javax.swing.JLabel labeltext1;
    private javax.swing.JLabel labeltext2;
    private javax.swing.JLabel lblOpaci;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel lbl_Clientes;
    private javax.swing.JLabel lbl_Pesquisar;
    public static javax.swing.JLabel lbl_TotalClientes;
    public static javax.swing.JLabel lbl_Usuario_BD;
    private javax.swing.JLabel lbl_advogado;
    private javax.swing.JLabel lbl_img;
    private javax.swing.JLabel lbl_img1;
    private javax.swing.JLabel maximizar;
    private javax.swing.JLabel minimizar;
    private javax.swing.JLabel msg;
    private javax.swing.JPanel novo;
    private javax.swing.JPanel pesquisar;
    private javax.swing.JPanel pnlBSup;
    private javax.swing.JPanel pnlCad1;
    private javax.swing.JPanel pnlCad2;
    private javax.swing.JPanel pnlCad4;
    private javax.swing.JPanel pnlCad5;
    private javax.swing.JPanel pnlMenu;
    private javax.swing.JPanel pnlMenu1;
    private javax.swing.JLabel proces;
    private javax.swing.JLabel proces2;
    private rojerusan.RSPanelsSlider rSPanelsSlider1;
    private javax.swing.JPanel sobre;
    private javax.swing.JTable tblClientes;
    private static javax.swing.JTable tblClientesAgenda;
    private javax.swing.JTable tblClientesPesquisa;
    private javax.swing.JLabel titulo1;
    private com.toedter.calendar.JDateChooser txtAudiencia;
    private com.toedter.calendar.JDateChooser txtAudiencia1;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JTextField txtBairro1;
    private javax.swing.JFormattedTextField txtCEP;
    private javax.swing.JFormattedTextField txtCEP1;
    private javax.swing.JFormattedTextField txtCPF;
    private javax.swing.JFormattedTextField txtCPF1;
    private javax.swing.JTextField txtCidade;
    private javax.swing.JTextField txtCidade1;
    private javax.swing.JTextField txtComp;
    private javax.swing.JTextField txtComp1;
    private com.toedter.calendar.JDateChooser txtDtnasc;
    private com.toedter.calendar.JDateChooser txtDtnasc1;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEmail1;
    private javax.swing.JTextField txtEndereco;
    private javax.swing.JTextField txtEndereco1;
    private javax.swing.JComboBox<String> txtEstado;
    private javax.swing.JComboBox<String> txtEstado1;
    private javax.swing.JComboBox<String> txtEstcivil;
    private javax.swing.JComboBox<String> txtEstcivil1;
    private javax.swing.JFormattedTextField txtFone1;
    private javax.swing.JFormattedTextField txtFone2;
    private javax.swing.JTextField txtIDClientes;
    private javax.swing.JTextField txtNomeCliente;
    private javax.swing.JTextField txtNomeCliente1;
    private javax.swing.JTextField txtNum;
    private javax.swing.JTextField txtNum1;
    private javax.swing.JTextField txtPesquisarCliente;
    private javax.swing.JFormattedTextField txtProcesso;
    private javax.swing.JFormattedTextField txtProcesso1;
    public static javax.swing.JLabel txtTotalCliBD;
    // End of variables declaration//GEN-END:variables

    private ImageIcon format = null;
    String filename = null;
    int s = 0;
    byte[] person_image = null;

}
