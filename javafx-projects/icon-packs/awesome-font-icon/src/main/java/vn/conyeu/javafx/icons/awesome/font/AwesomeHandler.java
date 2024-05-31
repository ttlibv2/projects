package vn.conyeu.javafx.icons.awesome.font;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import vn.conyeu.commons.utils.FileUtils;
import vn.conyeu.javafx.icons.font.LazyFontHandler;
import vn.conyeu.javafx.icons.font.SimpleFontDesc;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AwesomeHandler extends LazyFontHandler {

    protected AwesomeHandler(String prefix, String family, String orgName, String version, String fileName) {
        super(prefix, family, orgName, version, fileName, self -> {
            List<SimpleFontDesc> icons = new ArrayList<>();
           // log.warn("{}", getClass().getName());

            //for (Aw icon : AwesomeIcon.allIcon()) {
            //    self.registerIcon(icon.cloneWithPrefix(prefix));
            //}
        });

        super.lazyRegisterIcon = self -> {
            String uri = "/META-INF/resources/" + orgName + "/" + version + "/fonts/icons.desc";
            InputStream input = getClass().getResourceAsStream(uri);
            if(input == null) throw new UnsupportedOperationException();
            List<String> list = IOUtils.readLines(input, StandardCharsets.UTF_8);
            for(String line: list) self.registerIcon(AwesomeDesc.parse(prefix, line));
        };
    }

    public String resolveStyleClass(String description) {
        return description;
    }









    //@ServiceProviderFor(IkonHandler.class)
    public static class FAT extends AwesomeHandler {

        public FAT() {
            super("fat-", "Font Awesome 6 Pro Thin",
                    "fontawesome",  "v6.5.1",  "fa-thin-100.ttf");
        }

    }

    //@ServiceProviderFor(IkonHandler.class)
    public static class FAL extends AwesomeHandler {

        public FAL() {
            super("fal-", "Font Awesome 6 Pro Light",
                    "fontawesome", "v6.5.1", "fa-light-300.ttf");
        }

    }

    //@ServiceProviderFor(IkonHandler.class)
    public static class FAB extends AwesomeHandler {

        public FAB() {
            super("fab-", "Font Awesome 6 Brands Regular",
                    "fontawesome",  "v6.5.1",  "fa-brands-400.ttf");
        }

    }

    //@ServiceProviderFor(IkonHandler.class)
    public static class FAD extends AwesomeHandler {

        public FAD() {
            super("fad-", "Font Awesome 6 Duotone Solid",
                    "fontawesome",  "v6.5.1",  "fa-duotone-900.ttf");
        }

    }

    //@ServiceProviderFor(IkonHandler.class)
    public static class FAR extends AwesomeHandler {

        public FAR() {
            super("far-", "Font Awesome 6 Pro Regular",
                    "fontawesome",  "v6.5.1",  "fa-regular-400.ttf");
        }

    }

    //@ServiceProviderFor(IkonHandler.class)
    public static class FAS extends AwesomeHandler {

        public FAS() {
            super("fas-", "Font Awesome 6 Pro Solid",
                    "fontawesome",  "v6.5.1",  "fa-solid-900.ttf");
        }

    }


}