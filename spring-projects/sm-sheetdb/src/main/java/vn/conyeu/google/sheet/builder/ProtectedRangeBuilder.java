package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.Editors;
import com.google.api.services.sheets.v4.model.ProtectedRange;
import vn.conyeu.commons.utils.Lists;
import vn.conyeu.google.core.Utils;

public class ProtectedRangeBuilder implements XmlBuilder<ProtectedRange> {
    private final ProtectedRange range = new ProtectedRange();

    @Override
    public ProtectedRange build() {
        return range;
    }

    /**
     * The description of this protected range.
     *
     * @param description description or {@code null} for none
     */
    public ProtectedRangeBuilder description(String description) {
        range.setDescription(description);
        return this;
    }

    public ProtectedRangeBuilder editorUser(String... emailAddresses) {
        initEditors().setUsers(Lists.newList(emailAddresses));
        return this;
    }

    private Editors initEditors() {
        return Utils.setIfNull(range::getEditors, Editors::new, range::setEditors);
    }

    /**
     * The named range this protected range is backed by, if any.
     * <p>
     * When writing, only one of range or named_range_id may be set.
     *
     * @param namedRangeId namedRangeId or {@code null} for none
     */
    public ProtectedRangeBuilder namedRangeId(String namedRangeId) {
        range.setNamedRangeId(namedRangeId);
        return this;
    }

    /**
     * The range that is being protected. The range may be fully unbounded, in which case this is
     * considered a protected sheet.
     * <p>
     * When writing, only one of range or named_range_id may be set.
     *
     * @param consumer range or {@code null} for none
     */
    public ProtectedRangeBuilder range(ConsumerReturn<GridRangeBuilder> consumer) {
        GridRangeBuilder builder = consumer.accept(new GridRangeBuilder());
        range.setRange(builder.build());
        return this;
    }

    /**
     * Protected row
     * @param sheetId sheetId
     * @param beginRow 0-index
     */
    public ProtectedRangeBuilder forRow(Integer sheetId, Integer beginRow) {
        return forRow(sheetId, beginRow, beginRow+1);
    }

    /**
     * The range that is being protected. The range may be fully unbounded, in which case this is
     * considered a protected sheet.
     * @param sheetId sheetId or null for none
     *
     */
    public ProtectedRangeBuilder forRow(Integer sheetId, Integer beginRow, Integer endRow) {
        return range(r -> r.sheetId(sheetId).beginRow(beginRow).endRow(endRow));
    }

    /**
     * Protected column
     * @param sheetId sheetId
     * @param beginCol 0-index
     */
    public ProtectedRangeBuilder forColumn(Integer sheetId, Integer beginCol) {
        return forColumn(sheetId, beginCol, beginCol+1);
    }

    /**
     * Protected column
     * @param sheetId sheetId
     * @param beginCol 0-index
     * @param endCol 1-index
     */
    public ProtectedRangeBuilder forColumn(Integer sheetId, Integer beginCol, Integer endCol) {
        return range(r -> r.sheetId(sheetId).beginColumn(beginCol).endColumn(endCol));
    }

    /**
     * True if this protected range will show a warning when editing. Warning-based protection means
     * that every user can edit data in the protected range, except editing will prompt a warning
     * asking the user to confirm the edit.
     * <p>
     * When writing: if this field is true, then editors is ignored. Additionally, if this field is
     * changed from true to false and the `editors` field is not set (nor included in the field mask),
     * then the editors will be set to all the editors in the document.
     *
     * @param warningOnly warningOnly or {@code null} for none
     */
    public ProtectedRangeBuilder warningOnly(Boolean warningOnly) {
        range.setWarningOnly(warningOnly);
        return this;
    }
}