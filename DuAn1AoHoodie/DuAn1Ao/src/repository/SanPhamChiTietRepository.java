package repository;

import entity.SanPhamChiTiet;
import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SanPhamChiTietRepository {

    public List<SanPhamChiTiet> getAllSanPhamChiTiet() {
        List<SanPhamChiTiet> list = new ArrayList<>();
        String sql = "SELECT spct.id_ctsp, spct.id_san_pham, spct.so_luong, spct.gia_ban, spct.trang_thai, " +
                     "ms.ten_mau, sz.ten_size, cl.ten_chat_lieu, sp.ten_san_pham " +
                     "FROM SanPhamChiTiet spct " +
                     "JOIN MauSac ms ON spct.id_mau = ms.id_mau " +
                     "JOIN Size sz ON spct.id_size = sz.id_size " +
                     "JOIN ChatLieu cl ON spct.id_chat_lieu = cl.id_chat_lieu " +
                     "JOIN SanPham sp ON spct.id_san_pham = sp.id_san_pham";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SanPhamChiTiet spct = new SanPhamChiTiet();
                spct.setIdSpct(rs.getInt("id_ctsp"));
                spct.setIdSanPham(rs.getInt("id_san_pham"));
                spct.setMauSac(rs.getString("ten_mau"));
                spct.setKichThuoc(rs.getString("ten_size"));
                spct.setChatLieu(rs.getString("ten_chat_lieu"));
                spct.setSoLuong(rs.getInt("so_luong"));
                spct.setGiaBan(rs.getDouble("gia_ban"));
                spct.setTrangThai(rs.getInt("trang_thai"));
                spct.setTenSanPham(rs.getString("ten_san_pham"));
                list.add(spct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addSanPhamChiTiet(SanPhamChiTiet spct, int idMau, int idSize, int idChatLieu) {
        String sql = "INSERT INTO SanPhamChiTiet (id_san_pham, id_mau, id_size, id_chat_lieu, so_luong, gia_ban, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, spct.getIdSanPham());
            ps.setInt(2, idMau);
            ps.setInt(3, idSize);
            ps.setInt(4, idChatLieu);
            ps.setInt(5, spct.getSoLuong());
            ps.setDouble(6, spct.getGiaBan());
            ps.setInt(7, spct.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSanPhamChiTiet(SanPhamChiTiet spct, int idMau, int idSize, int idChatLieu) {
        String sql = "UPDATE SanPhamChiTiet SET id_san_pham = ?, id_mau = ?, id_size = ?, id_chat_lieu = ?, so_luong = ?, gia_ban = ?, trang_thai = ? WHERE id_ctsp = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, spct.getIdSanPham());
            ps.setInt(2, idMau);
            ps.setInt(3, idSize);
            ps.setInt(4, idChatLieu);
            ps.setInt(5, spct.getSoLuong());
            ps.setDouble(6, spct.getGiaBan());
            ps.setInt(7, spct.getTrangThai());
            ps.setInt(8, spct.getIdSpct());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSanPhamChiTiet(int idSpct) {
        String sql = "DELETE FROM SanPhamChiTiet WHERE id_ctsp = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSpct);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getIdByTenMau(String tenMau) {
        String sql = "SELECT id_mau FROM MauSac WHERE ten_mau = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenMau);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_mau");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getIdByTenSize(String tenSize) {
        String sql = "SELECT id_size FROM Size WHERE ten_size = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenSize);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_size");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getIdByTenChatLieu(String tenChatLieu) {
        String sql = "SELECT id_chat_lieu FROM ChatLieu WHERE ten_chat_lieu = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenChatLieu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_chat_lieu");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public boolean isDuplicateSanPhamChiTiet(int idSanPham, int idMau, int idSize, int idChatLieu) {
    String sql = "SELECT COUNT(*) FROM SanPhamChiTiet WHERE id_san_pham = ? AND id_mau = ? AND id_size = ? AND id_chat_lieu = ?";
    try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idSanPham);
        ps.setInt(2, idMau);
        ps.setInt(3, idSize);
        ps.setInt(4, idChatLieu);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
}