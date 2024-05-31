import vn.conyeu.javafx.icons.awesome.font.AwesomeHandler;
import vn.conyeu.javafx.icons.font.FontHandler;

module awesome.font.icon {
    exports vn.conyeu.javafx.icons.awesome.font to fx.icons;
    requires fx.icons;
    requires org.slf4j;
    requires static lombok;
    requires java.commons;

    provides FontHandler
            with AwesomeHandler.FAT,
                    AwesomeHandler.FAB,
                    AwesomeHandler.FAD,
                    AwesomeHandler.FAR,
                    AwesomeHandler.FAS,
                    AwesomeHandler.FAL;
}