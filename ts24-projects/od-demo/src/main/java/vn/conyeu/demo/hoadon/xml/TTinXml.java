package vn.conyeu.demo.hoadon.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@JacksonXmlRootElement(localName = "TTin")
public class TTinXml {
    private String TTruong;
    private String KDLieu;
    private String DLieu;
}