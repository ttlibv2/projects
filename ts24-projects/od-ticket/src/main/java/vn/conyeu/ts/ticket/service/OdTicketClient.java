package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.domain.ClsModel;
import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.odcore.service.OdClient;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;
import vn.conyeu.ts.ticket.domain.ClsNameSearchOption;
import vn.conyeu.ts.ticket.domain.ClsSearchReadOption;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class OdTicketClient<E> extends OdClient {

    public OdTicketClient(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    protected abstract Class<E> getDomainCls();
    protected abstract Function<ObjectMap, E> mapToObject();

    protected ObjectMap createContextMap() {
        return ObjectMap.setNew("context", createUserContext());
    }

    /**
     * Returns detail
     *
     * @param args any
     */
    protected <T> List<E> read(List<T> args) {
        //Object context = ObjectMap.setNew("context", createUserContext());
        String uriString = call_kwUri("read");
        ObjectMap response = sendPost(ObjectMap.create()
                .set("method", "read").set("model", getModel())
                .set("args", args).set("kwargs", createContextMap()), uriString);

        return response.getStream("result")
                .map(obj->mapToObject().apply(obj)).toList();
    }

    protected List<E> nameSearch(String keyword) {
        return nameSearch(keyword, null);
    }

    /**
     * Search by name. Path: `call_kw/{model}/name_search`
     * @param option  any => {limit, url, kwargs_args} `(Optional)`
     */
    protected List<E> nameSearch(ClsNameSearchOption option) {
        return nameSearch(null, option);
    }

    /**
     * Search by name. Path: `call_kw/{model}/name_search`
     *
     * @param keyword string
     * @param option  any => {limit, url, kwargs_args} `(Optional)`
     */
    protected List<E> nameSearch(String keyword, ClsNameSearchOption option) {
        option = ClsNameSearchOption.fixDefault(option);
        keyword = Objects.firstNotNull(keyword, "");

        String url = Objects.firstNonBlankSupplier(option.getUrl(),
                () -> call_kwUri("name_search"));

        ObjectMap kwargs = ObjectMap.create()
                .set("name", keyword)
                .set("args", option.getArgs())
                .set("context", createUserContext())
                .set("operator", "ilike")
                .set("limit", option.getLimit());

        return sendPost(ObjectMap.create()
                .set("args", new Object[0]).set("method", "name_search")
                .set("model", getModel()).set("kwargs", kwargs), url)
                .getStream("result", Object.class)
                .map(ClsModel::objectToMap)
                .map(obj -> mapToObject().apply(obj))
                .collect(Collectors.toList());
    }

    @Override
    protected ObjectMap checkResponse(Object requestBody, ObjectMap responseData) {
        return super.checkResponse(requestBody, responseData);
    }

    /**
     * Search with filter. Path `/{search_read}`
     */
    protected List<E> searchRead() {
        return searchRead(new ClsSearchReadOption());
    }

    protected List<E> searchRead(ClsFilterOption filterOption) {
        return searchRead(new ClsSearchReadOption(filterOption));
    }

    protected List<E> searchRead(ObjectMap searchObj) {
        return searchRead(new ClsSearchReadOption(searchObj));
    }

    /**
     * Search with filter. Path `/{search_read}`
     *
     * @param option -> {filterOptions, contextData}
     */
    protected List<E> searchRead(ClsSearchReadOption option) {
        Asserts.notNull(option, "@option -> ClsSearchReadOption");

        Object context = createUserContext().cloneMap().set(option.getContext());

        ObjectMap body = ObjectMap.setNew("model", getModel())
                .set("context", context)
                .set("domain", option.buildFilter())
                .set("fields", getFields("find", null))
                .set(option.getPage().cloneMap());

        return sendPost(body, datasetUri("search_read"))
                .getStream("result.records")
                .map(obj -> mapToObject().apply(obj))
                .toList();
    }

    protected ObjectMap callButton( String method, Object args) {
        ObjectMap body = ObjectMap.setNew("model",getModel())
            .set("method", method).set("args", new Object[]{args})
            .set("kwargs", ObjectMap.setNew("context", createUserContext()));

        return sendPost(body, datasetUri("call_button"));
    }


    protected ObjectMap createAndReturnMap(Object p) {
        Object context = createUserContext();
        Object body = ObjectMap
                .setNew("args", new Object[]{p})
                .set("method", "create")
                .set("model", getModel())
                .set("kwargs",ObjectMap.setNew("context", context));

        return sendPost( body, call_kwUri("create"));
    }


    protected ObjectMap updateAndReturnMap(Long objectId, Object object) {
        Object argId = Collections.singletonList(objectId);
        Object context = ObjectMap.setNew("context", createUserContext());
        Object body = ObjectMap.setNew("args", List.of(argId, object))
                .set("method", "create").set("model", getModel())
                .set("kwargs",context);

        String uri = call_kwUri("write");
        return sendPost(body, call_kwUri("write"));
    }

}