package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.Editors;
import vn.conyeu.commons.utils.Lists;
import vn.conyeu.google.core.Utils;

import java.util.List;

public class EditorsBuilder implements XmlBuilder<Editors> {
    private final Editors editors;

    public EditorsBuilder(Editors editors) {
        this.editors = Utils.getIfNull(editors, Editors::new);
    }

    @Override
    public EditorsBuilder copy() {
        return new EditorsBuilder(editors.clone());
    }

    @Override
    public Editors build() {
        return editors;
    }

    /**
     * True if anyone in the document's domain has edit access to the protected range.  Domain
     * protection is only supported on documents within a domain.
     * @param domainUsersCanEdit domainUsersCanEdit or {@code null} for none
     */
    public EditorsBuilder domainUsersCanEdit(Boolean domainUsersCanEdit) {
        editors.setDomainUsersCanEdit(domainUsersCanEdit);
        return this;
    }

    /**
     * The email addresses of groups with edit access to the protected range.
     * @param emailAddresses groups or {@code null} for none
     */
    public EditorsBuilder groups(String... emailAddresses) {
        editors.setGroups(Lists.newList(emailAddresses));
        return this;
    }

    /**
     * The email addresses of users with edit access to the protected range.
     * @param emailAddresses users or {@code null} for none
     */
    public EditorsBuilder users(String... emailAddresses) {
        editors.setUsers(Lists.newList(emailAddresses));
        return this;
    }

    /**
     * The email addresses of users with edit access to the protected range.
     * @param emailAddresses users or {@code null} for none
     */
    public EditorsBuilder users(List<String> emailAddresses) {
        editors.setUsers(Lists.newList(emailAddresses));
        return this;
    }
}