package vn.conyeu.ts.dtocls;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;

import java.io.Serializable;

@Getter @Setter
public class SaveUserApiDto implements Serializable {
    @NotNull(message = "action.notNull")
    private Action action; // copy | edit

    @NotNull(message = "action.notNull")
    private Long source_id;

    private ObjectMap api_info;
    private ObjectMap user_api;

    public enum Action {copy_api, edit_api, edit_user }

    public boolean hasCopyApi() {
        return action == Action.copy_api;
    }

    public boolean hasEditApi() {
        return action == Action.edit_api;
    }

    public boolean hasEditUser() {
        return action == Action.edit_user;
    }

    public ApiInfo asInfo(boolean checkNull) {
        if(api_info == null) {
            if(!checkNull)return null;
            else throw BaseException.e400("miss")
                    .message("Dữ liệu truyền thiếu thông tin [api_info]");
        }
        return api_info.asObject(ApiInfo.class);

    }

    public UserApi asCredential(boolean checkNull) {
        if(user_api == null) {
            if(!checkNull)return null;
            else throw BaseException.e400("miss")
                        .message("Dữ liệu truyền thiếu thông tin [user_api]");
        }
        return user_api.asObject(UserApi.class);
    }
}