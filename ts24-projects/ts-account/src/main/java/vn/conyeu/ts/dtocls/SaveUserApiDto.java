package vn.conyeu.ts.dtocls;

import lombok.Getter;
import lombok.Setter;
import vn.conyeu.common.exception.BadRequest;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.odcore.domain.ClsUser;
import java.io.Serializable;
import static vn.conyeu.commons.utils.Objects.setIfNotNull;

@Getter @Setter
public class SaveUserApiDto implements Serializable {
    private String username;
    private String password;
    private Boolean auto_login;
    private Boolean allow_edit;
    private String cookie;
    private String csrf_token;
    private ClsUser user_info;


    public boolean hasReset(UserApi user) {
        return  (username != null && !username.equals(user.getUserName())) ||
                (password != null && !password.equals(user.getPassword()));
    }

    public UserApi asUser(boolean validate) {
        if(validate) {
            ObjectMap object = new ObjectMap();
            if(username == null) object.set("username", "username.empty");
            if(password == null) object.set("password", "password.empty");
            if(!object.isEmpty()) throw new BadRequest().details(object)
                    .message("Vui lòng truyền đầy đủ thông tin");
        }

        UserApi user = new UserApi();
        user.setUserName(username);
        user.setPassword(password);
        user.setAutoLogin(auto_login);
        user.setAllowEdit(allow_edit);

        if(Objects.equals(allow_edit, true)) {
            user.setCsrfToken(csrf_token);
            user.setCookie(cookie);
            user.setUserInfo(user_info);
        }

        return user;

    }

    public UserApi update(UserApi user) {
        setIfNotNull(username, user::setUserName);
        setIfNotNull(password, user::setPassword);
        setIfNotNull(allow_edit, user::setAllowEdit);
        setIfNotNull(auto_login, user::setAutoLogin);

        if(Objects.equals(allow_edit, true)) {
            setIfNotNull(csrf_token, user::setCsrfToken);
            setIfNotNull(cookie, user::setCookie);
            setIfNotNull(user_info, user::setUserInfo);
        }

        else if(hasReset(user)) {
            user.setCookie(null);
            user.setCsrfToken(null);
            user.setUserInfo(null);
        }

        return user;
    }
}