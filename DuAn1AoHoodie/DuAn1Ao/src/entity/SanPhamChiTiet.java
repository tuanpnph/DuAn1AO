package entity;

public class SanPhamChiTiet {

    private int idSpct;
    private int idSanPham;
    private String mauSac;
    private String kichThuoc;
    private String chatLieu;
    private int soLuong;
    private double giaBan;
    private int trangThai;
    private String tenSanPham; 

    public SanPhamChiTiet() {
    }

    public SanPhamChiTiet(int idSpct, int idSanPham, String mauSac, String kichThuoc, String chatLieu, int soLuong, double giaBan, int trangThai, String tenSanPham) {
        this.idSpct = idSpct;
        this.idSanPham = idSanPham;
        this.mauSac = mauSac;
        this.kichThuoc = kichThuoc;
        this.chatLieu = chatLieu;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.trangThai = trangThai;
        this.tenSanPham = tenSanPham;
    }

    
    public int getIdSpct() {
        return idSpct;
    }

    public void setIdSpct(int idSpct) {
        this.idSpct = idSpct;
    }

    public int getIdSanPham() {
        return idSanPham;
    }

    public void setIdSanPham(int idSanPham) {
        this.idSanPham = idSanPham;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    public String getKichThuoc() {
        return kichThuoc;
    }

    public void setKichThuoc(String kichThuoc) {
        this.kichThuoc = kichThuoc;
    }

    public String getChatLieu() {
        return chatLieu;
    }

    public void setChatLieu(String chatLieu) {
        this.chatLieu = chatLieu;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }
}
