package vn.conyeu.demo.hoadon.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter @Setter @ToString
@JacksonXmlRootElement(localName = "Signature")
public class SignatureXml implements Serializable {
    private ObjectXml Object;

    @Getter @Setter @ToString
    public static class ObjectXml implements Serializable {
        private List<SignatureProperty> SignatureProperties;
    }

    @Getter @Setter @ToString
    public static class SignatureProperty implements  Serializable{
        private String SigningTime;
    }
}