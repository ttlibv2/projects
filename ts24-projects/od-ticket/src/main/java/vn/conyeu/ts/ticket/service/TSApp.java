package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.app.OdApp;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.odcore.service.ServiceUID;

import java.util.function.Supplier;

@ServiceUID(TSApp.APP_UID)
public class TSApp extends OdApp<OdTicketClient> {
    public static final String APP_UID = "TSApp";
    
    public static final ClsApiCfg DEFAULT_CONFIG = new ClsApiCfg()
            .setAppName(APP_UID).setAppUID(APP_UID)
            .setTitle("TS24 App").setLoginPath("/login")
            .setBaseUrl("https://web.ts24.com.vn/web")
            .setHeaders(ObjectMap.create())
            .setQueries(ObjectMap.create())
            .setCfgMenuLinks(OdWebClient.menuConfig);

    public TSApp(ClsApiCfg config) {
        super(config);
    }

    /**
     * Returns the APP_UID
     */
    public final String getAppUID() {
        return APP_UID;
    }

    /**
     * Action login app
     *
     * @param username the username
     * @param password the password
     */
    protected ClsUser login(String username, String password) {
        return user().login(username, password);
    }

    @Override
    public ObjectMap loadMenus() {
        return menus().loadMenuForUser();
    }

    /**
     * Returns {@link OdCategory}
     */
    public OdCategory category() {
        return service(OdCategory.class, () -> new OdCategory(config()));
    }

    /**
     * Returns {@link OdCategory}
     */
    public OdCategorySub categorySub() {
        return service(OdCategorySub.class, () -> new OdCategorySub(config()));
    }

    /**
     * Returns {@link OdFile}
     */
    public OdFile file() {
        return service(OdFile.class, () -> new OdFile(config()));
    }

    /**
     * Returns {@link OdFollow}
     */
    public OdFollow follow() {
        return service(OdFollow.class, () -> new OdFollow(config()));
    }

    /**
     * Returns {@link OdHelpdeskTeam}
     */
    public OdHelpdeskTeam team() {
        return service(OdHelpdeskTeam.class, () -> new OdHelpdeskTeam(config()));
    }

    /**
     * Returns {@link OdMailTemplate}
     */
    public OdMailTemplate mailTemplate() {
        return service(OdMailTemplate.class, () -> new OdMailTemplate(config()));
    }

    /**
     * Returns {@link OdMessage}
     */
    public OdMessage message() {
        return service(OdMessage.class, () -> new OdMessage(config(), file()));
    }

    /**
     * Returns {@link OdPartner}
     */
    public OdPartner partner() {
        return service(OdPartner.class, () -> new OdPartner(config()));
    }

    /**
     * Returns {@link OdPriority}
     */
    public OdPriority priority() {
        return service(OdPriority.class, () -> new OdPriority(config()));
    }

    public OdProduct product() {
        return service(OdProduct.class, () -> new OdProduct(config()));
    }

    /**
     * Returns {@link OdStage}
     */
    public OdStage stage() {
        return service(OdStage.class, () -> new OdStage(config()));
    }

    /**
     * Returns {@link OdTicketSubType}
     */
    public OdTicketSubType ticketSubjectType() {
        return service(OdTicketSubType.class, () -> new OdTicketSubType(config()));
    }

    /**
     * Returns {@link OdTicketTags}
     */
    public OdTicketTags ticketTags() {
        return service(OdTicketTags.class, () -> new OdTicketTags(config()));
    }

    /**
     * Returns {@link OdTicketType}
     */
    public OdTicketType ticketType() {
        return service(OdTicketType.class, () -> new OdTicketType(config()));
    }

    /**
     * Returns {@link OdTicket}
     */
    public OdTicket ticket() {
        return service(OdTicket.class, () -> new OdTicket(config(), message(), follow(), composeMsg()));
    }

    /**
     * Returns {@link OdMailComposeMsg}
     */
    public OdMailComposeMsg composeMsg() {
        return service(OdMailComposeMsg.class, () -> new OdMailComposeMsg(config()));
    }

    /**
     * Returns {@link OdUser}
     */
    public OdUser user() {
        return service(OdUser.class, () -> new OdUser(config()));
    }

    /**
     * Returns {@link OdTopic}
     */
    public OdTopic topic() {
        return service(OdTopic.class, () -> new OdTopic(config()));
    }

    /**
     * Returns {@link OdWkTeam}
     */
    public OdWkTeam wkTeam() {
        return service(OdWkTeam.class, () -> new OdWkTeam(config()));
    }

    public OdWebClient menus() {
        return service(OdWebClient.class, () -> new OdWebClient(config()));
    }
}