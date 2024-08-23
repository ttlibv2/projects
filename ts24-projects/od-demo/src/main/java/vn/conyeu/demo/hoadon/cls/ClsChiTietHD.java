package vn.conyeu.demo.hoadon.cls;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.commons.beans.ObjectMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@JsonIgnoreProperties({"clsHoaDon"})
public class ClsChiTietHD {
    private String ID = UUID.randomUUID().toString();
    private String MaHoaDon;
    private String MaHang;
    private String TenHang;
    private String DonViTinh;
    private String SoLuong;
    private String DonGia;
    private String DonGiaGoc;
    private String ThanhTien;
    private String TenNhanHieu;
    private String MaSo;
    private String ThucXuat;
    private String ThucNhap;
    private String ThueSuat;
    private String TienThue;
    private String TongTien;
    private String GUID_CHUNGTU;
    private String TENCHUNGTU;
    private String GUID_CONGTY;
    private String KHUYENMAI;
    private String GHICHU;
    private String LOAIDIEUCHINH;
    private String TYLEBH;
    private String BAOHIEMTRA;
    private String MUCBHTRA;
    private String TYLETHUETTDB;
    private String SOTHUTU;
    private Integer IDSTT;
    private String KHONGHIENTHI;
    private String TKNO1;
    private String TKCO1;
    private String TKNO2;
    private String TKCO2;
    private String TKNO3;
    private String TKCO3;
    private String TKNO4;
    private String TKCO4;
    private String TKNO5;
    private String TKCO5;
    private String THANHTIENVND;
    private String TIENTHUEVND;
    private String TONGTIENVND;
    private String DONGIAVND;
    private String MIENGIAM;
    private String BENHNHANTHANHTOAN;
    private String CHIDINH_ID;
    private ClsHoaDon clsHoaDon;

    public String writeSql() {
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();

        if(clsHoaDon != null) {
            setMaHoaDon(clsHoaDon.getID());
        }

        ObjectMap map = ObjectMap.fromJson(this);
        map.remove("clsHoaDon");

        map.keySet().forEach(key -> {
            Object value = map.get(key);
            if(value != null) {
                keys.add("`%s`".formatted(key));
                values.add("\"%s\"".formatted(value));
            }
        });

        if(keys.isEmpty()) return null;
        else {
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO `chitiethoadon` (");
            sb.append(String.join(",", keys));
            sb.append(") VALUES (");
            sb.append(String.join(",", values));
            sb.append(");");
            return sb.toString();
        }
    }

}