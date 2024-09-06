package vn.conyeu.google.drives;

import vn.conyeu.google.query.BoolExpression;
import java.util.function.Function;

public final class SearchBuilder {
    public String fields;
    public Integer pageSize = 1000;
    private String pageToken;
    private String orderBy;

    private SearchFunc queryFunc;

    public static SearchBuilder fromQuery(SearchFunc queryFunc) {
        SearchBuilder sb = new SearchBuilder();
        sb.queryFunc = queryFunc;
        return sb;
    }

    public static SearchBuilder create() {
        return new SearchBuilder();
    }

    /**
     * Set the queryFunc
     *
     * @param query the value
     */
    public SearchBuilder query(SearchFunc query) {
        if(this.queryFunc == null) this.queryFunc = query;
        else this.queryFunc = q -> queryFunc.apply(q).and(query.apply(q));
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
        DriveQuery driveQuery = new DriveQuery();
        return queryFunc.apply(driveQuery).toString();
    }

    @FunctionalInterface
    public interface SearchFunc extends Function<DriveQuery, BoolExpression> {
    }

}