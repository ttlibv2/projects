package vn.conyeu.ts.ticket.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.odcore.domain.*;
import vn.conyeu.ts.odcore.service.OdClient;
import vn.conyeu.ts.ticket.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class OdTicketClient<E> extends OdClient {

    public OdTicketClient(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    public Page<E> findAll() {
        return searchRead();
    }

    public Page<E> find(ClsSearch searchObj) {
        return searchRead(searchObj);
    }

    public Page<E> find(ClsFilterOption filterOption) {
        return searchRead(filterOption);
    }

    protected abstract Class<E> getDomainCls();
    protected abstract Function<ObjectMap, E> mapToObject();


    protected Page<E> forPage(List<E> data) {
        return new PageImpl<>(data);
    }

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
    protected Page<E> searchRead() {
        return searchRead(new ClsSearchReadOption());
    }

    protected Page<E> searchRead(Consumer<E> callbackItem) {
        return searchRead(new ClsSearchReadOption(), callbackItem);
    }

    protected Page<E> searchRead(ClsFilterOption filterOption) {
        return searchRead(filterOption, null);
    }

    protected Page<E> searchRead(ClsFilterOption filterOption, Consumer<E> callbackItem) {
        return searchRead(new ClsSearchReadOption(filterOption), callbackItem);
    }

//    protected Page<E> searchRead(ObjectMap data) {
//        return searchRead(ClsSearch.forData(data));
//    }

    protected Page<E> searchRead(ClsSearch searchObj) {
        return searchRead(searchObj, null);
    }

    protected Page<E> searchRead(ClsSearch searchObj, Consumer<E> callbackItem) {
        return searchRead(new ClsSearchReadOption(searchObj), callbackItem);
    }

    /**
     * Search with filter. Path `/{search_read}`
     *
     * @param option -> {filterOptions, contextData}
     */
    protected Page<E> searchRead(ClsSearchReadOption option) {
        return searchRead(option, null);
    }

    /**
     * Search with filter. Path `/{search_read}`
     *
     * @param option -> {filterOptions, contextData}
     */
    protected Page<E> searchRead(ClsSearchReadOption option, Consumer<E> callbackItem) {
        Asserts.notNull(option, "@option -> ClsSearchReadOption");

        Function<ObjectMap, E> convertItem = object -> {
            E item = mapToObject().apply(object);
            if(callbackItem != null) callbackItem.accept(item);
            return item;
        };

        Object context = createUserContext().cloneMap().set(option.getContext());
        ClsPage clsPage = option.getPage();

        ObjectMap body = ObjectMap.setNew("model", getModel())
                .set("context", context)
                .set("domain", option.buildFilter())
                .set("fields", getFields("find", null))
                .set(clsPage.cloneMap());

        ObjectMap response = sendPost(body, datasetUri("search_read")).getMap("result");

        List<E> listItem = response.getStream("records")
                .map(convertItem).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(clsPage.getPage(), clsPage.getLimit());
        return new PageImpl<>(listItem, pageable, response.getLong("length"));
    }

    protected ObjectMap callButton( String method, Object args) {
        return callButton(method, args, null);
    }

    protected ObjectMap callButton( String method, Object args, Consumer<ObjectMap> customContext) {
        ObjectMap context = createUserContext().cloneMap();
        if(customContext != null) customContext.accept(context);

        ObjectMap body = ObjectMap.setNew("model",getModel())
            .set("method", method).set("args", new Object[]{args})
            .set("kwargs", ObjectMap.setNew("context", context));

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