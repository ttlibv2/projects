package vn.conyeu.google.sheetdb.builder;

import com.google.api.services.sheets.v4.model.BasicFilter;
import com.google.api.services.sheets.v4.model.FilterCriteria;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.SortSpec;

import java.util.*;

public class BasicFilterBuilder implements XmlBuilder<BasicFilter> {
    private final BasicFilter basicFilter;

    public BasicFilterBuilder(BasicFilter basicFilter) {
        this.basicFilter = basicFilter == null ? new BasicFilter() : basicFilter;
    }

    @Override
    public BasicFilter build() {
        return basicFilter;
    }

    /**
     * The criteria for showing/hiding values per column. The map's key is the column index, and the
     * value is the criteria for that column.
     *
     * @param criteria criteria or {@code null} for none
     */
    public BasicFilterBuilder criteria(String criteriaKey, ConsumerReturn<FilterCriteria> criteria) {
        criteria.accept(initFilterCriteria(criteriaKey));
        return this;
    }

    private FilterCriteria initFilterCriteria(String criteriaKey) {

        Map<String, FilterCriteria> map = basicFilter.getCriteria();
        if(map == null) {
            map = new HashMap<>();
            basicFilter.setCriteria(map);
        }

        FilterCriteria criteria = map.get(criteriaKey);
        if(criteria == null) {
            criteria= new FilterCriteria();
            map.put(criteriaKey, criteria);
        }
        return criteria;
    }


    /**
     * The end column (exclusive) of the range, or not set if unbounded.
     *
     * @param endColumnIndex endColumnIndex or {@code null} for none
     */
    public BasicFilterBuilder endColumnIndex(Integer endColumnIndex) {
        initRange().setEndColumnIndex(endColumnIndex);
        return this;
    }

    /**
     * The end row (exclusive) of the range, or not set if unbounded.
     *
     * @param endRowIndex endRowIndex or {@code null} for none
     */
    public BasicFilterBuilder endRowIndex(Integer endRowIndex) {
        initRange().setEndRowIndex(endRowIndex);
        return this;
    }

    /**
     * The sheet this range is on.
     *
     * @param sheetId sheetId or {@code null} for none
     */
    public BasicFilterBuilder sheetId(Integer sheetId) {
        initRange().setSheetId(sheetId);
        return this;
    }

    /**
     * The start column (inclusive) of the range, or not set if unbounded.
     *
     * @param startColumnIndex startColumnIndex or {@code null} for none
     */
    public BasicFilterBuilder startColumnIndex(Integer startColumnIndex) {
        initRange().setStartColumnIndex(startColumnIndex);
        return this;
    }

    /**
     * The start row (inclusive) of the range, or not set if unbounded.
     *
     * @param startRowIndex startRowIndex or {@code null} for none
     */
    public BasicFilterBuilder startRowIndex(Integer startRowIndex) {
        initRange().setStartRowIndex(startRowIndex);
        return this;
    }

    /**
     * The sort order per column. Later specifications are used when values are equal in the earlier
     * specifications.
     *
     * @param sortSpecs sortSpecs or {@code null} for none
     */
    public BasicFilterBuilder addSortSpecs(ConsumerReturn<SortSpec> sortSpecs) {
        SortSpec spec = sortSpecs.accept(new SortSpec());
        initSortSpecs().add(spec);
        return this;
    }

    private List<SortSpec> initSortSpecs() {
        List<SortSpec> specs = basicFilter.getSortSpecs();
        if(specs == null) {
            specs = new ArrayList<>();
            basicFilter.setSortSpecs(specs);
        }
        return specs;
    }


    private GridRange initRange() {
        GridRange range = basicFilter.getRange();
        if (range == null) {
            range = new GridRange();
            basicFilter.setRange(range);
        }
        return range;
    }
}