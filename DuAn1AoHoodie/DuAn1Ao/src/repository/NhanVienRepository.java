package repository;

import entity.NhanVien;
import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NhanVienRepository {

    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setIdNhanVien(rs.getInt("id_nhan_vien"));
                nv.setMaNhanVien(rs.getString("ma_nhan_vien"));
                nv.setHoTen(rs.getString("ho_ten"));
                nv.setChucVu(rs.getInt("chuc_vu"));
                nv.setEmail(rs.getString("email"));
                nv.setSoDienThoai(rs.getString("so_dien_thoai"));
                nv.setGioiTinh(rs.getInt("gioi_tinh"));
                nv.setTrangThai(rs.getInt("trang_thai"));
                nv.setDiaChi(rs.getString("dia_chi"));
                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (ma_nhan_vien, ho_ten, chuc_vu, email, so_dien_thoai, gioi_tinh, trang_thai, dia_chi) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNhanVien());
            ps.setString(2, nv.getHoTen());
            ps.setInt(3, nv.getChucVu());
            ps.setString(4, nv.getEmail());
            ps.setString(5, nv.getSoDienThoai());
            ps.setInt(6, nv.getGioiTinh());
            ps.setInt(7, nv.getTrangThai());
            ps.setString(8, nv.getDiaChi());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateNhanVien(NhanVien nv) {
        String sql = "UPDATE NhanVien SET ma_nhan_vien = ?, ho_ten = ?, chuc_vu = ?, email = ?, so_dien_thoai = ?, gioi_tinh = ?, trang_thai = ?, dia_chi = ? WHERE id_nhan_vien = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNhanVien());
            ps.setString(2, nv.getHoTen());
            ps.setInt(3, nv.getChucVu());
            ps.setString(4, nv.getEmail());
            ps.setString(5, nv.getSoDienThoai());
            ps.setInt(6, nv.getGioiTinh());
            ps.setInt(7, nv.getTrangThai());
            ps.setString(8, nv.getDiaChi());
            ps.setInt(9, nv.getIdNhanVien());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<NhanVien> searchNhanVien(String keyword, String gioiTinh, String trangThai) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE ho_ten LIKE ?";
        if (!gioiTinh.equals("Tất cả")) {
            sql += " AND gioi_tinh = ?";
        }
        if (!trangThai.equals("Tất cả")) {
            sql += " AND trang_thai = ?";
        }
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            ps.setString(paramIndex++, "%" + keyword + "%");
            if (!gioiTinh.equals("Tất cả")) {
                ps.setInt(paramIndex++, gioiTinh.equals("Nam") ? 1 : 0);
            }
            if (!trangThai.equals("Tất cả")) {
                ps.setInt(paramIndex, trangThai.equals("Đang làm") ? 1 : 0);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setIdNhanVien(rs.getInt("id_nhan_vien"));
                nv.setMaNhanVien(rs.getString("ma_nhan_vien"));
                nv.setHoTen(rs.getString("ho_ten"));
                nv.setChucVu(rs.getInt("chuc_vu"));
                nv.setEmail(rs.getString("email"));
                nv.setSoDienThoai(rs.getString("so_dien_thoai"));
                nv.setGioiTinh(rs.getInt("gioi_tinh"));
                nv.setTrangThai(rs.getInt("trang_thai"));
                nv.setDiaChi(rs.getString("dia_chi"));
                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
