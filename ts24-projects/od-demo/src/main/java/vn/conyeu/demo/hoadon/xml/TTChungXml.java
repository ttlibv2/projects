package vn.conyeu.demo.hoadon.xml;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Getter @Setter @ToString
@JacksonXmlRootElement(localName = "TTChung")
public class TTChungXml  extends XmlEl {
    private String PBan;
    private String THDon;
    private String KHMSHDon;
    private String KHHDon;
    private String SHDon;
    private Double TGia;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate NLap;

    private String DVTTe;
    private String HTTToan;
    private String MSTTCGP;

}