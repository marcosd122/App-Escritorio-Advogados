package projeto_escritorio;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.TabStop;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
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
import java.awt.geom.RoundRectangle2D;
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
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.GregorianCalendar;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import net.proteanit.sql.DbUtils;
import org.apache.logging.log4j.core.jmx.Server;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static projeto_escritorio.Home_adv.lbl_Usuario_BD;

public class Home_admin extends javax.swing.JFrame {

    Connection conn = null;
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rst = null;
    Statement st = null;

    String logradouro = "";
    String Bairro = "";
    String cidade = "";
    String uf = "";

    public Home_admin() {

        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");

        setUndecorated(true);

        initComponents();

        conn = ConnectDB.getConnectionCliente();
        con = ConnectDB.getConnection();

        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.black);
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/img/icojustice3.png")).getImage());

        numClientes();
        mostrarData();
        mostrarHora();
        Update_users();
        LogUsuario();
        get_Total();
        getUserPgt();
        Update_pgto();
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

    private void Update_users() {

        try {
            conn = ConnectDB.getConnection();
            String sql = "select * from Contas_user";
            pst = conn.prepareStatement(sql);
            rst = pst.executeQuery();
            tblAdv.setModel(DbUtils.resultSetToTableModel(rst));
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

    private void LogUsuario() {

        try {
            conn = ConnectDB.getConnectionCliente();
            String sql = "Select * from Log";
            pst = conn.prepareStatement(sql);
            rst = pst.executeQuery();
            tblUsusariosLog.setModel(DbUtils.resultSetToTableModel(rst));
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

    private void PesquisarLog() {

        try {
            conn = ConnectDB.getConnectionCliente();
            String sql = "Select ID, Usuário, Data, Status from Log where Usuário LIKE ? OR Data LIKE ?" + "Order by Usuário ASC";
            pst = conn.prepareStatement(sql);

            pst.setString(1, txtPesquisarLog.getText() + "%");
            pst.setString(2, txtPesquisarLog.getText() + "%");

            rst = pst.executeQuery();

            tblUsusariosLog.setModel(DbUtils.resultSetToTableModel(rst));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        } finally {
            try {
                rst.close();
                pst.close();
            } catch (Exception e) {

            }
        }
    }

    private void getEdit() {

        if (!tblAdv.getSelectionModel().isSelectionEmpty()) {

            int rowIndex = tblAdv.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) tblAdv.getModel();

            txtIDAdv.setText(model.getValueAt(rowIndex, 0).toString());

            txtUser1.setText(model.getValueAt(rowIndex, 1).toString());
            txtPass1.setText(model.getValueAt(rowIndex, 2).toString());
            txtNomeAdv1.setText(model.getValueAt(rowIndex, 3).toString());

            txtOAB2.setText(model.getValueAt(rowIndex, 4).toString());

            txtCPF_Adv2.setText(model.getValueAt(rowIndex, 5).toString());

            try {
                Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(model.getValueAt(rowIndex, 6).toString());
                txtDtnasc_Adv2.setDate(date2);
            } catch (ParseException ex) {
                Logger.getLogger(Home_admin.class.getName()).log(Level.SEVERE, null, ex);
            }

            txtEstcivil1.setSelectedItem(model.getValueAt(rowIndex, 7).toString());
            txtFone1_Adv2.setText(model.getValueAt(rowIndex, 8).toString());
            txtEmail_Adv2.setText(model.getValueAt(rowIndex, 9).toString());
            txtCEP1.setText(model.getValueAt(rowIndex, 10).toString());
            txtEndereco1.setText(model.getValueAt(rowIndex, 11).toString());
            txtComp1.setText(model.getValueAt(rowIndex, 12).toString());
            txtNum1.setText(model.getValueAt(rowIndex, 13).toString());
            txtBairro1.setText(model.getValueAt(rowIndex, 14).toString());
            txtCidade1.setText(model.getValueAt(rowIndex, 15).toString());
            txtEstado1.setSelectedItem(model.getValueAt(rowIndex, 16).toString());

            lblStatus1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/verdadeiro.png")));

            try {

                byte[] byteIm = (byte[]) tblAdv.getValueAt(rowIndex, 17);
                format = new ImageIcon(byteIm);
                ImageIcon format = new ImageIcon(scaledImage(byteIm, lbl_img1.getWidth(), lbl_img1.getHeight()));
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
        try {
            pst = conn.prepareStatement("select count(*) from Clientes");
            rst = pst.executeQuery();
            while (rst.next()) {
                int count = rst.getInt(1);
                lbl_Clientes.setText(String.valueOf(count));
            }

            try {

                pst = con.prepareStatement("select count(*) from Contas_user");
                rst = pst.executeQuery();
                while (rst.next()) {
                    int count1 = rst.getInt(1);
                    lbl_advogado.setText(String.valueOf(count1));
                }

            } catch (Exception e) {
            }

        } catch (Exception e) {
        }
    }

    private void Update_pgto() {

        try {

            conn = ConnectDB.getConnection();
            String sql = "Select * from Pagamentos";
            pst = conn.prepareStatement(sql);
            rst = pst.executeQuery();
            Tbl_Relat_Pgto.setModel(DbUtils.resultSetToTableModel(rst));

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

    private void getUserPgt() {

        try {

            conn = ConnectDB.getConnection();
            String sql = "Select Nome, CPF from Contas_user";
            pst = conn.prepareStatement(sql);
            rst = pst.executeQuery();
            tblAdvPagamento.setModel(DbUtils.resultSetToTableModel(rst));

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
        btn_log = new javax.swing.JButton();
        agenda = new javax.swing.JPanel();
        icon6 = new javax.swing.JLabel();
        btn_Pagamento = new javax.swing.JButton();
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
        jLabel12 = new javax.swing.JLabel();
        lbl_Clientes = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lbl_advogado = new javax.swing.JLabel();
        Pnl_Novo = new javax.swing.JPanel();
        pnlCad1 = new javax.swing.JPanel();
        Nome = new javax.swing.JLabel();
        Estado_civil = new javax.swing.JLabel();
        CPF = new javax.swing.JLabel();
        DtNasc = new javax.swing.JLabel();
        txtNomeAdv = new javax.swing.JTextField();
        txtCPF_Adv = new javax.swing.JFormattedTextField();
        txtEstcivil = new javax.swing.JComboBox<>();
        Tel1 = new javax.swing.JLabel();
        txtFone1_Adv = new javax.swing.JFormattedTextField();
        email = new javax.swing.JLabel();
        txtEmail_Adv = new javax.swing.JTextField();
        proces = new javax.swing.JLabel();
        txtDtnasc_Adv = new com.toedter.calendar.JDateChooser();
        lbl_img = new javax.swing.JLabel();
        btn_img = new javax.swing.JButton();
        txtOAB = new javax.swing.JTextField();
        txtUser = new javax.swing.JTextField();
        Tel3 = new javax.swing.JLabel();
        Tel4 = new javax.swing.JLabel();
        txtPass = new javax.swing.JPasswordField();
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
        btnSalvarAdv = new javax.swing.JButton();
        btnLimparAdv = new javax.swing.JButton();
        Pnl_Atualizar = new javax.swing.JPanel();
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
        txtIDAdv = new javax.swing.JTextField();
        pnlCad4 = new javax.swing.JPanel();
        Nome2 = new javax.swing.JLabel();
        Estado_civil2 = new javax.swing.JLabel();
        CPF2 = new javax.swing.JLabel();
        DtNasc2 = new javax.swing.JLabel();
        txtNomeAdv1 = new javax.swing.JTextField();
        txtCPF_Adv2 = new javax.swing.JFormattedTextField();
        txtEstcivil1 = new javax.swing.JComboBox<>();
        Tel5 = new javax.swing.JLabel();
        txtFone1_Adv2 = new javax.swing.JFormattedTextField();
        email2 = new javax.swing.JLabel();
        txtEmail_Adv2 = new javax.swing.JTextField();
        proces2 = new javax.swing.JLabel();
        txtDtnasc_Adv2 = new com.toedter.calendar.JDateChooser();
        lbl_img1 = new javax.swing.JLabel();
        btn_img1 = new javax.swing.JButton();
        txtOAB2 = new javax.swing.JTextField();
        txtUser1 = new javax.swing.JTextField();
        Tel6 = new javax.swing.JLabel();
        Tel7 = new javax.swing.JLabel();
        txtPass1 = new javax.swing.JPasswordField();
        Pnl_Editar = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAdv = new javax.swing.JTable();
        btn_Editar_Cli = new javax.swing.JButton();
        btnExcluirCliente = new javax.swing.JButton();
        btnExportarExcel = new javax.swing.JButton();
        lbl_TotalClientes = new javax.swing.JLabel();
        Pnl_Log = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblUsusariosLog = new javax.swing.JTable();
        txtPesquisarLog = new javax.swing.JTextField();
        Total_Cli_Up = new javax.swing.JLabel();
        Pnl_Pagamento = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblAdvPagamento = new javax.swing.JTable();
        btn_Salvar_Pgt = new javax.swing.JButton();
        txtAdv = new javax.swing.JTextField();
        txtAdv1 = new javax.swing.JTextField();
        txtAdvHon = new javax.swing.JTextField();
        txtAdvProd = new javax.swing.JTextField();
        lblhon = new javax.swing.JLabel();
        lblprod = new javax.swing.JLabel();
        dtPagamento = new com.toedter.calendar.JDateChooser();
        lblhon1 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        btn_Relat = new javax.swing.JButton();
        Pnl_Relatorio = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        Tbl_Relat_Pgto = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
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

        btn_novo.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        btn_novo.setText("Novo usuário");
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
                .addContainerGap(17, Short.MAX_VALUE))
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

        btn_editar.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        btn_editar.setText("Editar usuário");
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        btn_log.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        btn_log.setText("Log de usuário");
        btn_log.setBorder(null);
        btn_log.setContentAreaFilled(false);
        btn_log.setDoubleBuffered(true);
        btn_log.setFocusCycleRoot(true);
        btn_log.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_log.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_logActionPerformed(evt);
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
                .addComponent(btn_log)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pesquisarLayout.setVerticalGroup(
            pesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(icon4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pesquisarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_log, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        btn_Pagamento.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        btn_Pagamento.setText("Pagamentos");
        btn_Pagamento.setBorder(null);
        btn_Pagamento.setContentAreaFilled(false);
        btn_Pagamento.setDoubleBuffered(true);
        btn_Pagamento.setFocusCycleRoot(true);
        btn_Pagamento.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_Pagamento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_Pagamento.setName("agenda"); // NOI18N
        btn_Pagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PagamentoActionPerformed(evt);
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
                .addComponent(btn_Pagamento)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        agendaLayout.setVerticalGroup(
            agendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(icon6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(agendaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Pagamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jLabel12.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("CLIENTES");

        lbl_Clientes.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        lbl_Clientes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Clientes.setText("CLIENTES");

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/adv3.png"))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("ADVOGADOS");

        lbl_advogado.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        lbl_advogado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_advogado.setText("ADVOGADO");

        javax.swing.GroupLayout Pnl_HomeLayout = new javax.swing.GroupLayout(Pnl_Home);
        Pnl_Home.setLayout(Pnl_HomeLayout);
        Pnl_HomeLayout.setHorizontalGroup(
            Pnl_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_HomeLayout.createSequentialGroup()
                .addGap(276, 276, 276)
                .addGroup(Pnl_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(Pnl_HomeLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(Pnl_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_Clientes, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Pnl_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(Pnl_HomeLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(Pnl_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_advogado, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(266, 266, 266))
            .addGroup(Pnl_HomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        Pnl_HomeLayout.setVerticalGroup(
            Pnl_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_HomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE)
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
                .addGap(78, 78, 78))
        );

        rSPanelsSlider1.add(Pnl_Home, "card1");

        Pnl_Novo.setBackground(new java.awt.Color(255, 255, 255));
        Pnl_Novo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de Advogados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        Pnl_Novo.setName("Pnl_Novo"); // NOI18N

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

        txtNomeAdv.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        try {
            txtCPF_Adv.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCPF_Adv.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        txtEstcivil.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtEstcivil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Solteiro(a)", "Casado(a)", "Divorciado(a)", "Separado(a) de Fato / Judicialmente", "Viúvo(a)", "União Estável" }));

        Tel1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        Tel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        Tel1.setText("Telefone 1:");
        Tel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        try {
            txtFone1_Adv.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#.####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtFone1_Adv.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        email.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        email.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        email.setText("Email:");
        email.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        txtEmail_Adv.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        proces.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        proces.setText("OAB:");

        txtDtnasc_Adv.setDateFormatString("dd/MM/yyyy"); // NOI18N

        lbl_img.setBackground(new java.awt.Color(51, 51, 51));
        lbl_img.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_img.setAlignmentY(0.0F);
        lbl_img.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lbl_img.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lbl_img.setMaximumSize(new java.awt.Dimension(140, 140));
        lbl_img.setMinimumSize(new java.awt.Dimension(140, 140));

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

        txtOAB.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        txtUser.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        Tel3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        Tel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        Tel3.setText("USUÁRIO");
        Tel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        Tel4.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        Tel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        Tel4.setText("SENHA");
        Tel4.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        txtPass.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        javax.swing.GroupLayout pnlCad1Layout = new javax.swing.GroupLayout(pnlCad1);
        pnlCad1.setLayout(pnlCad1Layout);
        pnlCad1Layout.setHorizontalGroup(
            pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCad1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCad1Layout.createSequentialGroup()
                        .addComponent(Tel3)
                        .addGap(18, 18, 18)
                        .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(124, 124, 124)
                        .addComponent(Tel4)
                        .addGap(18, 18, 18)
                        .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlCad1Layout.createSequentialGroup()
                        .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEstcivil, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Nome)
                            .addGroup(pnlCad1Layout.createSequentialGroup()
                                .addComponent(Estado_civil)
                                .addGap(246, 246, 246)
                                .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Tel1)
                                    .addComponent(txtFone1_Adv, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtNomeAdv))
                        .addGap(18, 18, 18)
                        .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmail_Adv, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(email)
                            .addGroup(pnlCad1Layout.createSequentialGroup()
                                .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(proces)
                                    .addComponent(txtOAB, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(36, 36, 36)
                                .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCPF_Adv, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CPF))
                                .addGap(33, 33, 33)
                                .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(DtNasc)
                                    .addComponent(txtDtnasc_Adv, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCad1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(lbl_img, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCad1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_img)
                        .addGap(45, 45, 45))))
        );
        pnlCad1Layout.setVerticalGroup(
            pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCad1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_img, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(pnlCad1Layout.createSequentialGroup()
                            .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(pnlCad1Layout.createSequentialGroup()
                                        .addComponent(proces)
                                        .addGap(31, 31, 31))
                                    .addComponent(txtOAB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnlCad1Layout.createSequentialGroup()
                                    .addComponent(CPF)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtCPF_Adv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnlCad1Layout.createSequentialGroup()
                                    .addComponent(DtNasc)
                                    .addGap(9, 9, 9)
                                    .addComponent(txtDtnasc_Adv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(15, 15, 15)
                            .addComponent(email)
                            .addGap(3, 3, 3)
                            .addComponent(txtEmail_Adv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlCad1Layout.createSequentialGroup()
                            .addComponent(Nome)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtNomeAdv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnlCad1Layout.createSequentialGroup()
                                    .addGap(15, 15, 15)
                                    .addComponent(Tel1))
                                .addGroup(pnlCad1Layout.createSequentialGroup()
                                    .addGap(13, 13, 13)
                                    .addComponent(Estado_civil)))
                            .addGap(3, 3, 3)
                            .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtEstcivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtFone1_Adv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCad1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addGroup(pnlCad1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Tel3)
                            .addComponent(Tel4)
                            .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26))
                    .addGroup(pnlCad1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btn_img)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

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

        btn_BuscarCEP.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btn_BuscarCEP.setText("BUSCAR");
        btn_BuscarCEP.setBorder(null);
        btn_BuscarCEP.setBorderPainted(false);
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
                                .addContainerGap(74, Short.MAX_VALUE))))
                    .addGroup(pnlCad2Layout.createSequentialGroup()
                        .addGroup(pnlCad2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCad2Layout.createSequentialGroup()
                                .addComponent(cep)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCEP, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_BuscarCEP)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                            .addComponent(btn_BuscarCEP))
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
                .addContainerGap(28, Short.MAX_VALUE))
        );

        btnSalvarAdv.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btnSalvarAdv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save.png"))); // NOI18N
        btnSalvarAdv.setText("SALVAR");
        btnSalvarAdv.setBorder(null);
        btnSalvarAdv.setContentAreaFilled(false);
        btnSalvarAdv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarAdvActionPerformed(evt);
            }
        });

        btnLimparAdv.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btnLimparAdv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/limpar.png"))); // NOI18N
        btnLimparAdv.setText("LIMPAR");
        btnLimparAdv.setBorder(null);
        btnLimparAdv.setContentAreaFilled(false);
        btnLimparAdv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparAdvActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Pnl_NovoLayout = new javax.swing.GroupLayout(Pnl_Novo);
        Pnl_Novo.setLayout(Pnl_NovoLayout);
        Pnl_NovoLayout.setHorizontalGroup(
            Pnl_NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_NovoLayout.createSequentialGroup()
                .addGap(428, 428, 428)
                .addComponent(btnSalvarAdv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(111, 111, 111)
                .addComponent(btnLimparAdv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(439, 439, 439))
            .addGroup(Pnl_NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_NovoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlCad1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(Pnl_NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_NovoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlCad2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        Pnl_NovoLayout.setVerticalGroup(
            Pnl_NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_NovoLayout.createSequentialGroup()
                .addContainerGap(580, Short.MAX_VALUE)
                .addGroup(Pnl_NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSalvarAdv, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimparAdv))
                .addContainerGap())
            .addGroup(Pnl_NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_NovoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlCad1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(397, Short.MAX_VALUE)))
            .addGroup(Pnl_NovoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_NovoLayout.createSequentialGroup()
                    .addGap(259, 259, 259)
                    .addComponent(pnlCad2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(155, Short.MAX_VALUE)))
        );

        rSPanelsSlider1.add(Pnl_Novo, "card2");

        Pnl_Atualizar.setBackground(new java.awt.Color(255, 255, 255));
        Pnl_Atualizar.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Atualizar Cadastro de Advogados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        Pnl_Atualizar.setName("Pnl_Novo"); // NOI18N

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
        txtCEP1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCEP1KeyReleased(evt);
            }
        });

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
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

        txtIDAdv.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtIDAdv.setForeground(new java.awt.Color(255, 255, 255));
        txtIDAdv.setBorder(null);

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

        txtNomeAdv1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        try {
            txtCPF_Adv2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCPF_Adv2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        txtEstcivil1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtEstcivil1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Solteiro(a)", "Casado(a)", "Divorciado(a)", "Separado(a) de Fato / Judicialmente", "Viúvo(a)", "União Estável" }));

        Tel5.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        Tel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        Tel5.setText("Telefone 1:");
        Tel5.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        try {
            txtFone1_Adv2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#.####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtFone1_Adv2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        email2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        email2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        email2.setText("Email:");
        email2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        txtEmail_Adv2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        proces2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        proces2.setText("OAB:");

        txtDtnasc_Adv2.setDateFormatString("dd/MM/yyyy"); // NOI18N

        lbl_img1.setBackground(new java.awt.Color(51, 51, 51));
        lbl_img1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_img1.setAlignmentY(0.0F);
        lbl_img1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lbl_img1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lbl_img1.setMaximumSize(new java.awt.Dimension(140, 140));
        lbl_img1.setMinimumSize(new java.awt.Dimension(140, 140));

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

        txtOAB2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        txtUser1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        Tel6.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        Tel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        Tel6.setText("USUÁRIO");
        Tel6.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        Tel7.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        Tel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        Tel7.setText("SENHA");
        Tel7.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        txtPass1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        javax.swing.GroupLayout pnlCad4Layout = new javax.swing.GroupLayout(pnlCad4);
        pnlCad4.setLayout(pnlCad4Layout);
        pnlCad4Layout.setHorizontalGroup(
            pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCad4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCad4Layout.createSequentialGroup()
                        .addComponent(txtEstcivil1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlCad4Layout.createSequentialGroup()
                        .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Nome2)
                            .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtNomeAdv1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlCad4Layout.createSequentialGroup()
                                    .addComponent(Estado_civil2)
                                    .addGap(246, 246, 246)
                                    .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(Tel5)
                                        .addComponent(txtFone1_Adv2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(pnlCad4Layout.createSequentialGroup()
                                    .addComponent(Tel6, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtUser1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmail_Adv2, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlCad4Layout.createSequentialGroup()
                                .addComponent(Tel7)
                                .addGap(18, 18, 18)
                                .addComponent(txtPass1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(email2)
                            .addGroup(pnlCad4Layout.createSequentialGroup()
                                .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtOAB2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(proces2))
                                .addGap(29, 29, 29)
                                .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCPF_Adv2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CPF2))
                                .addGap(32, 32, 32)
                                .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(DtNasc2)
                                    .addComponent(txtDtnasc_Adv2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(44, 44, 44)))
                .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_img1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlCad4Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(btn_img1)))
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
                                .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Nome2)
                                    .addComponent(CPF2, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtOAB2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtNomeAdv1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtCPF_Adv2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(proces2)
                            .addGroup(pnlCad4Layout.createSequentialGroup()
                                .addComponent(DtNasc2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDtnasc_Adv2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCad4Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(Tel5))
                            .addGroup(pnlCad4Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(Estado_civil2))
                            .addGroup(pnlCad4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(email2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEstcivil1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFone1_Adv2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmail_Adv2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lbl_img1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCad4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addGroup(pnlCad4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Tel6)
                            .addComponent(Tel7)
                            .addComponent(txtPass1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26))
                    .addGroup(pnlCad4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btn_img1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

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
            .addGroup(Pnl_AtualizarLayout.createSequentialGroup()
                .addGap(511, 511, 511)
                .addComponent(txtIDAdv, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(Pnl_AtualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_AtualizarLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlCad5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(Pnl_AtualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_AtualizarLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlCad4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        Pnl_AtualizarLayout.setVerticalGroup(
            Pnl_AtualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_AtualizarLayout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addComponent(txtIDAdv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 406, Short.MAX_VALUE)
                .addGroup(Pnl_AtualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLimparCli_Update)
                    .addComponent(btnUpdateCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(Pnl_AtualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_AtualizarLayout.createSequentialGroup()
                    .addGap(270, 270, 270)
                    .addComponent(pnlCad5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(150, Short.MAX_VALUE)))
            .addGroup(Pnl_AtualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_AtualizarLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlCad4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(393, Short.MAX_VALUE)))
        );

        rSPanelsSlider1.add(Pnl_Atualizar, "card3");

        Pnl_Editar.setBackground(new java.awt.Color(255, 255, 255));
        Pnl_Editar.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        Pnl_Editar.setName("Pnl_Editar"); // NOI18N

        tblAdv.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        tblAdv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "CPF", "Data de Nascimento", "Estado Civil", "E-mail", "Telefone", "CEP", "Endereço", "Complemento", "Numero", "Bairro", "Cidade", "Estado", "Foto"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAdv.setGridColor(new java.awt.Color(0, 0, 0));
        tblAdv.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAdv.getTableHeader().setReorderingAllowed(false);
        tblAdv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAdvMouseClicked(evt);
            }
        });
        tblAdv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblAdvKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblAdv);
        if (tblAdv.getColumnModel().getColumnCount() > 0) {
            tblAdv.getColumnModel().getColumn(0).setResizable(false);
            tblAdv.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblAdv.getColumnModel().getColumn(1).setResizable(false);
            tblAdv.getColumnModel().getColumn(2).setResizable(false);
            tblAdv.getColumnModel().getColumn(3).setResizable(false);
            tblAdv.getColumnModel().getColumn(4).setResizable(false);
            tblAdv.getColumnModel().getColumn(5).setResizable(false);
            tblAdv.getColumnModel().getColumn(6).setResizable(false);
            tblAdv.getColumnModel().getColumn(7).setResizable(false);
            tblAdv.getColumnModel().getColumn(8).setResizable(false);
            tblAdv.getColumnModel().getColumn(9).setResizable(false);
            tblAdv.getColumnModel().getColumn(10).setResizable(false);
            tblAdv.getColumnModel().getColumn(11).setResizable(false);
            tblAdv.getColumnModel().getColumn(12).setResizable(false);
            tblAdv.getColumnModel().getColumn(13).setResizable(false);
            tblAdv.getColumnModel().getColumn(14).setResizable(false);
        }

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 524, Short.MAX_VALUE)
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
                    .addContainerGap(171, Short.MAX_VALUE)))
        );

        rSPanelsSlider1.add(Pnl_Editar, "card4");

        Pnl_Log.setBackground(new java.awt.Color(255, 255, 255));
        Pnl_Log.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pesquisar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        Pnl_Log.setName("Pnl_Log"); // NOI18N

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));

        tblUsusariosLog.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        tblUsusariosLog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Processo", "Audiência", "Telefone"
            }
        ));
        tblUsusariosLog.setGridColor(new java.awt.Color(255, 255, 255));
        tblUsusariosLog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsusariosLogMouseClicked(evt);
            }
        });
        tblUsusariosLog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblUsusariosLogKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblUsusariosLog);

        txtPesquisarLog.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtPesquisarLog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarLogKeyReleased(evt);
            }
        });

        Total_Cli_Up.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        javax.swing.GroupLayout Pnl_LogLayout = new javax.swing.GroupLayout(Pnl_Log);
        Pnl_Log.setLayout(Pnl_LogLayout);
        Pnl_LogLayout.setHorizontalGroup(
            Pnl_LogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pnl_LogLayout.createSequentialGroup()
                .addContainerGap(773, Short.MAX_VALUE)
                .addComponent(Total_Cli_Up, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(242, 242, 242))
            .addGroup(Pnl_LogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_LogLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(Pnl_LogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1150, Short.MAX_VALUE)
                        .addGroup(Pnl_LogLayout.createSequentialGroup()
                            .addComponent(txtPesquisarLog, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        Pnl_LogLayout.setVerticalGroup(
            Pnl_LogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_LogLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(Total_Cli_Up, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(544, Short.MAX_VALUE))
            .addGroup(Pnl_LogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Pnl_LogLayout.createSequentialGroup()
                    .addGap(79, 79, 79)
                    .addComponent(txtPesquisarLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                    .addGap(79, 79, 79)))
        );

        rSPanelsSlider1.add(Pnl_Log, "card5");

        Pnl_Pagamento.setBackground(new java.awt.Color(255, 255, 255));
        Pnl_Pagamento.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pagamentos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        Pnl_Pagamento.setName("Pnl_Pagamento"); // NOI18N

        tblAdvPagamento.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        tblAdvPagamento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "CPF"
            }
        ));
        tblAdvPagamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAdvPagamentoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblAdvPagamento);

        btn_Salvar_Pgt.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btn_Salvar_Pgt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/addsalario.png"))); // NOI18N
        btn_Salvar_Pgt.setText("SALVAR PAGAMENTO");
        btn_Salvar_Pgt.setToolTipText("");
        btn_Salvar_Pgt.setBorder(null);
        btn_Salvar_Pgt.setBorderPainted(false);
        btn_Salvar_Pgt.setContentAreaFilled(false);
        btn_Salvar_Pgt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Salvar_PgtActionPerformed(evt);
            }
        });

        txtAdv.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        txtAdv1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        txtAdvHon.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtAdvHon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAdvHonKeyTyped(evt);
            }
        });

        txtAdvProd.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        txtAdvProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAdvProdKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAdvProdKeyTyped(evt);
            }
        });

        lblhon.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        lblhon.setText("HONORÁRIOS");

        lblprod.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        lblprod.setText("PRODUTIVIDADE");

        dtPagamento.setDateFormatString("dd/MM/yyyy");
        dtPagamento.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N

        lblhon1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        lblhon1.setText("DATA DO PAGAMENTO");

        lblTotal.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btn_Relat.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        btn_Relat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/contrato.png"))); // NOI18N
        btn_Relat.setText("RELATÓRIO DE PAGAMENTO");
        btn_Relat.setBorderPainted(false);
        btn_Relat.setContentAreaFilled(false);
        btn_Relat.setFocusPainted(false);
        btn_Relat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RelatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Pnl_PagamentoLayout = new javax.swing.GroupLayout(Pnl_Pagamento);
        Pnl_Pagamento.setLayout(Pnl_PagamentoLayout);
        Pnl_PagamentoLayout.setHorizontalGroup(
            Pnl_PagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_PagamentoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pnl_PagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_PagamentoLayout.createSequentialGroup()
                        .addGroup(Pnl_PagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAdv, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAdv1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(Pnl_PagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(Pnl_PagamentoLayout.createSequentialGroup()
                                .addComponent(lblprod)
                                .addGap(18, 18, 18)
                                .addComponent(txtAdvProd, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Pnl_PagamentoLayout.createSequentialGroup()
                                .addComponent(lblhon)
                                .addGap(18, 18, 18)
                                .addComponent(txtAdvHon, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Pnl_PagamentoLayout.createSequentialGroup()
                                .addComponent(lblhon1)
                                .addGap(18, 18, 18)
                                .addComponent(dtPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(233, 233, 233))
                    .addGroup(Pnl_PagamentoLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(Pnl_PagamentoLayout.createSequentialGroup()
                .addGap(319, 319, 319)
                .addComponent(btn_Salvar_Pgt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(148, 148, 148)
                .addComponent(btn_Relat)
                .addGap(284, 284, 284))
        );
        Pnl_PagamentoLayout.setVerticalGroup(
            Pnl_PagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_PagamentoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pnl_PagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_PagamentoLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(Pnl_PagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAdv1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(Pnl_PagamentoLayout.createSequentialGroup()
                        .addGroup(Pnl_PagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAdv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblhon)
                            .addComponent(txtAdvHon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(Pnl_PagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblhon1)
                            .addComponent(dtPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(Pnl_PagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAdvProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblprod))))
                .addGap(21, 21, 21)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(Pnl_PagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Salvar_Pgt, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Relat))
                .addGap(38, 38, 38))
        );

        rSPanelsSlider1.add(Pnl_Pagamento, "card6");

        Pnl_Relatorio.setBackground(new java.awt.Color(255, 255, 255));
        Pnl_Relatorio.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Relatório", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        Pnl_Relatorio.setName("Pnl_Relatorio"); // NOI18N

        Tbl_Relat_Pgto.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        Tbl_Relat_Pgto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"
            }
        ));
        Tbl_Relat_Pgto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tbl_Relat_PgtoMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(Tbl_Relat_Pgto);

        jButton1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exportarexcel.png"))); // NOI18N
        jButton1.setText("EXPORTAR RELATÓRIO EM EXCEL");
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Pnl_RelatorioLayout = new javax.swing.GroupLayout(Pnl_Relatorio);
        Pnl_Relatorio.setLayout(Pnl_RelatorioLayout);
        Pnl_RelatorioLayout.setHorizontalGroup(
            Pnl_RelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_RelatorioLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jScrollPane6)
                .addGap(51, 51, 51))
            .addGroup(Pnl_RelatorioLayout.createSequentialGroup()
                .addGap(440, 440, 440)
                .addComponent(jButton1)
                .addContainerGap(457, Short.MAX_VALUE))
        );
        Pnl_RelatorioLayout.setVerticalGroup(
            Pnl_RelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_RelatorioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jButton1)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        rSPanelsSlider1.add(Pnl_Relatorio, "card12");

        Pnl_Configuracao.setBackground(new java.awt.Color(255, 255, 255));
        Pnl_Configuracao.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configuração", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        Pnl_Configuracao.setName("Pnl_Configuracao"); // NOI18N

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 154, Short.MAX_VALUE)
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
                .addContainerGap(249, Short.MAX_VALUE))
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

            conn = ConnectDB.getConnectionCliente();
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


    private void btn_BuscarCEPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarCEPActionPerformed
        buscarCep(txtCEP.getText());
    }//GEN-LAST:event_btn_BuscarCEPActionPerformed

    private void btnSalvarAdvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarAdvActionPerformed

        if (txtNomeAdv.getText().isEmpty() || txtOAB.getText().isEmpty() || txtCPF_Adv.getText().isEmpty()
                || txtDtnasc_Adv.getDate() == null || txtEstcivil.getSelectedItem().toString().isEmpty() || txtEmail_Adv.getText().isEmpty()
                || txtFone1_Adv.getText().isEmpty() || txtCEP.getText().isEmpty() || txtEndereco.getText().isEmpty()
                || txtNum.getText().isEmpty() || txtBairro.getText().isEmpty() || txtUser.getText().isEmpty() || txtPass.getText().isEmpty()
                || txtCidade.getText().isEmpty() || txtEstado.getSelectedItem().toString().isEmpty()
                || person_image == null) {

            JOptionPane.showMessageDialog(this, "Favor preencher todos os campos");

        } else {

            int p = JOptionPane.showConfirmDialog(null, "Gostaria de adicionar estes dados?", "Adicionar Advogado", JOptionPane.YES_NO_OPTION);
            if (p == 0) {

                try {
                    con = ConnectDB.getConnection();
                    String sql = "insert into Contas_user (Nome, OAB, CPF, Data_de_Nascimento, Estado_Civil, Email, Telefone,CEP,Endereço,Complemento,Numero,Bairro,Cidade,Estado,Foto,Usuário_adv, Senha_adv) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    pst = con.prepareStatement(sql);

                    pst.setString(1, txtNomeAdv.getText());
                    pst.setString(2, txtOAB.getText());

                    Date data = txtDtnasc_Adv.getDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String date = sdf.format(txtDtnasc_Adv.getDate());

                    pst.setString(3, txtCPF_Adv.getText());
                    pst.setString(4, date);
                    pst.setString(5, txtEstcivil.getSelectedItem().toString());
                    pst.setString(6, txtEmail_Adv.getText());
                    pst.setString(7, txtFone1_Adv.getText());
                    pst.setString(8, txtCEP.getText());
                    pst.setString(9, txtEndereco.getText());
                    pst.setString(10, txtComp.getText());
                    pst.setString(11, txtNum.getText());
                    pst.setString(12, txtBairro.getText());
                    pst.setString(13, txtCidade.getText());
                    pst.setString(14, txtEstado.getSelectedItem().toString());
                    pst.setBytes(15, person_image);
                    pst.setString(16, txtUser.getText());
                    pst.setString(17, txtPass.getText());

                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Advogado adicionado com sucesso!!");

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
                    conn = ConnectDB.getConnectionCliente();
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
                dialog.setSelectedFile(new File(txtNomeAdv.getText() + " " + txtOAB.getText() + "-Contrato-Advogado" + ".pdf"));
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

                        Contrato.add(new Paragraph("CONTRATO DE ASSOCIAÇÃO COM ADVOGADO ", FontFactory.getFont(FontFactory.TIMES_BOLD, 15, Font.BOLD, BaseColor.BLACK)));

                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Contrato.add(new Paragraph("Pelo presente instrumento particular:", FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, BaseColor.BLACK)));

                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Paragraph texto1 = new Paragraph(txtNomeAdv.getText() + ", brasileiro(a), " + txtEstcivil.getSelectedItem().toString() + ", portador(a) do CPF N. " + txtCPF_Adv.getText() + ", residente e domiciliado na " + txtEndereco.getText() + ", Numero: " + txtNum.getText() + ", Bairro: " + txtBairro.getText() + ", na Cidade: " + txtCidade.getText() + ", no Estado de: " + txtEstado.getSelectedItem().toString() + ", CEP: " + txtCEP.getText() + ", Complemento: " + txtComp.getText() + ".", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                        texto1.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                        Contrato.add(texto1);

                        Contrato.add(new Paragraph("Denominado ASSOCIADO.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Paragraph texto2 = new Paragraph("De outro lado, ESCRITORIO DE ADVOCACIA, CNPJ 01.001.001/0001-00, localizado no Estado Rondônia. Doravante denominado SOCIEDADE, acordam o que segue.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                        texto2.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                        Contrato.add(texto2);

                        Contrato.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));

                        Paragraph texto3 = new Paragraph("O presente contrato tem por objeto regular a associação entre a SOCIEDADE e o ASSOCIADO, nos termos do artigo 39 do Regulamento Geral do Estatuto da Advocacia e da Ordem dos Advogados do Brasil de 16 de novembro de 1994.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                        texto3.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                        Contrato.add(texto3);

                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Paragraph texto4 = new Paragraph("CLÁUSULA PRIMEIRA -  O ASSOCIADO, pelo presente instrumento, se associa à SOCIEDADE, na categoria Advogado, e nessa condição se obriga a prestar serviços de advocacia consultiva, preventiva e contenciosa à SOCIEDADE, por prazo indeterminado, a contar da assinatura deste documento, em local e horário de conveniência da SOCIEDADE.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                        texto4.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                        Contrato.add(texto4);

                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Paragraph texto5 = new Paragraph("CLÁUSULA SEGUNDA – O não exercício de qualquer direito ou faculdade estabelecidos no presente contrato constituirá ato de mera liberalidade, não inovando ou criando direitos e precedentes a serem invocados por qualquer das partes.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
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
                        Contrato.add(new Paragraph("ASSOCIADO.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));

                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                        Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                        Paragraph texto8 = new Paragraph("____________________________", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                        texto8.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
                        Contrato.add(texto8);
                        Contrato.add(new Paragraph("SOCIEDADE.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));

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

            txtNomeAdv.setText(null);
            txtOAB.setText(null);
            txtCPF_Adv.setText(null);
            txtDtnasc_Adv.setDate(null);
            txtEstcivil.setSelectedItem(null);
            txtEmail_Adv.setText(null);
            txtFone1_Adv.setText(null);
            txtCEP.setText(null);
            txtEndereco.setText(null);
            txtComp.setText(null);
            txtNum.setText(null);
            txtBairro.setText(null);
            txtCidade.setText(null);
            txtEstado.setSelectedItem(null);
            lblStatus.setIcon(null);
            lbl_img.setIcon(null);
            txtUser.setText(null);
            txtPass.setText(null);

        }

        Update_users();
        numClientes();
        getUserPgt();
    }//GEN-LAST:event_btnSalvarAdvActionPerformed

    private void btnLimparAdvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparAdvActionPerformed

        txtNomeAdv.setText("");
        txtOAB.setText("");
        txtCPF_Adv.setText("");
        txtDtnasc_Adv.setDate(null);
        txtEstcivil.setSelectedItem(null);
        txtEmail_Adv.setText("");
        txtFone1_Adv.setText("");
        txtCEP.setText("");
        txtEndereco.setText("");
        txtComp.setText("");
        txtNum.setText("");
        txtBairro.setText("");
        txtCidade.setText("");
        txtEstado.setSelectedItem(null);
        lblStatus.setIcon(null);
        lbl_img.setIcon(null);


    }//GEN-LAST:event_btnLimparAdvActionPerformed


    private void btn_Buscar_UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Buscar_UpdateActionPerformed

        buscarCep(txtCEP1.getText());

    }//GEN-LAST:event_btn_Buscar_UpdateActionPerformed

    private void btnUpdateClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateClienteActionPerformed

        if (txtNomeAdv1.getText().isEmpty() || txtOAB2.getText().isEmpty() || txtCPF_Adv2.getText().isEmpty()
                || txtDtnasc_Adv2.getDate() == null || txtEstcivil1.getSelectedItem().toString().isEmpty() || txtEmail_Adv2.getText().isEmpty()
                || txtFone1_Adv2.getText().isEmpty() || txtCEP1.getText().isEmpty() || txtEndereco1.getText().isEmpty()
                || txtNum1.getText().isEmpty() || txtBairro1.getText().isEmpty()
                || txtCidade1.getText().isEmpty() || txtEstado1.getSelectedItem().toString().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Favor preencher todos os campos");

        } else {

            Date dtNasc = txtDtnasc_Adv2.getDate();
            SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
            String dataNasc = sdfD.format(txtDtnasc_Adv2.getDate());

            String value1 = txtNomeAdv1.getText();
            String value2 = txtOAB2.getText();
            String value3 = txtCPF_Adv2.getText();
            String value4 = dataNasc;
            String value5 = txtEstcivil1.getSelectedItem().toString();
            String value6 = txtEmail_Adv2.getText();
            String value7 = txtFone1_Adv2.getText();
            String value8 = txtCEP1.getText();
            String value9 = txtEndereco1.getText();
            String value10 = txtComp1.getText();
            String value11 = txtNum1.getText();
            String value12 = txtBairro1.getText();
            String value13 = txtCidade1.getText();
            String value14 = txtEstado1.getSelectedItem().toString();
            String value15 = txtIDAdv.getText();

            int p = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja atualizar o cadastro?", "Atualizar dados", JOptionPane.YES_NO_OPTION);
            if (p == 0) {

                try {
                    con = ConnectDB.getConnection();
                    String sql = "update Contas_user set Nome='" + value1 + "',OAB='" + value2 + "', CPF='" + value3 + "',"
                            + "Data_de_Nascimento='" + value4 + "',Estado_Civil='" + value5 + "',Email='" + value6 + "',Telefone= '" + value7 + "', "
                            + "CEP='" + value8 + "',Endereço='" + value9 + "',Complemento='" + value10 + "', Numero='" + value11 + "', Bairro='" + value12 + "',"
                            + " Cidade='" + value13 + "', Estado='" + value14 + "', ID='" + value15 + "' where id='" + value15 + "' ";

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
                    con = ConnectDB.getConnection();
                    String sql = "update Contas_user SET Foto = ? where ID = '" + value15 + "'";
                    pst = con.prepareStatement(sql);
                    pst.setBytes(1, image);
                    pst.executeUpdate();
                    pst.close();;

                } catch (Exception ex) {

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
                    conn = ConnectDB.getConnectionCliente();
                    String reg = "insert into Log (Usuário, Data,Status) values ('" + val + "','" + value0 + " / " + values + "','Registro atualizado')";
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
                    dialog.setSelectedFile(new File(txtNomeAdv1.getText() + " " + txtOAB2.getText() + "-Contrato" + ".pdf"));
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

//                            com.itextpdf.text.Image image1 = com.itextpdf.text.Image.getInstance(person_image);
//                            image1.setAbsolutePosition(473f, 750f);
//                            image1.scaleAbsolute(80f, 70f);
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Contrato.add(new Paragraph("CONTRATO DE ASSOCIAÇÃO COM ADVOGADO ", FontFactory.getFont(FontFactory.TIMES_BOLD, 15, Font.BOLD, BaseColor.BLACK)));

                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Contrato.add(new Paragraph("Pelo presente instrumento particular:", FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, BaseColor.BLACK)));

                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Paragraph texto1 = new Paragraph(txtNomeAdv.getText() + ", brasileiro(a), " + txtEstcivil.getSelectedItem().toString() + ", portador(a) do CPF N. " + txtCPF_Adv.getText() + ", residente e domiciliado na " + txtEndereco.getText() + ", Numero: " + txtNum.getText() + ", Bairro: " + txtBairro.getText() + ", na Cidade: " + txtCidade.getText() + ", no Estado de: " + txtEstado.getSelectedItem().toString() + ", CEP: " + txtCEP.getText() + ", Complemento: " + txtComp.getText() + ".", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto1.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                            Contrato.add(texto1);

                            Contrato.add(new Paragraph("Denominado ASSOCIADO.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Paragraph texto2 = new Paragraph("De outro lado, ESCRITORIO DE ADVOCACIA, CNPJ 01.001.001/0001-00, localizado no Estado Rondônia. Doravante denominado SOCIEDADE, acordam o que segue.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto2.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                            Contrato.add(texto2);

                            Contrato.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));

                            Paragraph texto3 = new Paragraph("O presente contrato tem por objeto regular a associação entre a SOCIEDADE e o ASSOCIADO, nos termos do artigo 39 do Regulamento Geral do Estatuto da Advocacia e da Ordem dos Advogados do Brasil de 16 de novembro de 1994.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto3.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                            Contrato.add(texto3);

                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Paragraph texto4 = new Paragraph("CLÁUSULA PRIMEIRA -  O ASSOCIADO, pelo presente instrumento, se associa à SOCIEDADE, na categoria Advogado, e nessa condição se obriga a prestar serviços de advocacia consultiva, preventiva e contenciosa à SOCIEDADE, por prazo indeterminado, a contar da assinatura deste documento, em local e horário de conveniência da SOCIEDADE.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto4.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                            Contrato.add(texto4);

                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Paragraph texto5 = new Paragraph("CLÁUSULA SEGUNDA – O não exercício de qualquer direito ou faculdade estabelecidos no presente contrato constituirá ato de mera liberalidade, não inovando ou criando direitos e precedentes a serem invocados por qualquer das partes.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
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
                            Contrato.add(new Paragraph("ASSOCIADO.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));

                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Paragraph texto8 = new Paragraph("____________________________", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto8.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
                            Contrato.add(texto8);
                            Contrato.add(new Paragraph("SOCIEDADE.", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));

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

                    txtNomeAdv1.setText("");
                    txtOAB2.setText("");
                    txtCPF_Adv2.setText("");
                    txtDtnasc_Adv2.setDate(null);
                    txtEstcivil1.setSelectedItem(null);
                    txtEmail_Adv2.setText("");
                    txtFone1_Adv2.setText("");
                    txtCEP1.setText("");
                    txtEndereco1.setText("");
                    txtComp1.setText("");
                    txtNum1.setText("");
                    txtBairro1.setText("");
                    txtCidade1.setText("");
                    txtEstado1.setSelectedItem(null);
                    lblStatus1.setIcon(null);
                    lbl_img1.setIcon(null);

                }
            }
    }//GEN-LAST:event_btnUpdateClienteActionPerformed
        Update_users();

        numClientes();
    }

    private void btnLimparCli_UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparCli_UpdateActionPerformed

        txtNomeAdv1.setText("");
        txtOAB2.setText("");
        txtCPF_Adv2.setText("");
        txtDtnasc_Adv2.setDate(null);
        txtEstcivil1.setSelectedItem(null);
        txtEmail_Adv2.setText("");
        txtFone1_Adv2.setText("");
        txtCEP1.setText("");
        txtEndereco1.setText("");
        txtComp1.setText("");
        txtNum1.setText("");
        txtBairro1.setText("");
        txtCidade1.setText("");
        txtEstado1.setSelectedItem(null);
        lblStatus1.setIcon(null);
        lbl_img1.setIcon(null);
    }//GEN-LAST:event_btnLimparCli_UpdateActionPerformed


    private void tblAdvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAdvMouseClicked


    }//GEN-LAST:event_tblAdvMouseClicked

    private void numClientes() {

        try {

            lbl_TotalClientes.setText("Total de Advogados = " + tblAdv.getRowCount() + " ");

            Total_Cli_Up.setText("Total de Advogados = " + tblAdv.getRowCount() + " ");

            Home_admin.txtTotalCliBD.setText("Total de Advogados = " + Integer.toString(Contador1.countDB("Contas_user")));

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

                int del = tblAdv.getSelectedRow();
                String cel = (tblAdv.getModel().getValueAt(del, 0).toString());

                conn = ConnectDB.getConnection();
                String sql = "DELETE from Contas_user where ID=" + cel;

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
                conn = ConnectDB.getConnectionCliente();
                String reg = "insert into Log (Usuário, Data,Status) values ('" + val + "','" + value0 + " / " + values + "','Registro apagado')";
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
        Update_users();

        numClientes();
    }//GEN-LAST:event_btnExcluirClienteActionPerformed

    private void tblUsusariosLogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsusariosLogMouseClicked


    }//GEN-LAST:event_tblUsusariosLogMouseClicked

    private void tblUsusariosLogKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblUsusariosLogKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {

        }
    }//GEN-LAST:event_tblUsusariosLogKeyReleased


    private void txtPesquisarLogKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarLogKeyReleased

        PesquisarLog();

    }//GEN-LAST:event_txtPesquisarLogKeyReleased


    private void btn_Salvar_PgtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Salvar_PgtActionPerformed

        if (txtAdv.getText().isEmpty() || txtAdv1.getText().isEmpty() || txtAdvHon.getText().isEmpty() || dtPagamento.getDate() == null || txtAdvHon.getText().isEmpty() || txtAdvProd.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Favor preencher todos os campos");

        } else {

            int p = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja cadastrar o pagamento?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (p == 0) {

                try {
                    conn = ConnectDB.getConnection();
                    String query = "INSERT INTO Pagamentos (Nome,CPF,Honorarios,Data,Produtividade) values (?,?,?,?,?)";

                    pst = conn.prepareStatement(query);

                    pst.setString(1, txtAdv.getText());
                    pst.setString(2, txtAdv1.getText());
                    pst.setString(3, txtAdvHon.getText());

                    Date datapgto = dtPagamento.getDate();
                    SimpleDateFormat sDT = new SimpleDateFormat("dd/MM/yyyy");
                    String pgto = sDT.format(dtPagamento.getDate());

                    pst.setString(4, pgto);

                    pst.setString(5, txtAdvProd.getText());

                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Pagamento adicionado com sucesso!!");

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
                    conn = ConnectDB.getConnectionCliente();
                    String reg = "insert into Log (Usuário, Data,Status) values ('" + val + "','" + value0 + " / " + values + "','Pagamento adicionado')";
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
                    dialog.setSelectedFile(new File(txtAdv.getText() + " " + txtAdv1.getText() + "-Recibo-Pagamento" + ".pdf"));
                    int dialogResult = dialog.showSaveDialog(null);
                    if (dialogResult == JFileChooser.APPROVE_OPTION) {
                        String filePath = dialog.getSelectedFile().getPath();

                        try {

                            Date datapgto = dtPagamento.getDate();
                            SimpleDateFormat sDT = new SimpleDateFormat("dd/MM/yyyy");
                            String pgto = sDT.format(dtPagamento.getDate());

                            Document Contrato = new Document();
                            PdfWriter.getInstance(Contrato, new FileOutputStream(filePath));

                            Contrato.open();

                            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance((getClass().getResource("/img/icojustice2.png")));

                            image.scaleAbsolute(80f, 80f);
                            image.setAbsolutePosition(250f, 750f);

                            Contrato.add(image);

                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Contrato.add(new Paragraph("RECIBO DE PAGAMENTO", FontFactory.getFont(FontFactory.TIMES_BOLD, 15, Font.BOLD, BaseColor.BLACK)));

                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Contrato.add(new Paragraph("ESCRITORIO DE ADVOCACIA, CNPJ 01.001.001/0001-00:", FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, BaseColor.BLACK)));

                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

                            Paragraph texto1 = new Paragraph("Nome: " + txtAdv.getText() + ".", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto1.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                            Contrato.add(texto1);

                            Paragraph texto2 = new Paragraph("Portador(a) do CPF N. " + txtAdv1.getText() + ".", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto2.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                            Contrato.add(texto2);

                            Paragraph texto3 = new Paragraph("Data do pagamento: " + pgto + ".", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto3.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                            Contrato.add(texto3);

                            Paragraph texto4 = new Paragraph("Valor do honorários: R$" + txtAdvHon.getText() + ".", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto4.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                            Contrato.add(texto4);

                            Paragraph texto5 = new Paragraph("Valor da produtividade: R$" + txtAdvProd.getText() + ".", FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK));
                            texto5.setAlignment(com.itextpdf.text.Element.ALIGN_JUSTIFIED);
                            Contrato.add(texto5);

                            Contrato.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));

                            Contrato.add(new Paragraph(exibir_data.getText(), FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.PLAIN, BaseColor.BLACK)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));
                            Contrato.add(new Paragraph("______________________", FontFactory.getFont(FontFactory.TIMES, 18, Font.PLAIN, BaseColor.WHITE)));

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
            }
            Update_users();

            numClientes();
        }
        txtAdv.setText("");
        txtAdv1.setText("");
        txtAdvHon.setText("");
        dtPagamento.setDate(null);
        txtAdvProd.setText("");


    }//GEN-LAST:event_btn_Salvar_PgtActionPerformed


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
            btn_log.setBackground(obj.getColor());
            pesquisar.setBackground(obj.getColor());
            btn_Pagamento.setBackground(obj.getColor());
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
            this.btn_log.setSelected(false);
            this.btn_Pagamento.setSelected(false);
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
            this.btn_log.setSelected(false);
            this.btn_Pagamento.setSelected(false);
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
            this.btn_log.setSelected(false);
            this.btn_Pagamento.setSelected(false);
            this.btn_configuracao.setSelected(false);
            this.btn_sobre.setSelected(false);

            rSPanelsSlider1.slidPanel(20, Pnl_Editar, RSPanelsSlider.direct.Right);
        }
    }//GEN-LAST:event_btn_editarActionPerformed

    private void btn_logActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_logActionPerformed
        if (!this.btn_log.isSelected()) {
            this.btn_home.setSelected(false);
            this.btn_novo.setSelected(false);
            this.btn_editar.setSelected(false);
            this.btn_log.setSelected(true);
            this.btn_Pagamento.setSelected(false);
            this.btn_configuracao.setSelected(false);
            this.btn_sobre.setSelected(false);

            rSPanelsSlider1.slidPanel(20, Pnl_Log, RSPanelsSlider.direct.Right);
        }
    }//GEN-LAST:event_btn_logActionPerformed

    private void btn_PagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PagamentoActionPerformed
        if (!this.btn_Pagamento.isSelected()) {
            this.btn_home.setSelected(false);
            this.btn_novo.setSelected(false);
            this.btn_editar.setSelected(false);
            this.btn_log.setSelected(false);
            this.btn_Pagamento.setSelected(true);
            this.btn_configuracao.setSelected(false);
            this.btn_sobre.setSelected(false);

            rSPanelsSlider1.slidPanel(20, Pnl_Pagamento, RSPanelsSlider.direct.Right);
        }
    }//GEN-LAST:event_btn_PagamentoActionPerformed

    private void btn_configuracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_configuracaoActionPerformed
        if (!this.btn_configuracao.isSelected()) {
            this.btn_home.setSelected(false);
            this.btn_novo.setSelected(false);
            this.btn_editar.setSelected(false);
            this.btn_log.setSelected(false);
            this.btn_Pagamento.setSelected(false);
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
            this.btn_log.setSelected(false);
            this.btn_Pagamento.setSelected(false);
            this.btn_configuracao.setSelected(false);
            this.btn_sobre.setSelected(true);

            rSPanelsSlider1.slidPanel(20, Pnl_Sobre, RSPanelsSlider.direct.Right);
        }
    }//GEN-LAST:event_btn_sobreActionPerformed


    private void btnExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcelActionPerformed

        int finaliza = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja exportar o registro?", "Atenção", JOptionPane.YES_NO_OPTION);

        if (finaliza == JOptionPane.YES_OPTION) {

            ExportarExcel obj;

            try {
                obj = new ExportarExcel();
                obj.exportarExcel(tblAdv);
            } catch (IOException ex) {
                System.out.println("Erro: " + ex);
            }

            JOptionPane.showMessageDialog(null, "Arquivo exportador com sucesso");

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
            conn = ConnectDB.getConnectionCliente();
            String reg = "insert into Log (Usuário, Data,Status) values ('" + val + "','" + value0 + " / " + values + "','Registro exportado')";
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

            conn = ConnectDB.getConnectionCliente();
            String reg = "insert into Log (Usuário, Data,Status) values ('" + value + "','" + value0 + " / " + values + "','Desconectado')";
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

    private void tblAdvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblAdvKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {

        }
    }//GEN-LAST:event_tblAdvKeyReleased

    private void maximizarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maximizarMouseExited
        maximizar.setIcon(new ImageIcon(getClass().getResource("/img/maximizar1.png")));
    }//GEN-LAST:event_maximizarMouseExited

    private void maximizarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maximizarMouseEntered
        maximizar.setIcon(new ImageIcon(getClass().getResource("/img/maximizar.png")));
    }//GEN-LAST:event_maximizarMouseEntered

    private void maximizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maximizarMouseClicked
        if (this.getExtendedState() != Home_admin.MAXIMIZED_BOTH) {
            this.setExtendedState(Home_admin.MAXIMIZED_BOTH);

        } else {
            this.setExtendedState(Home_admin.NORMAL);
        }
    }//GEN-LAST:event_maximizarMouseClicked

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

    private void tblAdvPagamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAdvPagamentoMouseClicked

        DefaultTableModel tmodel = (DefaultTableModel) tblAdvPagamento.getModel();
        int row = tblAdvPagamento.getSelectedRow();
        txtAdv.setText(tmodel.getValueAt(row, 0).toString());
        txtAdv1.setText(tmodel.getValueAt(row, 1).toString());


    }//GEN-LAST:event_tblAdvPagamentoMouseClicked
    int i = 0;
    private void txtAdvProdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdvProdKeyReleased
        char d = evt.getKeyChar();
        if (!Character.isDigit(d)) {
            evt.consume();
        }

    }//GEN-LAST:event_txtAdvProdKeyReleased

    private void txtAdvHonKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdvHonKeyTyped

        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }


    }//GEN-LAST:event_txtAdvHonKeyTyped

    private void txtAdvProdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdvProdKeyTyped

        char d = evt.getKeyChar();
        if (!Character.isDigit(d)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtAdvProdKeyTyped

    private void btn_RelatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RelatActionPerformed

        try {

            int finaliza = JOptionPane.showConfirmDialog(null, "Tem certeza que vizualizar o relatório?", "Atenção", JOptionPane.YES_NO_OPTION);

            if (finaliza == JOptionPane.YES_OPTION) {

                if (!this.btn_Relat.isSelected()) {

                    Update_pgto();
                    rSPanelsSlider1.slidPanel(20, Pnl_Relatorio, RSPanelsSlider.direct.Right);

                } else if (finaliza == JOptionPane.NO_OPTION) {

                    this.setVisible(false);
                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }


    }//GEN-LAST:event_btn_RelatActionPerformed

    private void Tbl_Relat_PgtoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tbl_Relat_PgtoMouseClicked


    }//GEN-LAST:event_Tbl_Relat_PgtoMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int finaliza = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja exportar o registro?", "Atenção", JOptionPane.YES_NO_OPTION);

        if (finaliza == JOptionPane.YES_OPTION) {

            ExportarExcel obj;

            try {
                obj = new ExportarExcel();
                obj.exportarExcel(tblAdv);
            } catch (IOException ex) {
                System.out.println("Erro: " + ex);
            }

            JOptionPane.showMessageDialog(null, "Arquivo exportador com sucesso");

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
            conn = ConnectDB.getConnectionCliente();
            String reg = "insert into Log (Usuário,Data,Status) values ('" + val + "','" + value0 + " / " + values + "','Registro de pagamento exportado')";
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
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtCEP1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCEP1KeyReleased
        String cp = txtCEP.getText();

        cp = cp.replaceAll("\\D*", "");
        int cont = cp.length();

        txtEndereco.setText("Aguarde...");

        if (cont == 8) {
            buscarCep(txtCEP.getText());
        }
    }//GEN-LAST:event_txtCEP1KeyReleased

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home_admin().setVisible(true);
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
    private javax.swing.JPanel Pnl_Atualizar;
    private javax.swing.JPanel Pnl_Configuracao;
    private javax.swing.JPanel Pnl_Editar;
    private javax.swing.JPanel Pnl_Home;
    private javax.swing.JPanel Pnl_Log;
    private javax.swing.JPanel Pnl_Novo;
    private javax.swing.JPanel Pnl_Pagamento;
    private javax.swing.JPanel Pnl_Relatorio;
    private javax.swing.JPanel Pnl_Sobre;
    private javax.swing.JTable Tbl_Relat_Pgto;
    private javax.swing.JLabel Tel1;
    private javax.swing.JLabel Tel3;
    private javax.swing.JLabel Tel4;
    private javax.swing.JLabel Tel5;
    private javax.swing.JLabel Tel6;
    private javax.swing.JLabel Tel7;
    private javax.swing.JLabel Total_Cli_Up;
    private javax.swing.JPanel agenda;
    private javax.swing.JLabel bairro;
    private javax.swing.JLabel bairro1;
    private javax.swing.JLabel balanca;
    private javax.swing.JPanel bgrodape;
    private javax.swing.JButton btnConfCor;
    private javax.swing.JButton btnConfCor1;
    private javax.swing.JButton btnExcluirCliente;
    private javax.swing.JButton btnExportarExcel;
    private javax.swing.JButton btnLimparAdv;
    private javax.swing.JButton btnLimparCli_Update;
    private javax.swing.JButton btnLogof;
    private javax.swing.JButton btnSalvarAdv;
    private javax.swing.JButton btnUpdateCliente;
    private javax.swing.JButton btn_BuscarCEP;
    private javax.swing.JButton btn_Buscar_Update;
    private javax.swing.JButton btn_Editar_Cli;
    private javax.swing.JButton btn_Pagamento;
    private javax.swing.JButton btn_Relat;
    private javax.swing.JButton btn_Salvar_Pgt;
    private javax.swing.JButton btn_configuracao;
    private javax.swing.JButton btn_editar;
    private javax.swing.JButton btn_home;
    private javax.swing.JButton btn_img;
    private javax.swing.JButton btn_img1;
    private javax.swing.JButton btn_log;
    private javax.swing.JButton btn_novo;
    private javax.swing.JButton btn_sobre;
    private javax.swing.JLabel cep;
    private javax.swing.JLabel cep1;
    private javax.swing.JLabel cid;
    private javax.swing.JLabel cid1;
    private javax.swing.JLabel comp;
    private javax.swing.JLabel comp1;
    private javax.swing.JPanel configuracao;
    private com.toedter.calendar.JDateChooser dtPagamento;
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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
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
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lbl_Clientes;
    public static javax.swing.JLabel lbl_TotalClientes;
    public static javax.swing.JLabel lbl_Usuario_BD;
    private javax.swing.JLabel lbl_advogado;
    private javax.swing.JLabel lbl_img;
    private javax.swing.JLabel lbl_img1;
    private javax.swing.JLabel lblhon;
    private javax.swing.JLabel lblhon1;
    private javax.swing.JLabel lblprod;
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
    private javax.swing.JTable tblAdv;
    private static javax.swing.JTable tblAdvPagamento;
    private javax.swing.JTable tblUsusariosLog;
    private javax.swing.JLabel titulo1;
    private javax.swing.JTextField txtAdv;
    private javax.swing.JTextField txtAdv1;
    private javax.swing.JTextField txtAdvHon;
    private javax.swing.JTextField txtAdvProd;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JTextField txtBairro1;
    private javax.swing.JFormattedTextField txtCEP;
    private javax.swing.JFormattedTextField txtCEP1;
    private javax.swing.JFormattedTextField txtCPF_Adv;
    private javax.swing.JFormattedTextField txtCPF_Adv2;
    private javax.swing.JTextField txtCidade;
    private javax.swing.JTextField txtCidade1;
    private javax.swing.JTextField txtComp;
    private javax.swing.JTextField txtComp1;
    private com.toedter.calendar.JDateChooser txtDtnasc_Adv;
    private com.toedter.calendar.JDateChooser txtDtnasc_Adv2;
    private javax.swing.JTextField txtEmail_Adv;
    private javax.swing.JTextField txtEmail_Adv2;
    private javax.swing.JTextField txtEndereco;
    private javax.swing.JTextField txtEndereco1;
    private javax.swing.JComboBox<String> txtEstado;
    private javax.swing.JComboBox<String> txtEstado1;
    private javax.swing.JComboBox<String> txtEstcivil;
    private javax.swing.JComboBox<String> txtEstcivil1;
    private javax.swing.JFormattedTextField txtFone1_Adv;
    private javax.swing.JFormattedTextField txtFone1_Adv2;
    private javax.swing.JTextField txtIDAdv;
    private javax.swing.JTextField txtNomeAdv;
    private javax.swing.JTextField txtNomeAdv1;
    private javax.swing.JTextField txtNum;
    private javax.swing.JTextField txtNum1;
    private javax.swing.JTextField txtOAB;
    private javax.swing.JTextField txtOAB2;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JPasswordField txtPass1;
    private javax.swing.JTextField txtPesquisarLog;
    public static javax.swing.JLabel txtTotalCliBD;
    private javax.swing.JTextField txtUser;
    private javax.swing.JTextField txtUser1;
    // End of variables declaration//GEN-END:variables

    private ImageIcon format = null;
    String filename = null;
    int s = 0;
    byte[] person_image = null;

}
