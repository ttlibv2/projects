package vn.conyeu.demo.hoadon.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter @Setter @ToString
@JacksonXmlRootElement(localName = "DLHDon")
public class DLHDonXml implements Serializable {

    @JacksonXmlProperty(localName = "Id", isAttribute = true)
    private String Id;

    @JacksonXmlProperty(localName = "TTChung")
    private TTChungXml ttChung;

    @JacksonXmlProperty(localName = "NDHDon")
    private NDHDonXml ndhDon;


}