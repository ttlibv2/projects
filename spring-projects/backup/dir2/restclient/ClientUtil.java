package vn.conyeu.restclient;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;

public final class ClientUtil {

    public static MultiValueMap<String, String> toMultiValueMap(ObjectMap map) {
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        if(map != null) map.forEach((key, value) -> valueMap.put(key, Objects.toStringList(value)));
        return valueMap;
    }
}