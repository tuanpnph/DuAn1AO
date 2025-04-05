package view;

import javax.swing.*;
import java.awt.*;

public class FormHome extends JFrame { 
    public FormHome() {
        setTitle("Quản Lý Bán Áo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Bán Hàng", new FormBanHang()); 
        tabbedPane.addTab("Hóa Đơn", new FormHoaDon());   
        tabbedPane.addTab("Khách Hàng", new FormKhachHang()); 
        tabbedPane.addTab("Nhân Viên", new FormNhanVien());  
        tabbedPane.addTab("Sản Phẩm", new FormSanPham());    

        add(tabbedPane);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormHome().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
