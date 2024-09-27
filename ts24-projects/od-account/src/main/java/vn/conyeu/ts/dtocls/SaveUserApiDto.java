package vn.conyeu.ts.dtocls;

import lombok.Getter;
import lombok.Setter;
import vn.conyeu.commons.beans.ObjectMap;
import java.io.Serializable;

@Getter @Setter
public class SaveUserApiDto implements Serializable {
    private Long api_id;
    private ObjectMap api_info;
    private ObjectMap user_api;
}