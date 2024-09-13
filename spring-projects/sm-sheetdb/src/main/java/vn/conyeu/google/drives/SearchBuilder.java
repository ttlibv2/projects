package vn.conyeu.google.drives;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLSerializer;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.google.query.BoolExpression;
import java.util.function.Function;

@Slf4j
public final class SearchBuilder {
    public String fields;
    public Integer pageSize = 1000;
    private String pageToken;
    private String orderBy;
    private DriveQuery query;
    private BoolExpression predicate;

    private SearchFunc queryFunc;



    public static SearchBuilder fromQuery(SearchFunc queryFunc) {
        SearchBuilder sb = new SearchBuilder();
        sb.queryFunc = queryFunc;
        return sb;
    }

    public static SearchBuilder create() {
        return new SearchBuilder();
    }

    public SearchBuilder query(SearchFunc function) {
        if(query == null) query = new DriveQuery();

        BoolExpression expression = function.apply(query);
        if(predicate == null) predicate = expression;
        else predicate = predicate.and(expression);
        return this;
    }

    /**
     * Set the fields
     *
     * @param fields the value
     */
    public SearchBuilder fields(String fields) {
        this.fields = fields;
        return this;
    }

    /**
     * Set the pageSize
     *
     * @param pageSize the value
     */
    public SearchBuilder pageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * Set the pageToken
     *
     * @param pageToken the value
     */
    public SearchBuilder pageToken(String pageToken) {
        this.pageToken = pageToken;
        return this;
    }

    /**
     * Set the orderBy
     *
     * @param orderBy the value
     */
    public SearchBuilder orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    /**
     * Returns the fields
     */
    public String getFields() {
        return fields;
    }

    /**
     * Returns the pageSize
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * Returns the pageToken
     */
    public String getPageToken() {
        return pageToken;
    }

    /**
     * Returns the orderBy
     */
    public String getOrderBy() {
        return orderBy;
    }

    public String buildQuery() {
        String searchQuery = new DriveQuery.DriveSerializer().serialize(predicate);
        log.warn("searchQuery: {}", searchQuery);
        return searchQuery;
    }

    @FunctionalInterface
    public interface SearchFunc extends Function<DriveQuery, BoolExpression> {
    }

}