package view;

import entity.SanPham;
import entity.SanPhamChiTiet;
import repository.SanPhamRepository;
import repository.SanPhamChiTietRepository;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import util.DBConnection;

public class FormSanPham extends javax.swing.JPanel {

    private DefaultTableModel tableModelSanPham;
    private DefaultTableModel tableModelSanPhamChiTiet;
    private SanPhamRepository sanPhamRepo;
    private SanPhamChiTietRepository sanPhamChiTietRepo;
    private DefaultTableModel tableModelMauSac;
    private DefaultTableModel tableModelKichThuoc;
    private DefaultTableModel tableModelChatLieu;

    public FormSanPham() {
        initComponents();
        sanPhamRepo = new SanPhamRepository();
        sanPhamChiTietRepo = new SanPhamChiTietRepository();
        tableModelSanPham = (DefaultTableModel) tblSanPham.getModel();
        tableModelSanPhamChiTiet = (DefaultTableModel) tblSanPhamChiTiet.getModel();
        tableModelMauSac = (DefaultTableModel) tblMauSac.getModel();
        tableModelKichThuoc = (DefaultTableModel) tblKichThuoc.getModel();
        tableModelChatLieu = (DefaultTableModel) tblChatLieu.getModel();
        loadDataSanPham();
        loadDataSanPhamChiTiet();
        loadComboBoxData();
        loadDataThuocTinh();
    }

    private void clearFormThuocTinh() {
        txtMaMauSac.setText("");
        txtTenMauSac.setText("");
        txtMaKichThuoc.setText("");
        txtTenKichThuoc.setText("");
        txtMaChatLieu.setText("");
        txtTenChatLieu.setText("");
    }

    private void loadDataThuocTinh() {
        tableModelMauSac.setRowCount(0);
        String sqlMauSac = "SELECT id_mau, ma_mau, ten_mau FROM MauSac";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlMauSac); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tableModelMauSac.addRow(new Object[]{
                    rs.getInt("id_mau"),
                    rs.getString("ma_mau"),
                    rs.getString("ten_mau")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableModelKichThuoc.setRowCount(0);
        String sqlKichThuoc = "SELECT id_size, ma_size, ten_size FROM Size";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlKichThuoc); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tableModelKichThuoc.addRow(new Object[]{
                    rs.getInt("id_size"),
                    rs.getString("ma_size"),
                    rs.getString("ten_size")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableModelChatLieu.setRowCount(0);
        String sqlChatLieu = "SELECT id_chat_lieu, ma_chat_lieu, ten_chat_lieu FROM ChatLieu";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlChatLieu); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tableModelChatLieu.addRow(new Object[]{
                    rs.getInt("id_chat_lieu"),
                    rs.getString("ma_chat_lieu"),
                    rs.getString("ten_chat_lieu")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadComboBoxData() {
        // Load dữ liệu cho cbbMauSac
        cbbMauSac.removeAllItems();
        String sqlMauSac = "SELECT ten_mau FROM MauSac";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlMauSac); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cbbMauSac.addItem(rs.getString("ten_mau"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cbbKichThuoc.removeAllItems();
        String sqlKichThuoc = "SELECT ten_size FROM Size";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlKichThuoc); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cbbKichThuoc.addItem(rs.getString("ten_size"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cbbChatLieu.removeAllItems();
        String sqlChatLieu = "SELECT ten_chat_lieu FROM ChatLieu";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlChatLieu); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cbbChatLieu.addItem(rs.getString("ten_chat_lieu"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cbbTenSp.removeAllItems();
        String sqlSanPham = "SELECT ten_san_pham FROM SanPham";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlSanPham); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cbbTenSp.addItem(rs.getString("ten_san_pham"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDataSanPham() {
        List<SanPham> list = sanPhamRepo.getAllSanPham();
        tableModelSanPham.setRowCount(0);
        for (SanPham sp : list) {
            String trangThai = sp.getTrangThai() == 1 ? "Đang bán" : "Ngừng bán";
            tableModelSanPham.addRow(new Object[]{sp.getIdSanPham(), sp.getMaSanPham(), sp.getTenSanPham(), trangThai});
        }
    }

    private void loadDataSanPhamChiTiet() {
        List<SanPhamChiTiet> list = sanPhamChiTietRepo.getAllSanPhamChiTiet();
        tableModelSanPhamChiTiet.setRowCount(0);
        for (SanPhamChiTiet spct : list) {
            String trangThai = spct.getTrangThai() == 1 ? "Đang bán" : "Ngừng bán";
            tableModelSanPhamChiTiet.addRow(new Object[]{
                spct.getIdSpct(), spct.getTenSanPham(), spct.getMauSac(), spct.getKichThuoc(),
                spct.getChatLieu(), spct.getSoLuong(), spct.getGiaBan(), trangThai
            });
        }
    }

    private void themSanPham() {
        if (!validateInputSanPham()) {
            return;
        }
        SanPham sp = new SanPham();
        sp.setMaSanPham(txtMaSP.getText().trim());
        sp.setTenSanPham(txtTenSP.getText().trim());
        sp.setTrangThai(rdoDangBanSP.isSelected() ? 1 : 0);
        if (sanPhamRepo.addSanPham(sp)) {
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
            loadDataSanPham();
            loadComboBoxData();
            resetFormSanPham();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại!");
        }
    }

    private void suaSanPham() {
        if (txtIDSanPham.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để sửa!");
            return;
        }
        if (!validateInputSanPham()) {
            return;
        }
        SanPham sp = new SanPham();
        sp.setIdSanPham(Integer.parseInt(txtIDSanPham.getText()));
        sp.setMaSanPham(txtMaSP.getText().trim());
        sp.setTenSanPham(txtTenSP.getText().trim());
        sp.setTrangThai(rdoDangBanSP.isSelected() ? 1 : 0);
        if (sanPhamRepo.updateSanPham(sp)) {
            JOptionPane.showMessageDialog(this, "Sửa sản phẩm thành công!");
            loadDataSanPham();
            loadComboBoxData();
            resetFormSanPham();
        } else {
            JOptionPane.showMessageDialog(this, "Sửa sản phẩm thất bại!");
        }
    }

    private void xoaSanPham() {
        if (txtIDSanPham.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa!");
            return;
        }
        int id = Integer.parseInt(txtIDSanPham.getText());
        String sqlCheck = "SELECT COUNT(*) FROM SanPhamChiTiet WHERE id_san_pham = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Không thể xóa sản phẩm vì có chi tiết sản phẩm liên quan!");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sqlDelete = "DELETE FROM SanPham WHERE id_san_pham = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlDelete)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
                loadDataSanPham();
                loadComboBoxData();
                resetFormSanPham();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void themSanPhamChiTiet() {
        if (!validateInputSanPhamChiTiet()) {
            return;
        }
        SanPhamChiTiet spct = new SanPhamChiTiet();
        String tenSanPham = cbbTenSp.getSelectedItem().toString();
        int idSanPham = sanPhamRepo.getIdByTenSanPham(tenSanPham);
        if (idSanPham == -1) {
            JOptionPane.showMessageDialog(this, "Sản phẩm không tồn tại! Vui lòng thêm sản phẩm trước.");
            return;
        }
        spct.setIdSanPham(idSanPham);
        spct.setSoLuong(Integer.parseInt(txtSoLuong.getText().trim()));
        spct.setGiaBan(Double.parseDouble(txtGiaBan.getText().trim()));
        spct.setTrangThai(rdoDangBanSPCT.isSelected() ? 1 : 0);

        int idMau = sanPhamChiTietRepo.getIdByTenMau(cbbMauSac.getSelectedItem().toString());
        int idSize = sanPhamChiTietRepo.getIdByTenSize(cbbKichThuoc.getSelectedItem().toString());
        int idChatLieu = sanPhamChiTietRepo.getIdByTenChatLieu(cbbChatLieu.getSelectedItem().toString());

        if (sanPhamChiTietRepo.addSanPhamChiTiet(spct, idMau, idSize, idChatLieu)) {
            JOptionPane.showMessageDialog(this, "Thêm chi tiết sản phẩm thành công!");
            loadDataSanPhamChiTiet();
            resetFormSanPhamChiTiet();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm chi tiết sản phẩm thất bại!");
        }
    }

    private void suaSanPhamChiTiet() {
        if (txtIDSPCT.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết sản phẩm để sửa!");
            return;
        }
        if (!validateInputSanPhamChiTiet()) {
            return;
        }
        SanPhamChiTiet spct = new SanPhamChiTiet();
        spct.setIdSpct(Integer.parseInt(txtIDSPCT.getText()));

        String tenSanPham = cbbTenSp.getSelectedItem().toString(); 
        int idSanPham = sanPhamRepo.getIdByTenSanPham(tenSanPham);
        if (idSanPham == -1) {
            JOptionPane.showMessageDialog(this, "Sản phẩm không tồn tại! Vui lòng thêm sản phẩm trước.");
            return;
        }
        spct.setIdSanPham(idSanPham);
        spct.setSoLuong(Integer.parseInt(txtSoLuong.getText().trim()));
        spct.setGiaBan(Double.parseDouble(txtGiaBan.getText().trim()));
        spct.setTrangThai(rdoDangBanSPCT.isSelected() ? 1 : 0);

        int idMau = sanPhamChiTietRepo.getIdByTenMau(cbbMauSac.getSelectedItem().toString());
        int idSize = sanPhamChiTietRepo.getIdByTenSize(cbbKichThuoc.getSelectedItem().toString());
        int idChatLieu = sanPhamChiTietRepo.getIdByTenChatLieu(cbbChatLieu.getSelectedItem().toString());

        if (sanPhamChiTietRepo.updateSanPhamChiTiet(spct, idMau, idSize, idChatLieu)) {
            JOptionPane.showMessageDialog(this, "Sửa chi tiết sản phẩm thành công!");
            loadDataSanPhamChiTiet();
            resetFormSanPhamChiTiet();
        } else {
            JOptionPane.showMessageDialog(this, "Sửa chi tiết sản phẩm thất bại!");
        }
    }

    private void xoaSanPhamChiTiet() {
        if (txtIDSPCT.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết sản phẩm để xóa!");
            return;
        }
        int idSpct = Integer.parseInt(txtIDSPCT.getText());
        if (sanPhamChiTietRepo.deleteSanPhamChiTiet(idSpct)) {
            JOptionPane.showMessageDialog(this, "Xóa chi tiết sản phẩm thành công!");
            loadDataSanPhamChiTiet();
            resetFormSanPhamChiTiet();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa chi tiết sản phẩm thất bại!");
        }
    }

    private void resetFormSanPham() {
        txtIDSanPham.setText("");
        txtMaSP.setText("");
        txtTenSP.setText("");
        rdoDangBanSP.setSelected(false);
        rdoNgungBanSP.setSelected(false);
    }

    private void resetFormSanPhamChiTiet() {
        txtIDSPCT.setText("");
        cbbTenSp.setSelectedIndex(0); 
        cbbMauSac.setSelectedIndex(0);
        cbbKichThuoc.setSelectedIndex(0);
        cbbChatLieu.setSelectedIndex(0);
        txtSoLuong.setText("");
        txtGiaBan.setText("");
        rdoDangBanSPCT.setSelected(false);
        rdoNgungBanSPCT.setSelected(false);
    }

    private boolean validateInputSanPham() {
        if (txtMaSP.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm không được để trống!");
            txtMaSP.requestFocus();
            return false;
        }
        if (txtTenSP.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống!");
            txtTenSP.requestFocus();
            return false;
        }
        if (!rdoDangBanSP.isSelected() && !rdoNgungBanSP.isSelected()) { 
            JOptionPane.showMessageDialog(this, "Vui lòng chọn trạng thái!");
            return false;
        }
        return true;
    }

    private boolean validateInputSanPhamChiTiet() {
        if (cbbTenSp.getSelectedItem() == null) {   
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!");
            cbbTenSp.requestFocus();
            return false;
        }
        if (txtSoLuong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số lượng không được để trống!");
            txtSoLuong.requestFocus();
            return false;
        }
        if (txtGiaBan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giá bán không được để trống!");
            txtGiaBan.requestFocus();
            return false;
        }
        try {
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
                txtSoLuong.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!");
            txtSoLuong.requestFocus();
            return false;
        }
        try {
            double giaBan = Double.parseDouble(txtGiaBan.getText().trim());
            if (giaBan <= 0) {
                JOptionPane.showMessageDialog(this, "Giá bán phải lớn hơn 0!");
                txtGiaBan.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá bán phải là số!");
            txtGiaBan.requestFocus();
            return false;
        }
        if (!rdoDangBanSPCT.isSelected() && !rdoNgungBanSPCT.isSelected()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn trạng thái!");
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        btnThemSanPham = new javax.swing.JButton();
        btnSuaSanPham = new javax.swing.JButton();
        txtMaSP = new javax.swing.JTextField();
        txtTenSP = new javax.swing.JTextField();
        rdoDangBanSP = new javax.swing.JRadioButton();
        rdoNgungBanSP = new javax.swing.JRadioButton();
        lblTrangThai = new javax.swing.JLabel();
        lblTenSP = new javax.swing.JLabel();
        lblMaSP = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        txtIDSanPham = new javax.swing.JTextField();
        lblIDSP = new javax.swing.JLabel();
        btnXoaSanPham = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblTenSp = new javax.swing.JLabel();
        lblMauSac = new javax.swing.JLabel();
        lblKichThuoc = new javax.swing.JLabel();
        lblGiaBan = new javax.swing.JLabel();
        lblSoLuong = new javax.swing.JLabel();
        cbbMauSac = new javax.swing.JComboBox<>();
        cbbKichThuoc = new javax.swing.JComboBox<>();
        txtSoLuong = new javax.swing.JTextField();
        txtGiaBan = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSanPhamChiTiet = new javax.swing.JTable();
        btnSuaCTSP = new javax.swing.JButton();
        lblIDSPCT = new javax.swing.JLabel();
        txtIDSPCT = new javax.swing.JTextField();
        cbbChatLieu = new javax.swing.JComboBox<>();
        lblChatLieu = new javax.swing.JLabel();
        lblTrangThai2 = new javax.swing.JLabel();
        rdoDangBanSPCT = new javax.swing.JRadioButton();
        rdoNgungBanSPCT = new javax.swing.JRadioButton();
        btnXoaCTSP = new javax.swing.JButton();
        btnThemCTSP = new javax.swing.JButton();
        cbbTenSp = new javax.swing.JComboBox<>();
        ThuocTinhPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtTenMauSac = new javax.swing.JTextField();
        txtTenKichThuoc = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTenChatLieu = new javax.swing.JTextField();
        btnThemMauSac = new javax.swing.JButton();
        btnSuaMauSac = new javax.swing.JButton();
        btnXoaMauSac = new javax.swing.JButton();
        btnThemKichThuoc = new javax.swing.JButton();
        btnSuaKichThuoc = new javax.swing.JButton();
        btnXoaKichThuoc = new javax.swing.JButton();
        btnThemChatLieu = new javax.swing.JButton();
        btnSuaChatLieu = new javax.swing.JButton();
        btnXoaChatLieu = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtMaMauSac = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtMaKichThuoc = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtMaChatLieu = new javax.swing.JTextField();
        btnResetThuocTinh = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblMauSac = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblKichThuoc = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblChatLieu = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Quản lý sản phẩm");

        btnThemSanPham.setText("Thêm");
        btnThemSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSanPhamActionPerformed(evt);
            }
        });

        btnSuaSanPham.setText("Sửa ");
        btnSuaSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaSanPhamActionPerformed(evt);
            }
        });

        txtTenSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenSPActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoDangBanSP);
        rdoDangBanSP.setText("Đang bán");

        buttonGroup1.add(rdoNgungBanSP);
        rdoNgungBanSP.setText("Ngừng bán");

        lblTrangThai.setText("Trạng thái");

        lblTenSP.setText("Tên sản phẩm");

        lblMaSP.setText("Mã sản phẩm");

        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Mã sản phẩm", "Tên sản phẩm", "Trạng thái"
            }
        ));
        tblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSanPham);

        txtIDSanPham.setEnabled(false);
        txtIDSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDSanPhamActionPerformed(evt);
            }
        });

        lblIDSP.setText("ID sản phẩm");

        btnXoaSanPham.setText("Xóa ");
        btnXoaSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSanPhamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblTenSP)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblIDSP)
                            .addComponent(lblMaSP)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lblTrangThai)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rdoDangBanSP)
                        .addGap(54, 54, 54)
                        .addComponent(rdoNgungBanSP)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIDSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnThemSanPham, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnSuaSanPham, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnXoaSanPham, javax.swing.GroupLayout.Alignment.TRAILING)))))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1010, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIDSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIDSP)
                    .addComponent(btnThemSanPham))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaSP)
                    .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSuaSanPham))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTenSP)
                    .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoaSanPham))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTrangThai)
                    .addComponent(rdoDangBanSP)
                    .addComponent(rdoNgungBanSP))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quản lý sản phẩm", jPanel1);

        lblTenSp.setText("Sản phẩm");

        lblMauSac.setText("Màu sắc");

        lblKichThuoc.setText("Kích thước");

        lblGiaBan.setText("Giá bán");

        lblSoLuong.setText("Số lượng");

        cbbMauSac.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbbKichThuoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tblSanPhamChiTiet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID SPCT", "Sản phẩm", "Màu sắc", "Kích thước", "Chất liệu", "Số lượng", "Giá bán", "Trạng thái"
            }
        ));
        tblSanPhamChiTiet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamChiTietMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblSanPhamChiTiet);

        btnSuaCTSP.setText("Sửa ");
        btnSuaCTSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaCTSPActionPerformed(evt);
            }
        });

        lblIDSPCT.setText("ID SPCT");

        txtIDSPCT.setEnabled(false);

        cbbChatLieu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblChatLieu.setText("Chất liệu");

        lblTrangThai2.setText("Trạng thái");

        buttonGroup2.add(rdoDangBanSPCT);
        rdoDangBanSPCT.setText("Đang bán");

        buttonGroup2.add(rdoNgungBanSPCT);
        rdoNgungBanSPCT.setText("Ngừng bán");

        btnXoaCTSP.setText("Xóa");
        btnXoaCTSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaCTSPActionPerformed(evt);
            }
        });

        btnThemCTSP.setText("Thêm");
        btnThemCTSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemCTSPActionPerformed(evt);
            }
        });

        cbbTenSp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTenSp)
                    .addComponent(lblIDSPCT)
                    .addComponent(lblMauSac)
                    .addComponent(lblKichThuoc)
                    .addComponent(lblChatLieu))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbbKichThuoc, 0, 182, Short.MAX_VALUE)
                    .addComponent(cbbMauSac, 0, 182, Short.MAX_VALUE)
                    .addComponent(txtIDSPCT, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addComponent(cbbChatLieu, 0, 182, Short.MAX_VALUE)
                    .addComponent(cbbTenSp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(70, 70, 70)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSoLuong)
                    .addComponent(lblGiaBan)
                    .addComponent(lblTrangThai2))
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(rdoDangBanSPCT)
                        .addGap(27, 27, 27)
                        .addComponent(rdoNgungBanSPCT)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 282, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThemCTSP, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSuaCTSP, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnXoaCTSP, javax.swing.GroupLayout.Alignment.TRAILING)))
            .addComponent(jScrollPane2)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(btnThemCTSP))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIDSPCT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblIDSPCT)
                            .addComponent(lblSoLuong)
                            .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblGiaBan)
                            .addComponent(txtGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTrangThai2)
                            .addComponent(rdoDangBanSPCT)
                            .addComponent(rdoNgungBanSPCT)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTenSp)
                            .addComponent(cbbTenSp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbbMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMauSac))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblKichThuoc)
                            .addComponent(cbbKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbbChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblChatLieu)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSuaCTSP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoaCTSP)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(218, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Chi tiết sản phẩm", jPanel2);

        jLabel2.setText("Tên màu sắc");

        jLabel3.setText("Tên kích thước");

        jLabel4.setText("Tên chất liệu");

        btnThemMauSac.setText("Thêm");
        btnThemMauSac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemMauSacActionPerformed(evt);
            }
        });

        btnSuaMauSac.setText("Sửa");
        btnSuaMauSac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaMauSacActionPerformed(evt);
            }
        });

        btnXoaMauSac.setText("Xóa");
        btnXoaMauSac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaMauSacActionPerformed(evt);
            }
        });

        btnThemKichThuoc.setText("Thêm");
        btnThemKichThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemKichThuocActionPerformed(evt);
            }
        });

        btnSuaKichThuoc.setText("Sửa");
        btnSuaKichThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaKichThuocActionPerformed(evt);
            }
        });

        btnXoaKichThuoc.setText("Xóa");
        btnXoaKichThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaKichThuocActionPerformed(evt);
            }
        });

        btnThemChatLieu.setText("Thêm");
        btnThemChatLieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemChatLieuActionPerformed(evt);
            }
        });

        btnSuaChatLieu.setText("Sửa");
        btnSuaChatLieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaChatLieuActionPerformed(evt);
            }
        });

        btnXoaChatLieu.setText("Xóa");
        btnXoaChatLieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaChatLieuActionPerformed(evt);
            }
        });

        jLabel5.setText("Mã màu sắc");

        txtMaMauSac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaMauSacActionPerformed(evt);
            }
        });

        jLabel6.setText("Mã kích thước");

        jLabel7.setText("Mã chất liệu");

        btnResetThuocTinh.setText("Clear");
        btnResetThuocTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetThuocTinhActionPerformed(evt);
            }
        });

        tblMauSac.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblMauSac.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id màu", "Mã màu", "Tên màu"
            }
        ));
        tblMauSac.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMauSacMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblMauSac);

        tblKichThuoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id Kích thước", "Mã kích thước", "Tên kích thước"
            }
        ));
        tblKichThuoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKichThuocMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblKichThuocMouseEntered(evt);
            }
        });
        jScrollPane4.setViewportView(tblKichThuoc);

        tblChatLieu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id chất liệu", "Mã chất liệu", "Tên chất liệu"
            }
        ));
        tblChatLieu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChatLieuMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblChatLieu);

        javax.swing.GroupLayout ThuocTinhPanelLayout = new javax.swing.GroupLayout(ThuocTinhPanel);
        ThuocTinhPanel.setLayout(ThuocTinhPanelLayout);
        ThuocTinhPanelLayout.setHorizontalGroup(
            ThuocTinhPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThuocTinhPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ThuocTinhPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(ThuocTinhPanelLayout.createSequentialGroup()
                        .addGroup(ThuocTinhPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(ThuocTinhPanelLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                                .addComponent(txtMaMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ThuocTinhPanelLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtMaChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ThuocTinhPanelLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                .addComponent(txtMaKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(ThuocTinhPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(30, 30, 30)
                        .addGroup(ThuocTinhPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTenKichThuoc)
                            .addComponent(txtTenMauSac)
                            .addComponent(txtTenChatLieu, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                        .addGap(113, 113, 113)
                        .addGroup(ThuocTinhPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnThemMauSac)
                            .addComponent(btnThemKichThuoc)
                            .addComponent(btnThemChatLieu))
                        .addGap(38, 38, 38)
                        .addGroup(ThuocTinhPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnResetThuocTinh)
                            .addGroup(ThuocTinhPanelLayout.createSequentialGroup()
                                .addGroup(ThuocTinhPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnSuaMauSac)
                                    .addComponent(btnSuaKichThuoc)
                                    .addComponent(btnSuaChatLieu))
                                .addGap(50, 50, 50)
                                .addGroup(ThuocTinhPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnXoaMauSac)
                                    .addComponent(btnXoaKichThuoc)
                                    .addComponent(btnXoaChatLieu)))))
                    .addGroup(ThuocTinhPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        ThuocTinhPanelLayout.setVerticalGroup(
            ThuocTinhPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThuocTinhPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(ThuocTinhPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTenMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemMauSac)
                    .addComponent(btnSuaMauSac)
                    .addComponent(btnXoaMauSac)
                    .addComponent(jLabel5)
                    .addComponent(txtMaMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(ThuocTinhPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTenKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemKichThuoc)
                    .addComponent(btnSuaKichThuoc)
                    .addComponent(btnXoaKichThuoc)
                    .addComponent(jLabel6)
                    .addComponent(txtMaKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ThuocTinhPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtTenChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemChatLieu)
                    .addComponent(btnSuaChatLieu)
                    .addComponent(btnXoaChatLieu)
                    .addComponent(jLabel7)
                    .addComponent(txtMaChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(btnResetThuocTinh)
                .addGap(18, 18, 18)
                .addGroup(ThuocTinhPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Thuộc tính", ThuocTinhPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(244, 244, 244)
                .addComponent(jLabel1)
                .addContainerGap(571, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addComponent(jTabbedPane1))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSuaCTSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaCTSPActionPerformed
        suaSanPhamChiTiet();
    }//GEN-LAST:event_btnSuaCTSPActionPerformed

    private void tblSanPhamChiTietMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamChiTietMouseClicked
        int row = tblSanPhamChiTiet.getSelectedRow();
        if (row >= 0) {
            txtIDSPCT.setText(tableModelSanPhamChiTiet.getValueAt(row, 0).toString());
            cbbTenSp.setSelectedItem(tableModelSanPhamChiTiet.getValueAt(row, 1).toString());
            cbbMauSac.setSelectedItem(tableModelSanPhamChiTiet.getValueAt(row, 2).toString());
            cbbKichThuoc.setSelectedItem(tableModelSanPhamChiTiet.getValueAt(row, 3).toString());
            cbbChatLieu.setSelectedItem(tableModelSanPhamChiTiet.getValueAt(row, 4).toString());
            txtSoLuong.setText(tableModelSanPhamChiTiet.getValueAt(row, 5).toString());
            txtGiaBan.setText(tableModelSanPhamChiTiet.getValueAt(row, 6).toString());
            String trangThai = tableModelSanPhamChiTiet.getValueAt(row, 7).toString();
            if (trangThai.equalsIgnoreCase("Đang bán")) {
                rdoDangBanSPCT.setSelected(true);
                rdoNgungBanSPCT.setSelected(false);
            } else {
                rdoNgungBanSPCT.setSelected(true);
                rdoDangBanSPCT.setSelected(false);
            }
        }
    }//GEN-LAST:event_tblSanPhamChiTietMouseClicked

    private void txtIDSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDSanPhamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDSanPhamActionPerformed

    private void tblSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamMouseClicked
        int row = tblSanPham.getSelectedRow();
        if (row >= 0) {
            txtIDSanPham.setText(tableModelSanPham.getValueAt(row, 0).toString());
            txtMaSP.setText(tableModelSanPham.getValueAt(row, 1).toString());
            txtTenSP.setText(tableModelSanPham.getValueAt(row, 2).toString());
            String trangThai = tableModelSanPham.getValueAt(row, 3).toString();
            if (trangThai.equals("Đang bán")) {
                rdoDangBanSP.setSelected(true);
            } else {
                rdoNgungBanSP.setSelected(true);
            }
        }
    }//GEN-LAST:event_tblSanPhamMouseClicked

    private void btnSuaSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaSanPhamActionPerformed
        suaSanPham();
    }//GEN-LAST:event_btnSuaSanPhamActionPerformed

    private void btnThemSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSanPhamActionPerformed
        themSanPham();
    }//GEN-LAST:event_btnThemSanPhamActionPerformed

    private void btnXoaSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSanPhamActionPerformed
        // TODO add your handling code here:
        xoaSanPham();
    }//GEN-LAST:event_btnXoaSanPhamActionPerformed

    private void btnXoaCTSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaCTSPActionPerformed
        xoaSanPhamChiTiet();        // TODO add your handling code here:
    }//GEN-LAST:event_btnXoaCTSPActionPerformed

    private void txtTenSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenSPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenSPActionPerformed

    private void btnThemCTSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemCTSPActionPerformed
        // TODO add your handling code here:
        themSanPhamChiTiet();
    }//GEN-LAST:event_btnThemCTSPActionPerformed

    private void txtMaMauSacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaMauSacActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaMauSacActionPerformed

    private void btnResetThuocTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetThuocTinhActionPerformed
        // TODO add your handling code here:
        clearFormThuocTinh();
    }//GEN-LAST:event_btnResetThuocTinhActionPerformed

    private void btnThemMauSacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemMauSacActionPerformed
        // TODO add your handling code here:
        String maMau = txtMaMauSac.getText().trim();
        String tenMau = txtTenMauSac.getText().trim();
        if (maMau.isEmpty() || tenMau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và tên màu sắc!");
            return;
        }
        String sql = "INSERT INTO MauSac (ma_mau, ten_mau) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMau);
            ps.setString(2, tenMau);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Thêm màu sắc thành công!");
                loadDataThuocTinh();
                loadComboBoxData(); 
                clearFormThuocTinh();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Thêm màu sắc thất bại! Mã màu có thể đã tồn tại.");
        }
    }//GEN-LAST:event_btnThemMauSacActionPerformed

    private void btnSuaMauSacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaMauSacActionPerformed
        // TODO add your handling code here:
        int row = tblMauSac.getSelectedRow();
        if (row >= 0) {
            String maMau = txtMaMauSac.getText().trim();
            String tenMau = txtTenMauSac.getText().trim();
            if (maMau.isEmpty() || tenMau.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và tên màu sắc!");
                return;
            }
            String sql = "UPDATE MauSac SET ma_mau = ?, ten_mau = ? WHERE id_mau = ?";
            try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, maMau);
                ps.setString(2, tenMau);
                ps.setInt(3, Integer.parseInt(tableModelMauSac.getValueAt(row, 0).toString()));
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Sửa màu sắc thành công!");
                    loadDataThuocTinh();
                    loadComboBoxData();
                    clearFormThuocTinh();
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Sửa màu sắc thất bại!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn màu sắc để sửa!");
        }
    }//GEN-LAST:event_btnSuaMauSacActionPerformed

    private void btnXoaMauSacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaMauSacActionPerformed
        // TODO add your handling code here:
        int row = tblMauSac.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa màu sắc này?");
            if (confirm == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM MauSac WHERE id_mau = ?";
                try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, Integer.parseInt(tableModelMauSac.getValueAt(row, 0).toString()));
                    int rows = ps.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Xóa màu sắc thành công!");
                        loadDataThuocTinh();
                        loadComboBoxData();
                        clearFormThuocTinh();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Xóa màu sắc thất bại! Có thể màu sắc đang được sử dụng.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn màu sắc để xóa!");
        }
    }//GEN-LAST:event_btnXoaMauSacActionPerformed

    private void tblMauSacMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMauSacMouseClicked
        // TODO add your handling code here:
        int row = tblMauSac.getSelectedRow();
        if (row >= 0) {
            txtMaMauSac.setText(tableModelMauSac.getValueAt(row, 1).toString());
            txtTenMauSac.setText(tableModelMauSac.getValueAt(row, 2).toString());
        }
    }//GEN-LAST:event_tblMauSacMouseClicked

    private void btnThemKichThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemKichThuocActionPerformed
        // TODO add your handling code here:
        String maSize = txtMaKichThuoc.getText().trim();
        String tenSize = txtTenKichThuoc.getText().trim();
        if (maSize.isEmpty() || tenSize.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và tên kích thước!");
            return;
        }
        String sql = "INSERT INTO Size (ma_size, ten_size) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSize);
            ps.setString(2, tenSize);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Thêm kích thước thành công!");
                loadDataThuocTinh();
                loadComboBoxData();
                clearFormThuocTinh();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Thêm kích thước thất bại! Mã kích thước có thể đã tồn tại.");
        }
    }//GEN-LAST:event_btnThemKichThuocActionPerformed

    private void btnSuaKichThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaKichThuocActionPerformed
        // TODO add your handling code here:
        int row = tblKichThuoc.getSelectedRow();
        if (row >= 0) {
            String maSize = txtMaKichThuoc.getText().trim();
            String tenSize = txtTenKichThuoc.getText().trim();
            if (maSize.isEmpty() || tenSize.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và tên kích thước!");
                return;
            }
            String sql = "UPDATE Size SET ma_size = ?, ten_size = ? WHERE id_size = ?";
            try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, maSize);
                ps.setString(2, tenSize);
                ps.setInt(3, Integer.parseInt(tableModelKichThuoc.getValueAt(row, 0).toString()));
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Sửa kích thước thành công!");
                    loadDataThuocTinh();
                    loadComboBoxData();
                    clearFormThuocTinh();
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Sửa kích thước thất bại!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn kích thước để sửa!");
        }
    }//GEN-LAST:event_btnSuaKichThuocActionPerformed

    private void btnXoaKichThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaKichThuocActionPerformed
        // TODO add your handling code here:
        int row = tblKichThuoc.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa kích thước này?");
            if (confirm == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM Size WHERE id_size = ?";
                try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, Integer.parseInt(tableModelKichThuoc.getValueAt(row, 0).toString()));
                    int rows = ps.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Xóa kích thước thành công!");
                        loadDataThuocTinh();
                        loadComboBoxData();
                        clearFormThuocTinh();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Xóa kích thước thất bại! Có thể kích thước đang được sử dụng.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn kích thước để xóa!");
        }
    }//GEN-LAST:event_btnXoaKichThuocActionPerformed

    private void tblKichThuocMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKichThuocMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tblKichThuocMouseEntered

    private void tblKichThuocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKichThuocMouseClicked
        // TODO add your handling code here:
        int row = tblKichThuoc.getSelectedRow();
        if (row >= 0) {
            txtMaKichThuoc.setText(tableModelKichThuoc.getValueAt(row, 1).toString());
            txtTenKichThuoc.setText(tableModelKichThuoc.getValueAt(row, 2).toString());
        }
    }//GEN-LAST:event_tblKichThuocMouseClicked

    private void btnThemChatLieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemChatLieuActionPerformed
        // TODO add your handling code here:
        String maChatLieu = txtMaChatLieu.getText().trim();
        String tenChatLieu = txtTenChatLieu.getText().trim();
        if (maChatLieu.isEmpty() || tenChatLieu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và tên chất liệu!");
            return;
        }
        String sql = "INSERT INTO ChatLieu (ma_chat_lieu, ten_chat_lieu) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maChatLieu);
            ps.setString(2, tenChatLieu);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Thêm chất liệu thành công!");
                loadDataThuocTinh();
                loadComboBoxData();
                clearFormThuocTinh();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Thêm chất liệu thất bại! Mã chất liệu có thể đã tồn tại.");
        }
    }//GEN-LAST:event_btnThemChatLieuActionPerformed

    private void btnSuaChatLieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaChatLieuActionPerformed
        // TODO add your handling code here:
        int row = tblChatLieu.getSelectedRow();
        if (row >= 0) {
            String maChatLieu = txtMaChatLieu.getText().trim();
            String tenChatLieu = txtTenChatLieu.getText().trim();
            if (maChatLieu.isEmpty() || tenChatLieu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và tên chất liệu!");
                return;
            }
            String sql = "UPDATE ChatLieu SET ma_chat_lieu = ?, ten_chat_lieu = ? WHERE id_chat_lieu = ?";
            try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, maChatLieu);
                ps.setString(2, tenChatLieu);
                ps.setInt(3, Integer.parseInt(tableModelChatLieu.getValueAt(row, 0).toString()));
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Sửa chất liệu thành công!");
                    loadDataThuocTinh();
                    loadComboBoxData();
                    clearFormThuocTinh();
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Sửa chất liệu thất bại!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chất liệu để sửa!");
        }
    }//GEN-LAST:event_btnSuaChatLieuActionPerformed

    private void btnXoaChatLieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaChatLieuActionPerformed
        // TODO add your handling code here:
        int row = tblChatLieu.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa chất liệu này?");
            if (confirm == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM ChatLieu WHERE id_chat_lieu = ?";
                try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, Integer.parseInt(tableModelChatLieu.getValueAt(row, 0).toString()));
                    int rows = ps.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Xóa chất liệu thành công!");
                        loadDataThuocTinh();
                        loadComboBoxData();
                        clearFormThuocTinh();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Xóa chất liệu thất bại! Có thể chất liệu đang được sử dụng.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chất liệu để xóa!");
        }
    }//GEN-LAST:event_btnXoaChatLieuActionPerformed

    private void tblChatLieuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChatLieuMouseClicked
        // TODO add your handling code here:
        int row = tblChatLieu.getSelectedRow();
        if (row >= 0) {
            txtMaChatLieu.setText(tableModelChatLieu.getValueAt(row, 1).toString());
            txtTenChatLieu.setText(tableModelChatLieu.getValueAt(row, 2).toString());
        }
    }//GEN-LAST:event_tblChatLieuMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ThuocTinhPanel;
    private javax.swing.JButton btnResetThuocTinh;
    private javax.swing.JButton btnSuaCTSP;
    private javax.swing.JButton btnSuaChatLieu;
    private javax.swing.JButton btnSuaKichThuoc;
    private javax.swing.JButton btnSuaMauSac;
    private javax.swing.JButton btnSuaSanPham;
    private javax.swing.JButton btnThemCTSP;
    private javax.swing.JButton btnThemChatLieu;
    private javax.swing.JButton btnThemKichThuoc;
    private javax.swing.JButton btnThemMauSac;
    private javax.swing.JButton btnThemSanPham;
    private javax.swing.JButton btnXoaCTSP;
    private javax.swing.JButton btnXoaChatLieu;
    private javax.swing.JButton btnXoaKichThuoc;
    private javax.swing.JButton btnXoaMauSac;
    private javax.swing.JButton btnXoaSanPham;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> cbbChatLieu;
    private javax.swing.JComboBox<String> cbbKichThuoc;
    private javax.swing.JComboBox<String> cbbMauSac;
    private javax.swing.JComboBox<String> cbbTenSp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblChatLieu;
    private javax.swing.JLabel lblGiaBan;
    private javax.swing.JLabel lblIDSP;
    private javax.swing.JLabel lblIDSPCT;
    private javax.swing.JLabel lblKichThuoc;
    private javax.swing.JLabel lblMaSP;
    private javax.swing.JLabel lblMauSac;
    private javax.swing.JLabel lblSoLuong;
    private javax.swing.JLabel lblTenSP;
    private javax.swing.JLabel lblTenSp;
    private javax.swing.JLabel lblTrangThai;
    private javax.swing.JLabel lblTrangThai2;
    private javax.swing.JRadioButton rdoDangBanSP;
    private javax.swing.JRadioButton rdoDangBanSPCT;
    private javax.swing.JRadioButton rdoNgungBanSP;
    private javax.swing.JRadioButton rdoNgungBanSPCT;
    private javax.swing.JTable tblChatLieu;
    private javax.swing.JTable tblKichThuoc;
    private javax.swing.JTable tblMauSac;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTable tblSanPhamChiTiet;
    private javax.swing.JTextField txtGiaBan;
    private javax.swing.JTextField txtIDSPCT;
    private javax.swing.JTextField txtIDSanPham;
    private javax.swing.JTextField txtMaChatLieu;
    private javax.swing.JTextField txtMaKichThuoc;
    private javax.swing.JTextField txtMaMauSac;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenChatLieu;
    private javax.swing.JTextField txtTenKichThuoc;
    private javax.swing.JTextField txtTenMauSac;
    private javax.swing.JTextField txtTenSP;
    // End of variables declaration//GEN-END:variables

}
