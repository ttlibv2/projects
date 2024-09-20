package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.Editors;
import com.google.api.services.sheets.v4.model.ProtectedRange;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.core.Utils;

import java.util.List;


public class ProtectedPermission implements XmlBuilder<ProtectedRange> {
    private final ProtectedRange range;
    private EditorsBuilder editorsBuilder;

    public ProtectedPermission(final ProtectedRange protectedRange) {
        this.range = Asserts.notNull(protectedRange);
    }

    @Override
    public ProtectedRange build() {
        return range;
    }

    /**
     * The description of this protected range.
     * @param description description or {@code null} for none
     */
    public ProtectedPermission description(String description) {
        range.setDescription(description);
        return this;
    }

    /**
     * The named range this protected range is backed by, if any.
     * <p>
     * When writing, only one of range or named_range_id may be set.
     * @param namedRangeId namedRangeId or {@code null} for none
     */
    public ProtectedPermission namedRangeId(String namedRangeId) {
        range.setNamedRangeId(namedRangeId);
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
    public ProtectedPermission warningOnly(Boolean warningOnly) {
        range.setWarningOnly(warningOnly);
        return this;
    }

    /**
     * True if anyone in the document's domain has edit access to the protected range.  Domain
     * protection is only supported on documents within a domain.
     * @param domainUsersCanEdit domainUsersCanEdit or {@code null} for none
     */
    public ProtectedPermission domainUsersCanEdit(Boolean domainUsersCanEdit) {
        getEditorsBuilder().domainUsersCanEdit(domainUsersCanEdit);
        return this;
    }

    /**
     * The email addresses of groups with edit access to the protected range.
     * @param emailAddresses groups or {@code null} for none
     */
    public ProtectedPermission groups(String... emailAddresses) {
        getEditorsBuilder().groups(emailAddresses);
        return this;
    }

    /**
     * The email addresses of users with edit access to the protected range.
     * @param emailAddresses users or {@code null} for none
     */
    public ProtectedPermission users(String... emailAddresses) {
        getEditorsBuilder().users(emailAddresses);
        return this;
    }

    /**
     * The email addresses of users with edit access to the protected range.
     * @param emailAddresses users or {@code null} for none
     */
    public ProtectedPermission users(List<String> emailAddresses) {
        getEditorsBuilder().users(emailAddresses);
        return this;
    }


    private Editors initEditors() {
        return Utils.setIfNull(range::getEditors, Editors::new, range::setEditors);
    }

    private EditorsBuilder getEditorsBuilder() {
        if (editorsBuilder == null) {
            editorsBuilder = new EditorsBuilder(Utils.setIfNull(range::getEditors, Editors::new, range::setEditors));
        }
        return editorsBuilder;
    }
}