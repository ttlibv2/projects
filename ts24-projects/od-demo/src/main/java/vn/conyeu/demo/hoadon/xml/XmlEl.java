package vn.conyeu.demo.hoadon.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter @Setter
public abstract class XmlEl implements Serializable {
    @JacksonXmlProperty(localName = "TTKhac")
    protected TTKhacXml TTKhac;

    public String get(String ttruong) {
        return TTKhac == null ? null : TTKhac.getValue(ttruong);
    }
}