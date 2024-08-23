package vn.conyeu.demo.hoadon.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@JacksonXmlRootElement(localName = "TToan")
public class TToanXml  extends XmlEl {

    private BigDecimal TTCKTMai;
    private BigDecimal TGTKhac;
    private BigDecimal TgTTTBSo;
    private String TgTTTBChu;

}