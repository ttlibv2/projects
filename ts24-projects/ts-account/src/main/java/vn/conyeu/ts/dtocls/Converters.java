package vn.conyeu.ts.dtocls;

import vn.conyeu.common.converter.base.ListToString;
import vn.conyeu.common.converter.base.ObjectToString;
import vn.conyeu.ts.odcore.domain.ClsModel;
import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.ticket.domain.*;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public final class Converters {

    private static class ClsConvert<E extends ClsModel<E>> extends ObjectToString<E> {
        static final Set<String> fieldOnly = Set.of("id", "name", "display_name");

        public ClsConvert(Class<E> objectClass) {
            this(objectClass, fieldOnly);
        }

        public ClsConvert(Class<E> objectClass, Set<String> onlyFields) {
            super(objectClass, onlyFields);
        }
    }

    public static class ClsUserConvert extends ClsConvert<ClsUser> {
        public ClsUserConvert() {
            super(ClsUser.class, new HashSet<>());
        }
    }

    public static class ClsAssignConvert extends ClsConvert<ClsUser> {
        public ClsAssignConvert() {
            super(ClsUser.class);
        }
    }

    public static class ClsCategorySubConvert extends ClsConvert<ClsCategorySub> {
        public ClsCategorySubConvert() {
            super(ClsCategorySub.class);
        }
    }

    public static class ClsCategoryConvert extends ClsConvert<ClsCategory> {
        public ClsCategoryConvert() {
            super(ClsCategory.class);
        }
    }

    public static class ClsPartnerConvert extends ClsConvert<ClsPartner> {
        public ClsPartnerConvert() {
            super(ClsPartner.class);
        }
    }

    public static class ClsTicketPriorityConvert extends ClsConvert<ClsTicketPriority> {
        public ClsTicketPriorityConvert() {
            super(ClsTicketPriority.class);
        }
    }

    public static class ClsRepiledStatusConvert extends ClsConvert<ClsRepiledStatus> {
        static final Set<String> fieldOnly = Set.of("id", "name", "code");
        public ClsRepiledStatusConvert() {
            super(ClsRepiledStatus.class, fieldOnly);
        }
    }

    public static class ClsSubjectTypeConvert extends ClsConvert<ClsSubjectType> {
        public ClsSubjectTypeConvert() {
            super(ClsSubjectType.class);
        }
    }

    public static class ClsTicketTagConvert extends ListToString<ClsTicketTag> {
        static final Set<String> fieldOnly = Set.of("id", "name", "display_name");

        public ClsTicketTagConvert() {
            super(ClsTicketTag.class, fieldOnly);
        }

    }

    public static class ClsHelpdeskTeamConvert extends ClsConvert<ClsHelpdeskTeam> {
        static final Set<String> fieldOnly = Set.of("id", "name", "display_name", "team_head");
        public ClsHelpdeskTeamConvert() {
            super(ClsHelpdeskTeam.class, fieldOnly);
        }
    }

    public static class ClsTeamHeadConvert extends ClsConvert<ClsTeamHead> {
        public ClsTeamHeadConvert() {
            super(ClsTeamHead.class);
        }
    }

    public static class ClsTicketTypeConvert extends ClsConvert<ClsTicketType> {
        public ClsTicketTypeConvert() {
            super(ClsTicketType.class);
        }
    }

    public static class ClsTopicConvert extends ClsConvert<ClsTopic> {
        public ClsTopicConvert() {
            super(ClsTopic.class);
        }
    }

    public static class ClsStageConvert extends ClsConvert<ClsStage> {
        public ClsStageConvert() {
            super(ClsStage.class);
        }
    }


    public static class ClsProductConvert extends ClsConvert<ClsProduct> {
        public ClsProductConvert() {
            super(ClsProduct.class);
        }
    }
}