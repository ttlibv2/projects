package vn.conyeu.demo.hoadon.help;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import vn.conyeu.demo.hoadon.cls.ClsBienLai;
import vn.conyeu.demo.hoadon.jackson.ItNamingBase;
import vn.conyeu.demo.hoadon.xml.HoaDonXml;

import java.io.IOException;
import java.nio.file.Path;

public final class XmlHelper {
   public final static XmlMapper xmlMapper = buildMapper();

    public static XmlMapper buildMapper() {
        XmlMapper mapper = XmlMapper.xmlBuilder()
                .addModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();

        mapper.setPropertyNamingStrategy(ItNamingBase.INSTANCE);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }


    public static <E> E parseXml(Class<E> eClass, String xml) {
        try{return xmlMapper.readValue(xml, eClass);}
        catch (JsonProcessingException exp ){
            throw new RuntimeException(exp);
        }
    }

    public static HoaDonXml parseHoaDonXml(Path xmlPath) {
        try {
            return xmlMapper.readValue(xmlPath.toFile(), new TypeReference<HoaDonXml>() {});
        }catch (IOException exp) {
            throw new RuntimeException(exp);
        }
    }

    public static ClsBienLai parseBL(Path xmlPath) {
        HoaDonXml xml = parseHoaDonXml(xmlPath);
        ClsBienLai bl = new ClsBienLai();
        xml.updateTo(bl);
        bl.setHinhThucHoaDon("-700");

        return bl;
    }



}