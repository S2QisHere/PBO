package toko.komputer.transaksi;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import toko.komputer.barang.BarangView;
import toko.komputer.barang.CariBarangView;
import toko.komputer.setting.Koneksi;

public class TransaksiView extends javax.swing.JFrame {

    /**
     * Creates new form TransaksiView
     */
    public TransaksiView() {
        initComponents();
        data_pelanggan();
        ulang();
    }

    Connection conn = new Koneksi().getKoneksi();
    PreparedStatement pst;
    ResultSet rs;
    String status, sql;
    DefaultTableModel dtm;
    
    private void nota_otomatis(){
        try {
            sql = " select no_nota from tb_penjualan order by no_nota desc limit 1";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                int kode = Integer.parseInt(rs.getString(1).substring(4))+1;
                textNota.setText("NTA-"+kode);
            }else{
                textNota.setText("NTA-1000");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BarangView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void data_pelanggan(){
        try {
//            comboPelanggan.removeAllItems();
            sql = "select nama_pelanggan from tb_pelanggan";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                comboPelanggan.addItem(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e.toString());
        }
    }
    
    private void ulang(){
        nota_otomatis();
        data_pelanggan();
        textNota.setEnabled(false);
        textIDPelanggan.setEnabled(false);
        textKodeBarang.setEnabled(false);
        textNamaBarang.setEnabled(false);
        textKategori.setEnabled(false);
        textHarga.setEnabled(false);
        textStok.setEnabled(false);
        textTotal.setEnabled(false);
        textIDPelanggan.setText("");
        textKodeBarang.setText("");
        textNamaBarang.setText("");
        textKategori.setText("");
        textHarga.setText("");
        textStok.setText("");
        textQty.setText("");
        textTotal.setText("");
        textBayar.setText("");
        textKembali.setText("");
        textKembali.setEnabled(false);
        tabelItemBelanja.removeAll();
    }
    
    private void hitung_total(){
        BigDecimal total = new BigDecimal(0);
        for(int a=0; a<tabelItemBelanja.getRowCount();a++){
            total = total.add(new BigDecimal(tabelItemBelanja.getValueAt(a, 5).toString()));
        }
        textTotal.setText(total.toString());
    }
    
    private boolean validasi(){
        boolean cek = false;
        java.util.Date tgl = textTanggal.getDate();
        if(tgl==null){
            JOptionPane.showMessageDialog(null, "Tanggal Transaksi belum diisi",null,JOptionPane.ERROR_MESSAGE);
            textTanggal.requestFocus();
        }else if(textIDPelanggan.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Data Pelanggan belum diisi",null,JOptionPane.ERROR_MESSAGE);
            comboPelanggan.requestFocus();
        }else if(tabelItemBelanja.getRowCount()<=0){
            JOptionPane.showMessageDialog(null, "Data Barang belanja masih kosong",null,JOptionPane.ERROR_MESSAGE);
            buttonCariBarang.requestFocus();
        }else if(textBayar.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Textbox Bayar belum diisi",null,JOptionPane.ERROR_MESSAGE);
            textBayar.requestFocus();
        }else if(Integer.parseInt(textBayar.getText())<Integer.parseInt(textTotal.getText())){
            JOptionPane.showMessageDialog(null, "Tidak Melayani hutang",null,JOptionPane.ERROR_MESSAGE);
            textBayar.requestFocus();
    }
        else{
           cek = true; 
        }
        return cek;
    }
    
    private void simpan_transaksi(){
        if(validasi()){
            try {
                java.util.Date d = textTanggal.getDate();
                java.sql.Date tgl = new java.sql.Date(d.getTime());
                pst = conn.prepareStatement("insert into tb_penjualan values (?,?,?,?,?,?)");
                pst.setString(1, textNota.getText());
                pst.setString(2, tgl.toString());
                pst.setString(3, textIDPelanggan.getText());
                pst.setBigDecimal(4, new BigDecimal(textTotal.getText()));
                pst.setBigDecimal(5, new BigDecimal(textBayar.getText()));
                pst.setBigDecimal(6, new BigDecimal(textKembali.getText()));
                int isSucces = pst.executeUpdate();
                if(isSucces == 1){
                    simpan_item_belanja();
                }
                JOptionPane.showMessageDialog(null, "Data Berhasil disimpan!");
                ulang();
            } catch(SQLException ex){
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada Simpan Transaksi: Details \n"+ex.toString());
            }
        }
    }
    
    private void simpan_item_belanja(){
        for(int a = 0; a<= tabelItemBelanja.getRowCount()-1;a++){
            try {
                pst = conn.prepareStatement("insert into tb_detail_penjualan(no_nota,kode_barang,qty) values(?,?,?)");
                String kode; int jumlah;
                kode = tabelItemBelanja.getValueAt(a, 0).toString();
                jumlah = Integer.parseInt(tabelItemBelanja.getValueAt(a, 4).toString());
                pst.setString(1, textNota.getText());
                pst.setString(2, kode);
                pst.setInt(3, jumlah);
                pst.executeUpdate();
                update_stok(kode,jumlah);
            } catch (SQLException ex){
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada Simpan item belanja: Details \n"+ex.toString());
            }
        }
    }
    
    private void update_stok(String kode, int jumlah){
        try {
            sql = "update tb_barang set stok=stok-? where kode_barang = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, jumlah);
            pst.setString(2, kode);
            pst.executeUpdate();
        } catch(SQLException ex){
            System.out.println("Terjadi kesalahan pada update stok: " + ex.toString());
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        textNota = new javax.swing.JTextField();
        textKodeBarang = new javax.swing.JTextField();
        textNamaBarang = new javax.swing.JTextField();
        textKategori = new javax.swing.JTextField();
        textIDPelanggan = new javax.swing.JTextField();
        textTanggal = new com.toedter.calendar.JDateChooser();
        textHarga = new javax.swing.JTextField();
        textStok = new javax.swing.JTextField();
        textQty = new javax.swing.JTextField();
        buttonCariBarang = new javax.swing.JButton();
        buttonTambah = new javax.swing.JButton();
        comboPelanggan = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelItemBelanja = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        textBayar = new javax.swing.JTextField();
        textTotal = new javax.swing.JTextField();
        textKembali = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        buttonSimpan = new javax.swing.JButton();
        buttonBatal = new javax.swing.JButton();
        buttonHapus = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 102));

        jLabel1.setFont(new java.awt.Font("Adobe Caslon Pro Bold", 1, 24)); // NOI18N
        jLabel1.setText("FORM TRANSAKSI");

        jLabel2.setText("NAMA PELANGGAN");

        jLabel3.setText("NO NOTA");

        jLabel4.setText("KODE BARANG");

        jLabel5.setText("KATEGORI");

        jLabel6.setText("NAMA BARANG");

        jLabel7.setText("TANGGAL TRANSAKSI");

        jLabel8.setText("HARGA");

        jLabel9.setText("STOK");

        jLabel10.setText("JUMLAH BELI");

        buttonCariBarang.setText("CARI");
        buttonCariBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCariBarangActionPerformed(evt);
            }
        });

        buttonTambah.setText("TAMBAH");
        buttonTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTambahActionPerformed(evt);
            }
        });

        comboPelanggan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Pelanggan" }));
        comboPelanggan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboPelangganItemStateChanged(evt);
            }
        });
        comboPelanggan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                comboPelangganMouseClicked(evt);
            }
        });
        comboPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboPelangganActionPerformed(evt);
            }
        });

        tabelItemBelanja.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Barang", "Nama Barang", "Kategori", "Harga", "Stok", "Sub Total"
            }
        ));
        jScrollPane1.setViewportView(tabelItemBelanja);

        jLabel11.setText("TOTAL");

        jLabel12.setText("BAYAR");

        textBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textBayarKeyReleased(evt);
            }
        });

        jLabel13.setText("KEMBALI");

        buttonSimpan.setText("SIMPAN");
        buttonSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSimpanActionPerformed(evt);
            }
        });

        buttonBatal.setText("BATAL");
        buttonBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBatalActionPerformed(evt);
            }
        });

        buttonHapus.setText("HAPUS");
        buttonHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonHapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(textKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(buttonCariBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(textNota, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(comboPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 587, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textIDPelanggan)
                                    .addComponent(textHarga)
                                    .addComponent(textStok)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(textTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(textQty, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(buttonTambah)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(419, 419, 419)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(95, 95, 95)
                                .addComponent(textBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(309, 309, 309))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(105, 105, 105)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(buttonBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buttonSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(textKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1060, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(112, 112, 112)
                                .addComponent(textTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(textNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(textIDPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(textKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonCariBarang)
                    .addComponent(jLabel8))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(textNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(textKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(textTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textKembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(textBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(89, 89, 89))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboPelangganItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboPelangganItemStateChanged
        
    }//GEN-LAST:event_comboPelangganItemStateChanged

    private void comboPelangganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboPelangganMouseClicked

    }//GEN-LAST:event_comboPelangganMouseClicked

    private void buttonCariBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCariBarangActionPerformed
        CariBarangView cbv = new CariBarangView(this, true);
        cbv.setVisible(true);
    }//GEN-LAST:event_buttonCariBarangActionPerformed

    private void buttonTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTambahActionPerformed
        if(textKodeBarang.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"Data Barang Belum diisi");
        }else if(textQty.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"Jumlah beli belum diisi");
        }else if(Integer.parseInt(textQty.getText()) > Integer.parseInt(textStok.getText())){
            JOptionPane.showMessageDialog(null,"Stok barang tidak cukup");
            textQty.setText("0");
            textQty.requestFocus();
        }else if(Integer.parseInt(textQty.getText())<=0){
           JOptionPane.showMessageDialog(null,"Jumlah beli tidak boleh dibawah nol!");
            textQty.setText("0");
            textQty.requestFocus();
        }else{
            DefaultTableModel dtm = (DefaultTableModel) tabelItemBelanja.getModel();
            ArrayList list = new ArrayList();
            list.add(textKodeBarang.getText());
            list.add(textNamaBarang.getText());
            list.add(textKategori.getText());
            list.add(textHarga.getText());
            list.add(textQty.getText());
            list.add(Integer.parseInt(textHarga.getText()) * Integer.parseInt(textQty.getText()));
            dtm.addRow(list.toArray());
            textKodeBarang.setText("");
            textNamaBarang.setText("");
            textKategori.setText("");
            textHarga.setText("");
            textStok.setText("");
            textQty.setText("");
            hitung_total();
        }
    }//GEN-LAST:event_buttonTambahActionPerformed

    private void buttonHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonHapusActionPerformed
        int row = tabelItemBelanja.getSelectedRow();
        if(row <0){
            JOptionPane.showMessageDialog(null, "Pilih dulu item yang ini di hapus!");
        }else{
            dtm.removeRow(row);
            tabelItemBelanja.setModel(dtm);
            hitung_total();
        }
    }//GEN-LAST:event_buttonHapusActionPerformed

    private void textBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBayarKeyReleased
        BigDecimal bayar = new BigDecimal(0);
        if(!textBayar.getText().equals("")){
            bayar = new BigDecimal(textBayar.getText());
        }
        BigDecimal total = new BigDecimal(textTotal.getText());
        BigDecimal kembali = bayar.subtract(total);
        textKembali.setText(kembali.toString());
        
    }//GEN-LAST:event_textBayarKeyReleased

    private void buttonSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSimpanActionPerformed
        simpan_transaksi();
    }//GEN-LAST:event_buttonSimpanActionPerformed

    private void buttonBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBatalActionPerformed
        ulang();
    }//GEN-LAST:event_buttonBatalActionPerformed

    private void comboPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboPelangganActionPerformed
        try {
            pst = conn.prepareStatement("select id_pelanggan from tb_pelanggan where nama_pelanggan =?");
            pst.setString(1, comboPelanggan.getSelectedItem().toString());
            rs = pst.executeQuery();
            if(rs.next()){
                textIDPelanggan.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }//GEN-LAST:event_comboPelangganActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TransaksiView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransaksiView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransaksiView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransaksiView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TransaksiView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonBatal;
    private javax.swing.JButton buttonCariBarang;
    private javax.swing.JButton buttonHapus;
    private javax.swing.JButton buttonSimpan;
    private javax.swing.JButton buttonTambah;
    private javax.swing.JComboBox<String> comboPelanggan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable tabelItemBelanja;
    private javax.swing.JTextField textBayar;
    public static javax.swing.JTextField textHarga;
    private javax.swing.JTextField textIDPelanggan;
    public static javax.swing.JTextField textKategori;
    private javax.swing.JTextField textKembali;
    public static javax.swing.JTextField textKodeBarang;
    public static javax.swing.JTextField textNamaBarang;
    private javax.swing.JTextField textNota;
    private javax.swing.JTextField textQty;
    public static javax.swing.JTextField textStok;
    private com.toedter.calendar.JDateChooser textTanggal;
    private javax.swing.JTextField textTotal;
    // End of variables declaration//GEN-END:variables
}
