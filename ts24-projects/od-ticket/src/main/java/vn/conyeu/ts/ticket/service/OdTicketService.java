//package vn.conyeu.ts.ticket.service;
//
//import vn.conyeu.commons.beans.ObjectMap;
//import vn.conyeu.ts.odcore.domain.ClsUser;
//import vn.conyeu.ts.odcore.service.OdBaseService;
//import vn.conyeu.ts.odcore.service.ServiceUID;
//
//@ServiceUID(OdTicketService.SERVICE_NAME)
//public class OdTicketService extends OdBaseService<OdTicketClient> {
//    public static final String SERVICE_NAME = "od.ticket";
//
//    public static final ClsApiCfg DEFAULT_INFO = new ClsApiCfg()
//            .setApiTitle("Ticket Api")
//            .setServiceName(SERVICE_NAME)
//            .setServiceUid(SERVICE_NAME)
//            .setBaseUrl("https://web.ts24.com.vn/web")
//            .setLoginPath("/login")
//            .setHeaders(ObjectMap.create())
//            .setQueries(ObjectMap.create());
//
//    public OdTicketService(ClsApiCfg clsApiConfig) {
//        super(clsApiConfig);
//    }
//
//    public final String getUniqueId() {
//        return SERVICE_NAME;
//    }
//
//    protected ClsUser loginImpl() {
//        ClsUser clsUser = user().login();
//        clsUser.setMenuLinks(menus().loadMenuForUser());
//        return clsUser;
//    }
//
//    @Override
//    public ObjectMap loadMenus() {
//        return menus().loadMenuForUser();
//    }
//
//    /**
//     * Returns {@link OdCategory}
//     */
//    public OdCategory category() {
//        return service(OdCategory.class, () -> new OdCategory(clsConfig));
//    }
//
//    /**
//     * Returns {@link OdCategory}
//     */
//    public OdCategorySub categorySub() {
//        return service(OdCategorySub.class, () -> new OdCategorySub(clsConfig));
//    }
//
//    /**
//     * Returns {@link OdFile}
//     */
//    public OdFile file() {
//        return service(OdFile.class, () -> new OdFile(clsConfig));
//    }
//
//    /**
//     * Returns {@link OdFollow}
//     */
//    public OdFollow follow() {
//        return service(OdFollow.class, () -> new OdFollow(clsConfig));
//    }
//
//    /**
//     * Returns {@link OdHelpdeskTeam}
//     */
//    public OdHelpdeskTeam team() {
//        return service(OdHelpdeskTeam.class, () -> new OdHelpdeskTeam(clsConfig));
//    }
//
//    /**
//     * Returns {@link OdMailTemplate}
//     */
//    public OdMailTemplate mailTemplate() {
//        return service(OdMailTemplate.class, () -> new OdMailTemplate(clsConfig));
//    }
//
//    /**
//     * Returns {@link OdMessage}
//     */
//    public OdMessage message() {
//        return service(OdMessage.class, () -> new OdMessage(clsConfig, file()));
//    }
//
//    /**
//     * Returns {@link OdPartner}
//     */
//    public OdPartner partner() {
//        return service(OdPartner.class, () -> new OdPartner(clsConfig));
//    }
//
//    /**
//     * Returns {@link OdPriority}
//     */
//    public OdPriority priority() {
//        return service(OdPriority.class, () -> new OdPriority(clsConfig));
//    }
//
//    public OdProduct product() {
//        return service(OdProduct.class, () -> new OdProduct(clsConfig));
//    }
//
//    /**
//     * Returns {@link OdStage}
//     */
//    public OdStage stage() {
//        return service(OdStage.class, () -> new OdStage(clsConfig));
//    }
//
//    /**
//     * Returns {@link OdTicketSubType}
//     */
//    public OdTicketSubType ticketSubjectType() {
//        return service(OdTicketSubType.class, () -> new OdTicketSubType(clsConfig));
//    }
//
//    /**
//     * Returns {@link OdTicketTags}
//     */
//    public OdTicketTags ticketTags() {
//        return service(OdTicketTags.class, () -> new OdTicketTags(clsConfig));
//    }
//
//    /**
//     * Returns {@link OdTicketType}
//     */
//    public OdTicketType ticketType() {
//        return service(OdTicketType.class, () -> new OdTicketType(clsConfig));
//    }
//
//    /**
//     * Returns {@link OdTicket}
//     */
//    public OdTicket ticket() {
//        return service(OdTicket.class, () -> new OdTicket(clsConfig, message(), follow(), composeMsg()));
//    }
//
//    /**
//     * Returns {@link OdMailComposeMsg}
//     */
//    public OdMailComposeMsg composeMsg() {
//        return service(OdMailComposeMsg.class, () -> new OdMailComposeMsg(clsConfig));
//    }
//
//    /**
//     * Returns {@link OdUser}
//     */
//    public OdUser user() {
//        return service(OdUser.class, () -> new OdUser(clsConfig));
//    }
//
//    /**
//     * Returns {@link OdTopic}
//     */
//    public OdTopic topic() {
//        return service(OdTopic.class, () -> new OdTopic(clsConfig));
//    }
//
//    /**
//     * Returns {@link OdWkTeam}
//     */
//    public OdWkTeam wkTeam() {
//        return service(OdWkTeam.class, () -> new OdWkTeam(clsConfig));
//    }
//
//    public OdWebClient menus() {
//        return service(OdWebClient.class, () -> new OdWebClient(clsConfig));
//    }
//
//
//}