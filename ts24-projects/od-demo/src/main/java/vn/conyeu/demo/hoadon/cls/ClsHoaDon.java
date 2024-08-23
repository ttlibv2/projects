package vn.conyeu.demo.hoadon.cls;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.commons.beans.ObjectMap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Getter@Setter
public class ClsHoaDon {
    protected String ID = UUID.randomUUID().toString();
    protected String MaKhachHang;
    protected String MaCongTy;
    protected String MaCaiDat;
    protected String MaNgoaiTe;
    protected String MaThanhToan;
    protected String SoBatDau;
    protected BigDecimal TongTien;
    protected String NguoiLap;
    protected String NgayHoaDon;
    protected String LoaiHoaDon;
    protected String TenDonViNhapKhau;
    protected String DiaChi;
    protected String DienThoai;
    protected String HinhThucThanhToan;
    protected String LoaiTienTe;
    protected String SoTaiKhoan;
    protected String SoHopDong;
    protected String NgayHopDong;
    protected String DiaDiemGiaoHang;
    protected String DiaDiemNhanHang;
    protected String SoVanDon;
    protected String SoConTainer;
    protected String TenDonViVanChuyen;
    protected String CanCuLenhDieuLenh;
    protected String NgayThangNam;
    protected String Cua;
    protected String VeViec;
    protected String HopDongSo;
    protected String XuatTaiKho;
    protected String NhapTaiKho;
    protected String ThuKhoXuat;
    protected String NguoiVanChuyen;
    protected String CanCuHopDongKinhTeSo;
    protected String Voi;
    protected String HoTenNguoiVanChuyen;
    protected String PhuongTienVanChuyen;
    protected String NguoiNhanHang;
    protected String ThuKhoNhap;
    protected String HoTenNguoiMuaHang;
    protected String TenDonVi;
    protected String MaSoThue;
    protected String MauSo;
    protected Integer SoThuTu;
    protected String KyHieu;
    protected String TinhTrang;
    protected String ThueSuatGTGT;
    protected String TenTongTien;
    protected String NoiMoTaiKhoan;
    protected String NgayTinhTrang;
    protected String SoLienMat;
    protected String GioMat;
    protected String LyDoMat;
    protected String ThongTinHangChung;
    protected String TinhTrangBangKe;
    protected BigDecimal TongTienThueGTGT;
    protected String TinhTrangChuyenHoaDonGiay;
    protected String TinhTrangKyNguoiBan;
    protected String TinhTrangKyNguoiMua;
    protected String TinhTrangGoiEmail;
    protected String HinhThucHoaDon;
    protected String DuongDanFile;
    protected String NamThangNgayHoaDon;
    protected String EmailKhachHang;
    protected String NguoiChuyenHoaDon;
    protected String NgayThangNamChuyenHoaDon;
    protected String MaHoaDonThayThe;
    protected String TinhTrangThayThe;
    protected String SoHoaDonThayThe;
    protected String BanHangQuaDienThoai;
    protected String QuocTich;
    protected String SoHoChieu;
    protected String SoBangKe;
    protected String NgayGuiMail;
    protected String UpServerState;
    protected String NgayUpServerState;
    protected String NoidungXML;
    protected String NoidungXML2;
    protected String DNghiepCNhan;
    protected String ACCESSKEY;
    protected String NgayKyNguoiBan;
    protected String NgayKyNguoiMua;
    protected Double TyGiaNT;
    protected String InvoiceId;
    protected String GUID_CONGTY;
    protected String DAXUATHD;
    protected String LAHDONTHAYTHE;
    protected String NOIDUNGTHAYTHE;
    protected String GHICHU;
    protected String TINHTRANGDIEUCHINH;
    protected String NGAYDIEUCHINH;
    protected Integer HOADONXACTHUC;
    protected Integer TINHTRANGXACTHUC;
    protected String NGAYXACTHUC;
    protected String DownServerState;
    protected String NgayDownServerState;
    protected String SOHDXACTHUC;
    protected String SOHDXACTHUC_XOABO;
    protected String MAXACTHUC;
    protected String MAXACTHUC_XOABO;
    protected String TINHTRANGKYBBANNMUA;
    protected String NGAYKYBBANNMUA;
    protected String TIENCHIETKHAUTMAI;
    protected String SOHOSO;
    protected String MANHANHOADON;
    protected String MUCBHTRA;
    protected String TYLEBH;
    protected String BAOHIEMTRA;
    protected String GUID_PX;
    protected LocalDateTime NGAYGIO_PX;
    protected String MA_PX;
    protected String NGAYNHAPKHO;
    protected String NGAYXUATKHO;
    protected String dienThoaiCN;
    protected String diaChiCN;
    protected String tenCN;
    protected String MaCH_CN;
    protected String TAIKHOANNGUOINGAN;
    protected String SODONHANG;
    protected String TAIKHOANGIAONHAN;
    protected String NGUOITAOHD;
    protected String NGAYLAPBIENBAN;
    protected String BUYERFAXNUMBER;
    protected String CHIPHIKHAC;
    protected String TIENCHIETKHAUTMAIVND;
    protected String MASOTHUENBAN;
    protected String CHIPHIKHACVND;
    protected String TONGTIENTHUEGTGTVND;
    protected String TONGTIENVND;
    protected String THANHTIENTRUOCTHUE;
    protected String THANHTIENTRUOCTHUEVND;
    protected Integer HIENTHINGOAITE;
    protected String TINHTRANGNHANEMAIL;
    protected String MOTANHANEMAIL;
    protected String MSTNUOCNGOAI;
    protected Integer DATHANHTOAN;
    protected Integer UPFILESTATUS;
    protected Integer KYTINHTHUE;
    protected String TONGTIENKCT;
    protected String TONGTIEN0;
    protected String TONGTIENCHUAVAT5;
    protected String TONGTIENVAT5;
    protected String TONGTIENCHUAVAT10;
    protected String TONGTIENVAT10;
    protected List<ClsChiTietHD> chiTiets;

    /**
     * Returns the chiTiets
     */
    public List<ClsChiTietHD> getChiTiets() {
        if(chiTiets == null)chiTiets = new ArrayList<>();
        return chiTiets;
    }

    public void addChitiet(ClsChiTietHD detail) {
        getChiTiets().add(detail);
        detail.setClsHoaDon(this);
    }

    public void setNgayHoaDon(LocalDate date) {
        NgayHoaDon = getNotNull(date, d -> d.format(HD_ISO));
        NgayThangNam = getNotNull(date, d -> d.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        NamThangNgayHoaDon = getNotNull(date, d -> d.format(NTNHD_ISO));
    }

    public void setSoThuTu(LocalDate date) {
        SoThuTu = getNotNull(date, LocalDate::getYear);
    }

    public void setMaCongTy(String maCongTy) {
        MaCongTy = maCongTy;
        GUID_CONGTY = maCongTy;
    }

    public String writeSql() {
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();

        ObjectMap map = ObjectMap.fromJson(this);
        map.remove("chiTiets");

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
            sb.append("\n\nINSERT INTO `hoadon` (");
            sb.append(String.join(",", keys));
            sb.append(") VALUES (");
            sb.append(String.join(",", values));
            sb.append(");");

            // chitiet
            for(ClsChiTietHD ct:getChiTiets()) {
                String sql = ct.writeSql();
                sb.append("\n").append(sql);
            }

            return sb.toString();
        }
    }

    public static <E, V> V getNotNull(E object, Function<E, V> function) {
        return object == null ? null : function.apply(object);
    }
    public static final DateTimeFormatter HD_ISO = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public static final DateTimeFormatter NTNHD_ISO = DateTimeFormatter.ofPattern("yyyy/MM/dd");

}