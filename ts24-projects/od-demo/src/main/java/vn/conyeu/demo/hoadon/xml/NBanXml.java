package vn.conyeu.demo.hoadon.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter @Setter @ToString
@JacksonXmlRootElement(localName = "NBan")
public class NBanXml  extends XmlEl {
    private String Ten;
    private String MST;
    private String DChi;
    private String SDThoai;
    private String DCTDTu;

}