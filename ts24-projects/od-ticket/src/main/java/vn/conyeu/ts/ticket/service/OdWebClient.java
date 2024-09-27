package vn.conyeu.ts.ticket.service;

import org.springframework.http.MediaType;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.domain.ClsMenu;

import java.util.function.Function;

public class OdWebClient extends OdTicketClient<ClsMenu> {
    public static final String LOAD_MENU_ID = "load_menus";

    // menu -> [ xmlid, urlModel ]
    public static final ObjectMap menuConfig = ObjectMap
            .setNew("contacts.menu_contacts", "od_partner")
            .set("sh_all_in_one_helpdesk.helpdesk_tickets_menu", "od_ticket");

    public OdWebClient(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        throw new UnsupportedOperationException();
    }

    public String getBasePath() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Class<ClsMenu> getDomainCls() {
        return ClsMenu.class;
    }

    @Override
    protected Function<ObjectMap, ClsMenu> mapToObject() {
        return ClsMenu::from;
    }

    private String getLoadMenuId() {
        return cfg().getClsUser().getMenuId();
    }

    public ObjectMap loadMenuForUser() {
        ObjectMap menus = loadMenus();

        ObjectMap links = ObjectMap.create();

        for(String menuKey : menuConfig.keySet()) {
            if(!menus.containsKey(menuKey)) {
                throw BaseException.e404("no_menu")
                        .message("Không tìm thấy menu [%s]", menuKey);
            }

            String model = menuConfig.getString(menuKey);
            String link = menus.getMap(menuKey).getString("link");
            links.set(model, link);
        }

        return links;
    }

    private ObjectMap loadMenus() {
        String menuId = cfg().getClsUser().getMenuId();
        Asserts.notNull(menuId, "clsUser.cache_hashes.load_menus is null");

        String menuUrl = "%s/%s/%s".formatted(getApiUrl(), "webclient/load_menus", menuId);
        String data = createClient().mutate().defaultContentType(MediaType.ALL)
                .build().get().uri(menuUrl).retrieve().bodyToMono(String.class)
                .blockOptional().orElseThrow();

        ObjectMap menus  = ObjectMap.fromJson(data);
        ObjectMap newMap = new ObjectMap();
        for(String key:menus.keySet()) {
            ObjectMap object = menus.getMap(key);
            object.set("link", buildLink(object));

            String newKey = object.getString("xmlid");
            newMap.set(newKey, object);
        }

        return newMap;
    }

    private String buildLink(ObjectMap obj) {
        return "#id={id}&cids=1&menu_id="+obj.getString("id")+"&action="+obj.getString("actionID");
    }


}