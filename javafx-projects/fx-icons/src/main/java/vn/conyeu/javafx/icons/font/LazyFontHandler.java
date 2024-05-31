package vn.conyeu.javafx.icons.font;

import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LazyFontHandler extends FontHandler {
    protected final Map<String, FontDesc> icons = new HashMap<>();
    protected Consumer<LazyFontHandler> lazyRegisterIcon;
    protected boolean existsRegisterIcon = false;
    protected final String fontFamily;
    protected final String fontPrefix;
    protected final String version;
    protected final String orgName;
    protected final String fileName;
    protected final String fontUri;

    public LazyFontHandler(String prefix, String family, String orgName, String version, String fileName,
                           Consumer<LazyFontHandler> lazyRegisterIcon) {
        this.fontPrefix = Asserts.notBlank(prefix, "@prefix");
        this.fontFamily = Asserts.notBlank(family, "@family");
        this.version = Asserts.notBlank(version, "@version");
        this.orgName = Asserts.notBlank(orgName, "@orgName");
        this.fileName = Asserts.notBlank(fileName, "@fileName");
        this.fontUri = Asserts.notBlank(buildFontUri());
        this.lazyRegisterIcon = lazyRegisterIcon;
    }

    public void registerIcon(FontDesc icon) {
        icons.put(icon.getName(), icon);
    }

    @Override
    public boolean supports(String description) {
        return description != null && description.startsWith(fontPrefix);
    }

    @Override
    public FontDesc resolve(String description) {
        asyncInitializeData();
        if (!icons.containsKey(description)) {
            throw Objects.newIllegal("FontIcon '%s' without description " +
                    "'%s' and version '%s' is invalid!", fontFamily, description, version);
        }
        return icons.get(description);
    }

    protected String buildFontUri() {
        return "/META-INF/resources/" + orgName + "/" + version + "/fonts/" + fileName;
    }

    @Override
    public final URL getFontResource() {
        return getClass().getResource(fontUri);
    }

    @Override
    public final InputStream getFontResourceAsStream() {
        return getClass().getResourceAsStream(fontUri);
    }

    public final String getFontFamily() {
        return fontFamily;
    }

    public final String getPrefix() {
        return fontPrefix;
    }

    public final String getVersion() {
        return version;
    }

    public final String getOrgName() {
        return orgName;
    }

    public final String getFileName() {
        return fileName;
    }

    public final String getFontUri() {
        return fontUri;
    }

    @Override
    protected final void asyncInitializeData() {
        super.asyncInitializeData();
        if (!existsRegisterIcon && lazyRegisterIcon != null) {
            existsRegisterIcon = true;
            icons.clear();
            lazyRegisterIcon.accept(this);
        }
    }

}