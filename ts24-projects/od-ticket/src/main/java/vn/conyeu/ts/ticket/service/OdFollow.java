package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.domain.ClsFollow;
import java.util.List;
import java.util.function.Function;

public class OdFollow extends OdTicketClient<ClsFollow> {

    public OdFollow(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    public String getModel() {
        throw new UnsupportedOperationException();
    }

    public String getBasePath() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Class<ClsFollow> getDomainCls() {
        return null;
    }

    @Override
    protected Function<ObjectMap, ClsFollow> mapToObject() {
        return null;
    }

    public List<ClsFollow> read_followers(Long res_id, String res_model) {
        ObjectMap map = ObjectMap.create()
                .set("res_id", res_id)
                .set("res_model", res_model);

       return sendPost(map, "/mail/read_followers")
                .getStream("result.followers")
                .map(obj -> mapToObject().apply(obj))
                .toList();
    }



}