package com.example.asm_android_nang_cao;

import java.util.HashMap;
import java.util.Map;

public class SanPham {
    private String id;
    private String TenSp;
    private String GiaSp;
    private  String GhiChu;
    private String LinkHinhSp;

    public SanPham() {
    }

    public SanPham(String id, String tenSp, String giaSp, String ghiChu, String linkHinhSp) {
        this.id = id;
        TenSp = tenSp;
        GiaSp = giaSp;
        GhiChu = ghiChu;
        LinkHinhSp = linkHinhSp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenSp() {
        return TenSp;
    }

    public void setTenSp(String tenSp) {
        TenSp = tenSp;
    }

    public String getGiaSp() {
        return GiaSp;
    }

    public void setGiaSp(String giaSp) {
        GiaSp = giaSp;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        GhiChu = ghiChu;
    }

    public String getLinkHinhSp() {
        return LinkHinhSp;
    }

    public void setLinkHinhSp(String linkHinhSp) {
        LinkHinhSp = linkHinhSp;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> resuft = new HashMap<>();
        resuft.put("tenSp", TenSp);
        resuft.put("giaSp", GiaSp);
        resuft.put("ghiChu", GhiChu);
        resuft.put("linkHinhSp", LinkHinhSp);
        return resuft;
    }
}
