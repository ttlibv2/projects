package vn.conyeu.demo.hoadon.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@JacksonXmlRootElement(localName = "DSCKS")
public class DSCKSXml implements Serializable {
    private NBanSign NBan;

    public String getNBanTime() {
        if(NBan == null) return null;
        else if(NBan.getSignature() == null)return null;
        else if(NBan.getSignature().getObject() == null) return null;
        else if(NBan.getSignature().getObject().getSignatureProperties() == null || NBan.getSignature().getObject().getSignatureProperties().isEmpty()) return null;
        else return NBan.getSignature().getObject().getSignatureProperties().get(0).getSigningTime();
    }

    @Getter @Setter @ToString
    public static class NBanSign implements Serializable {
        private SignatureXml Signature;
    }
}