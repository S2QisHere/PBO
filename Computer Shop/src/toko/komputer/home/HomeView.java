package toko.komputer.home;
import toko.komputer.barang.BarangView;
import toko.komputer.pelanggan.PelangganView;
import toko.komputer.transaksi.TransaksiView;

public class HomeView extends javax.swing.JFrame {

    public HomeView() {
        initComponents();
    }
    
    public HomeView(String status) {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        if(status.equals("kasir")){
            menuData.setVisible(false);
            menuBarangMasuk.setVisible(false);
        }else if(status.equals("gudang")){
            menuData.setVisible(false);
            menuPenjualan.setVisible(false);            
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
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        menuData = new javax.swing.JMenu();
        menuBarang = new javax.swing.JMenuItem();
        menuPelanggan = new javax.swing.JMenuItem();
        menuPemasok = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        menuBarangMasuk = new javax.swing.JMenuItem();
        menuPenjualan = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 102));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 862, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 490, Short.MAX_VALUE)
        );

        jMenuBar1.setMinimumSize(new java.awt.Dimension(0, 0));

        jMenu1.setText("Aplikasi");

        jMenuItem1.setText("Keluar");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        menuData.setText("Data");

        menuBarang.setText("Barang");
        menuBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBarangActionPerformed(evt);
            }
        });
        menuData.add(menuBarang);

        menuPelanggan.setText("Pelanggan");
        menuPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPelangganActionPerformed(evt);
            }
        });
        menuData.add(menuPelanggan);

        menuPemasok.setText("Pemasok");
        menuPemasok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPemasokActionPerformed(evt);
            }
        });
        menuData.add(menuPemasok);

        jMenuBar1.add(menuData);

        jMenu3.setText("Transaksi");

        menuBarangMasuk.setText("Barang Masuk");
        jMenu3.add(menuBarangMasuk);

        menuPenjualan.setText("Penjualan");
        menuPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPenjualanActionPerformed(evt);
            }
        });
        jMenu3.add(menuPenjualan);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Laporan");
        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuPemasokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPemasokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuPemasokActionPerformed

    private void menuBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBarangActionPerformed
        BarangView barangView = new BarangView();
        barangView.setVisible(true);
        dispose();
    }//GEN-LAST:event_menuBarangActionPerformed

    private void menuPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPelangganActionPerformed
        PelangganView pelangganView = new PelangganView();
        pelangganView.setVisible(true);
        dispose();
    }//GEN-LAST:event_menuPelangganActionPerformed

    private void menuPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPenjualanActionPerformed
        TransaksiView transaksiView = new TransaksiView();
        transaksiView.setVisible(true);
        dispose();
    }//GEN-LAST:event_menuPenjualanActionPerformed

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
            java.util.logging.Logger.getLogger(HomeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JMenuItem menuBarang;
    private javax.swing.JMenuItem menuBarangMasuk;
    private javax.swing.JMenu menuData;
    private javax.swing.JMenuItem menuPelanggan;
    private javax.swing.JMenuItem menuPemasok;
    private javax.swing.JMenuItem menuPenjualan;
    // End of variables declaration//GEN-END:variables
}
