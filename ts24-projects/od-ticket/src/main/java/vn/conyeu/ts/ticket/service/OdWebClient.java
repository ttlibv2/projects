package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.domain.ClsMenu;

import java.util.function.Function;

public class OdWebClient extends OdTicketClient<ClsMenu> {
    public static final String LOAD_MENU_ID = "load_menus";

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
//        ClsUser clsUser = getUser().getOdooUser();
//        if(clsUser == null) return null;
//
//        ObjectMap hashes = clsUser.getCache_hashes();
//        if(hashes == null) return null;
//
//        else return hashes.getString(LOAD_MENU_ID);
        throw new UnsupportedOperationException();
    }

    public ObjectMap loadMenuForUser() {
//        Assert.notNull(settingSrv, "@setSettingSrv(SystemSettingSrv)");
//
//        ObjectMap setting = settingSrv.findByKey("load_od_menu").orElseThrow(()-> BaseException
//                .e404("load_setting").withMsg("Không tìm thấy cấu hình field [load_od_menu]")).getAs();
//
//        String menuId = Assert.notNull(getLoadMenuId(), "OdUser not find loadMenus='%s'");
//
//        ObjectMap menus = loadMenus(menuId);
//        String allMenuKey = String.join("|", menus.keySet());
//
//        ObjectMap mapNew = ObjectMap.create();
//
//        for(String key:setting.keySet()) {
//            String menuKey = setting.getString(key);
//            if(!menus.containsKey(menuKey)) {
//                throw BaseException.e404("no_menu")
//                        .withMsg("Không tìm thấy menu [%s] -> (%s)", menuKey, allMenuKey);
//            }
//
//            mapNew.set(key, menus.getMap(menuKey).get("link"));
//        }
//
//        return mapNew;
        throw new UnsupportedOperationException();
    }

    private ObjectMap loadMenus(String menuId) {
//        Assert.notNull(menuId, "@menuId");
//        Assert.notNull(menuId, "clsUser.cache_hashes.load_menus is null");
//        String loginUrl = joinApiUrl("webclient/load_menus/"+menuId);
//        ProxyOption option = OdHelper.fixOption(getUser());
//        ObjectMap map = restClient.get(loginUrl, option).getBody();
//
//        ObjectMap mapNew = ObjectMap.create();
//        for(String key:map.keySet()) {
//            ObjectMap mapObj = map.getMap(key);
//            mapObj.put("link", buildLink(mapObj));
//            mapNew.put(mapObj.getString("xmlid"), mapObj);
//        }
//
//        return mapNew;
        throw new UnsupportedOperationException();
    }

    private String buildLink(ObjectMap obj) {
        return "#id={id}&cids=1&menu_id="+obj.getString("id")+"&action="+obj.getString("actionID");
    }


}