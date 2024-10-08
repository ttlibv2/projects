package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.ticket.domain.ClsNameSearchOption;
import vn.conyeu.ts.ticket.domain.ClsProduct;

import java.util.List;
import java.util.function.Function;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
public class OdProduct extends OdTicketClient<ClsProduct> {

    public OdProduct(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "product.product";
    }

    @Override
    protected Class<ClsProduct> getDomainCls() {
        return ClsProduct.class;
    }

    @Override
    protected Function<ObjectMap, ClsProduct> mapToObject() {
        return ClsProduct::from;
    }

    public List<ClsProduct> getAll(Integer size) {
        return nameSearch(new ClsNameSearchOption().setLimit(size));
    }
}