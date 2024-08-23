package vn.conyeu.demo.hoadon.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.conyeu.demo.hoadon.cls.ClsChiTietHD;
import vn.conyeu.demo.hoadon.cls.ClsHoaDon;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter @Setter @ToString
@JacksonXmlRootElement(localName = "HDon")
public class HoaDonXml {

    @JacksonXmlProperty(localName = "DLHDon")
    private DLHDonXml dlHD;

    @JacksonXmlProperty(localName = "DSCKS")
    private DSCKSXml DSCKS;

    public void updateTo(ClsHoaDon cls) {
        TTChungXml ttchung = dlHD.getTtChung();
        NMuaXml nmua = dlHD.getNdhDon().getNMua();
        NBanXml nban = dlHD.getNdhDon().getNBan();
        TToanXml ttoan = dlHD.getNdhDon().getTToan();

        cls.setMaNgoaiTe(ttchung.getDVTTe());
        cls.setLoaiTienTe(ttchung.getDVTTe());
        cls.setSoBatDau(ttchung.getSHDon());
        cls.setNgayHoaDon(ttchung.getNLap());
        cls.setLoaiHoaDon(ttchung.getTHDon());
        cls.setHinhThucThanhToan(ttchung.getHTTToan());
        cls.setMauSo(ttchung.getKHMSHDon());
        cls.setSoThuTu(ttchung.getNLap());
        cls.setKyHieu(ttchung.getKHHDon());
        cls.setNgayHopDong(ttchung.get("NQDinh"));
        cls.setThongTinHangChung(ttchung.get("TTHHChung"));
        cls.setTinhTrang("Đã sử dụng");
        cls.setNgayKyNguoiBan(DSCKS.getNBanTime());
        cls.setNgayKyNguoiMua(cls.getNgayKyNguoiBan());
        cls.setTyGiaNT(ttchung.getTGia());
        cls.setTINHTRANGDIEUCHINH("1");
        cls.setNGAYDIEUCHINH(cls.getNgayKyNguoiBan());
        cls.setHOADONXACTHUC(1);
        cls.setInvoiceId(ttchung.get("MNHDon"));
        cls.setMANHANHOADON(cls.getInvoiceId());
        cls.setNGUOITAOHD(nban.getDCTDTu());
        cls.setMASOTHUENBAN(nban.getMST());
        cls.setTinhTrangKyNguoiBan("Đã ký");

        cls.setGioMat(nban.getDCTDTu());
        cls.setNguoiLap(nban.get("NLap"));

        cls.setDiaChi(nmua.getDChi());
        cls.setDienThoai(nmua.getSDThoai());
        cls.setMaKhachHang(nmua.getMKHang());
        cls.setHoTenNguoiMuaHang(nmua.getHVTNMHang());
        cls.setEmailKhachHang(nmua.getDCTDTu());
        cls.setSoTaiKhoan(nmua.getSTKNHang());
        cls.setMaSoThue(nmua.getMST());
        cls.setBanHangQuaDienThoai("0");

        cls.setTongTien(ttoan.getTgTTTBSo());
        cls.setTenTongTien(ttoan.getTgTTTBChu());
        cls.setTongTienThueGTGT(BigDecimal.ZERO);

        //--
        int hdPos = 1;
        List<HHDVuXml> dsDongHang = dlHD.getNdhDon().getDSHHDVu();
        for(HHDVuXml dhang : dsDongHang) {
            ClsChiTietHD ct = new ClsChiTietHD();
            cls.addChitiet(ct);
            ct.setID(UUID.randomUUID().toString());
            ct.setMaHoaDon(cls.getID());
            ct.setMaHang(dhang.getMHHDVu());
            ct.setTenHang(dhang.getTHHDVu());
            ct.setDonViTinh(dhang.getDVTinh());
            ct.setSoLuong(dhang.getSLuong());
            ct.setDonGia(dhang.getDGia());
            ct.setDonGiaGoc(ct.getDonGia());
            ct.setThanhTien(dhang.getThTien());
            ct.setMaSo(dhang.get("CTieu1"));
            ct.setTongTien(dhang.get("ThTCThue"));
            ct.setTienThue(dhang.get("TThue"));
            ct.setThueSuat(dhang.getTSuat());
            ct.setKHUYENMAI(dhang.getTChat());
            ct.setSOTHUTU(dhang.getSTT());
            cls.setGHICHU(dhang.get("GChu"));
            ct.setTKCO2(dhang.get("CTieu5"));
            ct.setIDSTT(hdPos++);
        }



    }
}