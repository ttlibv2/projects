package vn.conyeu.ts.dtocls;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FileDto implements Serializable {
    private String name;
    private String md5;
    private String base64;
}