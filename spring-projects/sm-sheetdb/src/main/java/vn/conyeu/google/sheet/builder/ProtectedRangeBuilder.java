package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.ProtectedRange;
import vn.conyeu.google.core.Utils;

import java.util.List;

public class ProtectedRangeBuilder implements XmlBuilder<ProtectedRange> {
    private final ProtectedRange range;
    private final ProtectedPermission permission;

    public ProtectedRangeBuilder(ProtectedRange range) {
        this.range = Utils.getIfNull(range, ProtectedRange::new);
        this.permission = new ProtectedPermission(this.range);
    }

    @Override
    public ProtectedRangeBuilder copy() {
       return new ProtectedRangeBuilder(range.clone());
    }

    @Override
    public ProtectedRange build() {
        return range;
    }

    /** Returns the permission */
    public ProtectedPermission getPermission() {
        return permission;
    }

    /**
     * The description of this protected range.
     * @param description description or {@code null} for none
     */
    public ProtectedRangeBuilder description(String description) {
        permission.description(description);
        return this;
    }

    /**
     * The named range this protected range is backed by, if any.
     * <p>
     * When writing, only one of range or named_range_id may be set.
     * @param namedRangeId namedRangeId or {@code null} for none
     */
    public ProtectedRangeBuilder namedRangeId(String namedRangeId) {
        permission.namedRangeId(namedRangeId);
        return this;
    }


    /**
     * True if this protected range will show a warning when editing. Warning-based protection means
     * that every user can edit data in the protected range, except editing will prompt a warning
     * asking the user to confirm the edit.
     * <p>
     * When writing: if this field is true, then editors is ignored. Additionally, if this field is
     * changed from true to false and the `editors` field is not set (nor included in the field mask),
     * then the editors will be set to all the editors in the document.
     * @param warningOnly warningOnly or {@code null} for none
     */
    public ProtectedRangeBuilder warningOnly(Boolean warningOnly) {
        range.setWarningOnly(warningOnly);
        return this;
    }

    /**
     * True if anyone in the document's domain has edit access to the protected range.  Domain
     * protection is only supported on documents within a domain.
     * @param domainUsersCanEdit domainUsersCanEdit or {@code null} for none
     */
    public ProtectedRangeBuilder domainUsersCanEdit(Boolean domainUsersCanEdit) {
        permission.domainUsersCanEdit(domainUsersCanEdit);
        return this;
    }

    /**
     * The email addresses of groups with edit access to the protected range.
     * @param emailAddresses groups or {@code null} for none
     */
    public ProtectedRangeBuilder groups(String... emailAddresses) {
        permission.groups(emailAddresses);
        return this;
    }

    /**
     * The email addresses of users with edit access to the protected range.
     * @param emailAddresses users or {@code null} for none
     */
    public ProtectedRangeBuilder users(String... emailAddresses) {
        permission.users(emailAddresses);
        return this;
    }

    /**
     * The email addresses of users with edit access to the protected range.
     * @param emailAddresses users or {@code null} for none
     */
    public ProtectedRangeBuilder users(List<String> emailAddresses) {
        permission.users(emailAddresses);
        return this;
    }

    /**
     * The range that is being protected. The range may be fully unbounded, in which case this is
     * considered a protected sheet.
     * <p>
     * When writing, only one of range or named_range_id may be set.
     * @param consumer range or {@code null} for none
     */
    public ProtectedRangeBuilder range(ConsumerReturn<GridRangeBuilder> consumer) {
        GridRangeBuilder builder = consumer.accept(new GridRangeBuilder(range.getRange()));
        range.setRange(builder.build());
        return this;
    }


    /**
     * Protected row
     * @param sheetId sheetId
     * @param beginRow 0-index
     */
    public ProtectedRangeBuilder forRow(Integer sheetId, Integer beginRow) {
        forRow(sheetId, beginRow, beginRow + 1);
        return this;
    }

    /**
     * The range that is being protected. The range may be fully unbounded, in which case this is
     * considered a protected sheet.
     * @param sheetId sheetId or null for none
     */
    public ProtectedRangeBuilder forRow(Integer sheetId, Integer beginRow, Integer endRow) {
        range(r -> r.sheetId(sheetId).beginRow(beginRow).endRow(endRow));
        return this;
    }

    /**
     * Protected column
     * @param sheetId sheetId
     * @param beginCol 0-index
     */
    public ProtectedRangeBuilder forColumn(Integer sheetId, Integer beginCol) {
        forColumn(sheetId, beginCol, beginCol + 1);
        return this;
    }

    /**
     * Protected column
     * @param sheetId sheetId
     * @param beginCol 0-index
     * @param endCol 1-index
     */
    public ProtectedRangeBuilder forColumn(Integer sheetId, Integer beginCol, Integer endCol) {
        range(r -> r.sheetId(sheetId).beginColumn(beginCol).endColumn(endCol));
        return this;
    }


}