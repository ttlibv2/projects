package vn.conyeu.demo.hoadon.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@JacksonXmlRootElement(localName = "HHDVu")
public class HHDVuXml extends XmlEl {
    private String TChat;
    private String STT;
    private String MHHDVu;
    private String THHDVu;
    private String DVTinh;
    private String SLuong;
    private String DGia;
    private String TSuat;
    private String TLCKhau;
    private String STCKhau;
    private String ThTien;


}