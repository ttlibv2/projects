package vn.conyeu.demo.hoadon.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;

@JacksonXmlRootElement(localName = "TTKHac")
public class TTKhacXml extends ArrayList<TTinXml> {

    public String getValue(String ttruong) {
        return stream()
                .filter(ttin -> ttruong.equals(ttin.getTTruong()))
                .findFirst().map(TTinXml::getDLieu).orElse(null);
    }
}