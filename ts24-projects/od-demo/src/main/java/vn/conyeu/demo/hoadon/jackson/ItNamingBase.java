package vn.conyeu.demo.hoadon.jackson;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;

public class ItNamingBase extends PropertyNamingStrategies.NamingBase {
    public static final ItNamingBase INSTANCE = new ItNamingBase();

    @Override
    public String translate(String propertyName) {
        return propertyName;
    }
}