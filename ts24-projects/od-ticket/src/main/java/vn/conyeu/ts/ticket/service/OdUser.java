package vn.conyeu.ts.ticket.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.restclient.ClientBuilder;
import vn.conyeu.restclient.ClientUtil;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.odcore.domain.ClsUserContext;
import vn.conyeu.ts.odcore.domain.IOdUserLogin;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;
import vn.conyeu.ts.ticket.domain.ClsNameSearchOption;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class OdUser extends OdTicketClient<ClsUser> implements IOdUserLogin {

    public OdUser(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "res.users";
    }

    @Override
    protected final Class<ClsUser> getDomainCls() {
        return ClsUser.class;
    }

    @Override
    protected final Function<ObjectMap, ClsUser> mapToObject() {
        return ClsUser::from;
    }

    /**
     * Find user by id
     * @param userId the id of user
     * */
    public Optional<ClsUser> findById(Long userId) {
        List<String> fields = getFields("detail", getModel());
        List<ClsUser> all = read(List.of(userId, fields));
        return Optional.ofNullable(Objects.getItemAt(all, 0));
    }

    /**
     * Find user by id
     * @param listUserId the id of user
     * */
    public List<ClsUser> search(List<Long> listUserId) {
        Object[] args = new Object[] {"id", "in", listUserId};
        return nameSearch(ClsNameSearchOption.withArgs(args));
    }

    /**
     * Search user by keyword
     * @param keyword the keyword to search
     * */
    public List<ClsUser> search(String keyword) {
        return nameSearch(keyword);
    }

    /**
     * Search user by {@link ClsFilterOption}
     * @param filterOption the option filter
     * */
    public List<ClsUser> search(ClsFilterOption filterOption) {
        return searchRead(filterOption);
    }

    /**
     * Search user by {@link ObjectMap}
     * @param searchObj the option filter
     * */
    public List<ClsUser> search(ObjectMap searchObj) {
        return searchRead(searchObj);
    }

    public ClsUser login() {
        return login(cfg.getUserName(), cfg.getPassword());
    }

    @Override
    public ClsUser login(ObjectMap info) {
        return login(info.getString("username"),
                info.getString("password"));
    }

    /**
     * Login page
     * @param userName the user login
     * @param password the pass login
     * */
    public ClsUser login(String userName, String password) {
        String loginUrl = cfg.getLoginPath();

        ClientBuilder builder = applyDefaultBuilder()
                .defaultContentType(MediaType.TEXT_HTML)
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/117.0");

        //-- B1: get input login form
        ResponseEntity<String> entityB1 = builder.clone().build().get()
                .uri(loginUrl).retrieve().toEntity(String.class)
                .blockOptional().orElseThrow();

        // fix info
        ObjectMap info = ObjectMap.create();

        Optional.ofNullable(entityB1.getBody())
                .map(Jsoup::parse)
                .map(doc -> doc.selectFirst("form[action=/web/login]"))
                .map(doc -> doc.select("input[type=hidden]"))
                .orElseThrow().forEach(el -> info.set(el.attr("name"), el.val()));

        info.set("login", userName).set("password", password);

        //-- B2: send login
        log.warn("-- B2: send login");
        MultiValueMap<String, String> formData = ClientUtil.toMultiValueMap(info);
        ResponseEntity<String> entityB2 = builder.clone()
                .disableCookieManagement(false)
                .build().post().uri(loginUrl)
                .header("cookie", getSessionId(entityB1.getHeaders()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve().toEntity(String.class)
                .blockOptional().orElseThrow();

        Document jsDoc = Optional.ofNullable(entityB2.getBody())
                .map(Jsoup::parse).orElseThrow();

        Element loginEl = jsDoc.selectFirst("form[action=/web/login]");
        if(loginEl != null) {
            Elements els = loginEl.select("p.alert.alert-danger");

            if(!els.isEmpty()) {
                String msg = els.get(0).text().trim();
                String newMsg = msg;
                if(msg.contains("Wrong login/password") || msg.contains("Sai tên")) {
                    newMsg = "<b>Tài khoản đăng nhập không đúng.</b>" +
                            "<br> Vui lòng kiểm tra lại !!";
                }

                throw BaseException.e401("login_ts24").message(newMsg)
                        .arguments("ts24_msg", msg);
            }

            throw BaseException.e401("login_ts24")
                    .message("Lỗi  hệ thống -> Đã xảy ra lỗi đăng nhập");
        }

        //-- oe_reset_password_form
        Element formResetPwd = jsDoc.selectFirst("form.oe_reset_password_form");
        if(formResetPwd != null) {
            throw BaseException.e401("login_ts24")
                    .message("Vui lòng mở trang web '%s' để thay đổi mật khẩu", getApiUrl());
        }


        //-- B3: lấy thông tin user
        log.warn("-- B3: lấy thông tin user");
        ResponseEntity<String> entityB3 = builder.clone().build().get().uri("?")
                .header("cookie", getSessionId(entityB2.getHeaders()))
                .header("content-type", MediaType.TEXT_HTML_VALUE)
                .retrieve().toEntity(String.class)
                .blockOptional().orElseThrow();

        ObjectMap obj = Optional.ofNullable(entityB3.getBody())
                .map(this::getScriptObj).orElseThrow();

        ClsUser user = new ClsUser();
        user.setId(obj.getLong("uid"));
        user.setCompany_id(obj.get("company_id"));
        user.setName(obj.getString("name"));
        user.setEmail(obj.getString("username"));
        user.setContext(obj.get("user_context", ClsUserContext.class));
        user.setCsrfToken(obj.getString("csrf_token"));
        user.setCookie(extractCookie(entityB3.getHeaders()));
        user.setPartner_id(obj.getLong("partner_id", null));
        user.setCache_hashes(obj.getMap("cache_hashes"));
        user.setTsUser(obj);

        // update to config
        cfg.setClsUser(user);
        cfg.setCookieValue(user.getCookie());
        cfg.setCsrfToken(user.getCsrfToken());

        return user;
    }

    private String extractCookie(HttpHeaders headers) {
        List<String> list = headers.get(HttpHeaders.SET_COOKIE);
        if(Objects.isEmpty(list)) return null;
        return list.stream().filter(text -> text.startsWith("session_id="))
                .findFirst() //.map(tx -> tx.replace("session_id=", ""))
                .map(s -> s.split(";")[0]).orElse(null);
    }

    private String getSessionId(HttpHeaders headers) {
        List<String> list = headers.get(HttpHeaders.SET_COOKIE);
        if(Objects.isEmpty(list)) return null;
        return list.stream().filter(s->s.startsWith("session_id="))
                .findFirst().map(s -> s.split(";")[0])
              //  .map(tx -> tx.replace("session_id=", ""))
                .orElseThrow(() -> BaseException.e500("invalid_session").message("Không tìm thấy session_id"));
    }

    private ObjectMap getScriptObj(String html) {
        Function<String, String> fnc = tx -> tx.replace("var odoo =", "")
                .replace("odoo.__session_info__ =", "")
                .replaceAll("\n", "")
                .replace(",                    }", "}")
                .replace("'", "\"")
                .replaceAll(";", "");

        ObjectMap obj = ObjectMap.create();
        Jsoup.parse(html)
                .getElementsByTag("script").stream()
                .map(el->el.html().trim())
                .filter(tx->tx.contains("csrf_token")||tx.startsWith("odoo.__session_info__"))
                .map(fnc).map(ObjectMap::fromJson)
                .toList().forEach(obj::putAll);
        return obj;
    }



}