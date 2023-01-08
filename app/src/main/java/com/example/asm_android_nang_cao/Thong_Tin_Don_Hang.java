package com.example.asm_android_nang_cao;

public class Thong_Tin_Don_Hang {
    private String idDonHang;
    private String idSanPham;
    private String tenKh;
    private String sDT;
    private  String soLuong;
    private  String diaChi;

    public Thong_Tin_Don_Hang() {
    }

    public Thong_Tin_Don_Hang(String idDonHang, String idSanPham, String tenKh, String sDT, String soLuong, String diaChi) {
        this.idDonHang = idDonHang;
        this.idSanPham = idSanPham;
        this.tenKh = tenKh;
        this.sDT = sDT;
        this.soLuong = soLuong;
        this.diaChi = diaChi;
    }

    public String getIdDonHang() {
        return idDonHang;
    }

    public void setIdDonHang(String idDonHang) {
        this.idDonHang = idDonHang;
    }

    public String getIdSanPham() {
        return idSanPham;
    }

    public void setIdSanPham(String idSanPham) {
        this.idSanPham = idSanPham;
    }

    public String getTenKh() {
        return tenKh;
    }

    public void setTenKh(String tenKh) {
        this.tenKh = tenKh;
    }

    public String getsDT() {
        return sDT;
    }

    public void setsDT(String sDT) {
        this.sDT = sDT;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
}
