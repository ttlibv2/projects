package vn.conyeu.ts.odcore.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.MapperHelper;
import vn.conyeu.commons.utils.Objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

public final class ClsHelper {
    public static final DateTimeFormatter ISO_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static volatile ObjectMap tsconfig;

    public static ObjectMap loadJson() {
        if(tsconfig == null) {
            try {
                File file = ResourceUtils.getFile("classpath:ts24_config.json");
                tsconfig = MapperHelper.readValue(file, new TypeReference<>() {});
            }
            //
            catch (IOException exp) {
                throw BaseException.e400("load_json")
                        .message("Không tìm thấy file cấu hình [ts24_config.json]");
            }
        }

        return tsconfig;

    }

    public static String getBase64FromText(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    public static Path loadFile(String fileSrc) throws FileNotFoundException {

        if (Objects.isBlank(fileSrc))
            throw new FileNotFoundException("@fileSrc không được để trống");

        Path path = Paths.get(fileSrc);
        if (!Files.exists(path) || Files.isDirectory(path)) {
            String msg = String.format("Đường dẫn `%s` không tồn tại.", fileSrc);
            throw new FileNotFoundException(msg);
        }
        return path;
    }

    public static byte[] readAllBytes(String diskFile) {
        try {
            return Files.readAllBytes(loadFile(diskFile));
        } //
        catch (FileNotFoundException exp) {
            throw BaseException.e400("FileNotFound")
                    .message(exp.getMessage())
                    .throwable(exp);
        } //
        catch (SecurityException | IOException exp) {
            throw BaseException.e400("SecurityException")
                    .message("Xảy ra lỗi đọc file `%s`", diskFile)
                    .arguments("exception", exp.getMessage())
                    .throwable(exp);
        }
    }

    public static String getBase64FromFile(String urlImage) {
        return Base64.getEncoder().encodeToString(readAllBytes(urlImage));
    }

    public static ClsPage fixPage(ClsPage page) {
        page = Objects.createIfNull(page, ClsPage::new);
        if (page.getLimit() == null) page.setLimit(80);
        return page;
    }

    public static boolean isBool(String str) {
        return Objects.notBlank(str) && (str.equals("true") || str.equals("false"));
    }

    public static LocalDateTime dateTime(Object date) {
        if(!(date instanceof String) || "false".equals(date)) return null;
        else return LocalDateTime.parse(toString(date), ISO_DATETIME);
    }

    public static String toString(Object object) {
        return object == null || "false".equals(object) ? null : object.toString();
    }

    public static <T> T getObj(Object obj, int index, Class<T> targetClass) {
        if (String.valueOf(obj).equals("false")) return null;
        Object objValue = null;

        if (obj instanceof List list) {
            objValue = list.get(index);
        } else {
            objValue = CollectionUtils.arrayToList(obj).get(index);
        }

        return MapperHelper.convert(objValue, targetClass);


    }

    public static void checkResponse(ClsApiCfg cfg, Object response) {
        ObjectMap detail = ObjectMap.setNew("client_response", response);
        String msgPrefix = "Đã xảy ra lỗi từ hệ thống [%s]. ".formatted(cfg.getApiTitle());

        BaseException exp = BaseException.e500("ts_api")
                .detail("ts_api", cfg.getServiceName())
                .detail("ts_detail", detail);

        if (response instanceof String html) {

            if (html.contains("invalid CSRF token")) {
                throw exp.status(401).detail("ts_code", "csrf_token")
                        .message(msgPrefix+"csrf_token bị sai -> vui lòng cập nhật.");
            }

        }
        else if (response instanceof ObjectMap obj) {

            if (CollectionUtils.isEmpty(obj))
                throw exp.message("checkResponse -> response is null");

            if (obj.containsKey("error")) {
                obj = obj.getMap("error");
                String msg = obj.getString("message");
                String keyw = obj.getString("data.name");

                if (Objects.equals(keyw, "odoo.http.SessionExpiredException")) {
                    throw SessionExpired(msgPrefix, exp);
                }

                if (Objects.equals(keyw, "odoo.exceptions.AccessError")) {
                    msg = obj.getString("data.message", "Rất tiếc, bạn không được phép truy cập tài liệu này");
                    throw exp.status(403).detail("ts_code", "AccessError").message(msgPrefix+msg);
                }

                throw exp.code("client_error").message(msgPrefix+msg);
            }

        }

    }

    public static BaseException SessionExpired(String msgPrefix, BaseException exp) {
        return exp.status(401).detail("ts_code", "token_expired")
                .message(msgPrefix+"Mã xác thực không đúng hoặc đã hết hạn. Vui lòng kiểm tra lại");
    }

    public static void updateConfig(ClsApiCfg userApi, ClsUser clsUser) {
        userApi.setClsUser(clsUser);
        userApi.setCookieValue(clsUser.getCookie());
        userApi.setCsrfToken(clsUser.getCsrfToken());
    }


}