package vn.conyeu.ts.odcore.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;

@Data
@EqualsAndHashCode(callSuper = false)
public  class ClsResponse extends ClsModel {
    String jsonrpc = "2.0";
    Long id;
    ObjectMap result;
    Object error;
}