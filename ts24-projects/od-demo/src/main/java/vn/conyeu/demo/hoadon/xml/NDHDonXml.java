package vn.conyeu.demo.hoadon.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter @Setter @ToString
@JacksonXmlRootElement(localName = "NDHDon")
public class NDHDonXml implements Serializable {
    private NBanXml NBan;
    private NMuaXml NMua;

    @JacksonXmlProperty(localName = "DSHHDVu")
    private DSHHDVuXml DSHHDVu;

    private TToanXml TToan;
}