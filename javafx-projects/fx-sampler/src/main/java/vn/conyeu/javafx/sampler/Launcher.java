package vn.conyeu.javafx.sampler;

import fr.brouillard.oss.cssfx.CSSFX;
import fr.brouillard.oss.cssfx.api.URIToPathConverter;
import fr.brouillard.oss.cssfx.impl.log.CSSFXLogger;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.scenicview.ScenicView;
import vn.conyeu.commons.utils.Lists;
import vn.conyeu.javafx.controls.*;
import vn.conyeu.javafx.controls.flex.FlexAlignContent;
import vn.conyeu.javafx.controls.flex.FlexAlignItem;
import vn.conyeu.javafx.icons.font.FontIcon;
import vn.conyeu.javafx.sampler.basic.DefaultExceptionHandler;
import vn.conyeu.javafx.sampler.basic.Uris;
import vn.conyeu.javafx.sampler.event.BrowseEvent;
import vn.conyeu.javafx.sampler.event.EventBus;
import vn.conyeu.javafx.sampler.event.HotkeyEvent;
import vn.conyeu.javafx.sampler.event.Listener;
import vn.conyeu.javafx.sampler.layout.AppWindow;
import vn.conyeu.javafx.theme.PrimerLight;
import vn.conyeu.javafx.utils.Colors;

import java.net.URI;
import java.nio.file.Paths;
import java.util.*;


@Slf4j
public class Launcher extends Application {
    final AppWindow root = new AppWindow();
    boolean showView = false;

    public static void main(String[] args) {
        launch(args);
    }

    void addNodes(Node... nodes) {
        root.addNodes(nodes);
    }

    @Override
    public void start(Stage stage) {

        Thread.currentThread().setUncaughtExceptionHandler(new DefaultExceptionHandler(stage));

        var antialiasing = Platform.isSupported(ConditionalFeature.SCENE3D)
                ? SceneAntialiasing.BALANCED
                : SceneAntialiasing.DISABLED;

        var scene = new Scene(root, AppWindow.MIN_WIDTH + 80, 450, false, antialiasing);
        var cssUri = Uris.getUrl(PrimerLight.class, "primer-light.css").toExternalForm();
        scene.setUserAgentStylesheet("");
        scene.getStylesheets().add(cssUri);
        //scene.getStylesheets().add(Uris.getUrl(Launcher.class.getClassLoader(), "app.css").toExternalForm());

        scene.setOnKeyPressed(this::dispatchHotkeys);


        appendUI(stage);

        startCssFX(scene);

        stage.setScene(scene);
        stage.setTitle("Simple JavaFx");

        // loadIcons(stage);
        stage.setResizable(true);
        stage.setOnCloseRequest(t -> Platform.exit());

        // register event listeners
        EventBus.subscribeEvent(BrowseEvent.class, this::onBrowseEvent);

        Platform.runLater(() -> {
            stage.show();
            stage.requestFocus();
            if (showView) ScenicView.show(scene);
        });
    }


    private void startCssFX(Scene scene) {
        URIToPathConverter fileUrlConverter = uri -> {
            try {
                if (uri != null && uri.startsWith("file:")) {
                    return Paths.get(URI.create(uri));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };

        CSSFX.addConverter(fileUrlConverter).start();
        CSSFXLogger.setLoggerFactory(loggerName -> (level, message, args) -> {
            //if (level.ordinal() <= CSSFXLogger.LogLevel.INFO.ordinal()) {
            // System.out.println("[" + level + "] CSSFX: " + String.format(message, args));
            // }
        });
        CSSFX.start(scene);
    }

    private void dispatchHotkeys(KeyEvent event) {
        for (KeyCodeCombination k : SUPPORTED_HOTKEYS) {
            if (k.match(event)) {
                EventBus.publishEvent(new HotkeyEvent(k));
                return;
            }
        }
    }

    @Listener
    private void onBrowseEvent(BrowseEvent event) {
        getHostServices().showDocument(event.getUri().toString());
    }

    public static final List<KeyCodeCombination> SUPPORTED_HOTKEYS = List.of(
            new KeyCodeCombination(KeyCode.SLASH),
            new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN),
            new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN)
    );


    private void appendUI(Stage stage) {


         showView = true;

        //demoRating();
        //demoToolBox();
        //demoFlex();
        //demoTag();
        //demoTagField();
        //demoInputGroup();
        //demoDivider();
//        AbstractMFXListView;
//        VirtualFlow;
//        IMultipleSelectionModel;
//        IMultipleSelectionModel;
//        SelectionModel;
//        SynchronizedIntegerProperty;
//        BindingManager;
//        MFXListViewSkin ;
//        MFXListCell

        // demoMaskField();
        // demoSlider();
        //demoButton();
        //demoMeterGroup();
        //demoAvatar();

    }

    private void demoAvatar() {
        Avatar avatar = new Avatar();
        avatar.setLabel("P");
        avatar.setAvatarShape(Avatar.AvatarShape.square);
        avatar.setBadge(() -> {
            Badge badge = new Badge();
            badge.setType(Badge.BadgeType.DOT);
            return badge;
        });
        addNodes(avatar);
    }


    private void demoMeterGroup() {
        //MeterGroup meter = new MeterGroup();
        //meter.addItem("Apps", "#34d399", 25);//.iconLiteral("far-apple-whole");
        //meter.addItem("Messages", "#fbbf24", 10);//.iconLiteral("far-message");
        //meter.addItem("Media", "#60a5fa", 15);//.iconLiteral("far-user");;
        //meter.addItem("System", "#c084fc", 35);//.iconLiteral("far-home");;


        GroupBar group = new GroupBar();
        addNodes(group);
    }


   void colorToString() {


       StringBuilder colors = new StringBuilder();
       colors.append("// scss-docs-start color-variables\n");

       StringBuilder colorMap = new StringBuilder();
       colorMap.append("// scss-docs-start colors-map\n").append("$colors: (\n");

       StringBuilder colorScale = new StringBuilder();

       colorScale.append("\n// fusv-disable\n\n// scss-docs-start colors-scale\n");


       Map<String, Color> allColor = new HashMap<>();
       allColor.put("primary", Color.valueOf("#0d6efd"));
       allColor.put("secondary", Color.valueOf("#6610f2"));
       allColor.put("success", Color.valueOf("#6f42c1"));
       allColor.put("info", Color.valueOf("#d63384"));
       allColor.put("warning", Color.valueOf("#dc3545"));
       allColor.put("danger", Color.valueOf("#fd7e14"));
       allColor.put("help", Color.valueOf("#ffc107"));


       for (Map.Entry<String, Color> entry : allColor.entrySet()) {
           String entryKey = entry.getKey();
           String colName = "$" + entryKey;
           String colorHex = Colors.colorToHex(entry.getValue());


           colors.append(colName).append(": ").append(colorHex).append(" !default;\n");
           colorMap.append("\"").append(entryKey).append("\": ").append(colName).append(",\n");


           // scss-docs-start color-variables
           colorScale.append("\n// scss-docs-start ")
                   .append(entryKey).append("-color-variables");

           for (int i = 0; i <= 5; i++) {
               String str = "utils.tint(" + colName + ", " + (5 - i) + " * 19%)";
               colorScale.append("\n").append(colName).append("-").append(i);
               colorScale.append(": ").append(i == 5 ? colName : str).append(" !default;");
           }

           for (int i = 1; i <= 4; i++) {
               String str = "utils.shade(" + colName + ", " + i + " * 15%)";
               colorScale.append("\n").append(colName).append("-").append(i + 5);
               colorScale.append(": ").append(str).append(" !default;");
           }

           colorScale.append("\n// scss-docs-end ")
                   .append(entryKey).append("-color-variables");

           // scss-docs-start colors-map
           colorScale.append("\n\n// scss-docs-start ")
                   .append(entryKey).append("-colors-map");
           colorScale.append("\n").append(colName).append("s: (\n");
           for (int i = 0; i <= 9; i++) {
               colorScale.append("\"")
                       .append(entryKey).append("-")
                       .append(i)
                       .append("\": ").append(colName)
                       .append("-").append(i)
                       .append(i == 9 ? "" : ",").append("\n");
           }
           colorScale.append(");").append("\n// scss-docs-end ")
                   .append(entryKey).append("-colors-map");

       }

       colors.append("\n// scss-docs-end color-variables\n");
       colorMap.append(") !default;\n// scss-docs-end colors-map\n");
       colorScale.append("\n// scss-docs-end colors-scale\n// fusv-enable\n");

       log.warn("\n{}\n\n{}\n\n{}", colors, colorMap, colorScale);


   }


    protected static class GroupBar extends FlexBox {
        int totalPercent = 0;
        Region emptyBar;

        public GroupBar() {
            super(0);
            getStyleClass().setAll("line-group2");
            setDefaultFlexGrow(1);
            setDefaultFlexShrink(1);
            setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            setAlignContent(FlexAlignContent.STRETCH);
            setAlignItem(FlexAlignItem.STRETCH);
            getChildren().addListener((ListChangeListener) c -> {
                List<Node> nodes = getChildren();
                if (nodes.size() > 1) {
                    nodes.sort(Comparator.comparingInt(FlexBox::getFlexOrder));
//                    for (int pos = 0; pos < nodes.size()-1; pos++) {
//                        final Node node = nodes.get(pos);
//                        NodeUtils.removeStyleClass(node, "first", "last");
//                        if (pos == 0) NodeUtils.addStyleClass(node, "first");
//                        else if (pos == nodes.size() - 2) NodeUtils.addStyleClass(node, "last");
//                        else NodeUtils.addStyleClass(node, "middle");
//                    }
                }

            });

            emptyBar = buildRectangle("bar");
            emptyBar.getStyleClass().add("empty-bar");
            addChild(emptyBar, 999999);

            double[] widths = new double[]{10, 8, 65, 65, 40};
            for (int i = 0; i < widths.length; i++) {
                final double percent = calcPercent(widths[i], 0, 350);
                totalPercent += percent;

                int si = (int) percent;

                Region bar = buildRectangle("bar");
                bar.getStyleClass().add("bar-" + i);
                addFlexChild(bar, si, si, "auto");

            }

            log.warn("{}", totalPercent);
            FlexBox.setFlexGrow(emptyBar, 100 - totalPercent);
            FlexBox.setFlexShrink(emptyBar, 100 - totalPercent);
        }


        private double calcPercent(double value, double min, double max) {
            return ((value - min) / (max - min)) * 100;
        }

        protected Region buildRectangle(String styleClass) {
            Region rectangle = new Region();
            rectangle.getStyleClass().setAll(styleClass);
            rectangle.setMinSize(1, 1);
            //rectangle.setStroke(Color.valueOf("red"));
            //rectangle.setStrokeLineCap(StrokeLineCap.ROUND);
            //rectangle.setStrokeLineJoin(StrokeLineJoin.ROUND);
            //rectangle.setStrokeType(StrokeType.INSIDE);
            //rectangle.setStrokeWidth(0);
            //rectangle.setHeight(6);
            // rectangle.setWidth(6);
            return rectangle;
        }

    }


    private void demoButton() {
        Button button = new Button("Button");
        button.getStyleClass().add("btn1");
        button.setEnableRipple(true);
        button.setDisable(true);

        addNodes(button, new Button("Button"));
    }

    private void demoSlider() {
        RangeSlider slider = new RangeSlider();
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(5);
        slider.setBlockIncrement(5);
        slider.setVisibleRangeBar(false);

        Slider slider2 = new Slider();
        slider2.setShowTickMarks(true);
        slider2.setShowTickLabels(true);
        slider2.setMajorTickUnit(5);
        slider2.setBlockIncrement(5);

        addNodes(slider, slider2);
    }

    private void demoMaskField() {

        addNodes(
                MaskField.createPhoneNumberField(),
                MaskField.createSSNField(),
                new Button("Button"));
    }

    private void demoDivider() {
        Divider divider = new Divider();
        divider.setLabel("label");
        divider.setGraphic(new FontIcon("far-home:32:red"));
        divider.setVertical(false);

        Separator sep = new Separator();


        addNodes(divider, sep);
    }

    private void demoInputGroup() {
        InputGroup<TextField> ig = InputGroup.field();
        ig.setMaxWidth(Double.MAX_VALUE);
        ig.getLeadingNodes().setAll(new FontIcon("far-user"), new FontIcon("far-user"), new FontIcon("far-user"));
        ig.getTrailingNodes().setAll(new FontIcon("far-xmark"), new FontIcon("far-xmark"), new FontIcon("far-xmark"));
        ig.getLeftNodes().setAll(new FontIcon("far-home"), new FontIcon("far-home"), new FontIcon("far-home"));
        ig.getRightNodes().setAll(new FontIcon("far-download"), new FontIcon("far-download"), new FontIcon("far-download"));

        SearchBar searchBar = new SearchBar();
        searchBar.setMinCharacter(10);
        searchBar.setOnChangeText(e -> log.warn(e.getText()));

        addNodes(ig, searchBar);
    }

    private void demoTagField() {
        TagField<String> field = new TagField();
        field.getItems().addAll("Item 1", "Item 2");

        addNodes(field);

    }

    void demoTag() {
        Tag<String> tag = new Tag<>();
        tag.setValue("Tag 001");
        tag.setConverter(new StringConverter<>() {
            @Override
            public String toString(String str) {
                return str == null ? null : "tag --- " + str;
            }

            @Override
            public String fromString(String str) {
                return str;
            }
        });
        tag.setMode(Tag.TagMode.CHECKED);
        tag.setOnChecked(e -> log.warn("checked --> {}", e.isChecked()));
        tag.setOnClose(e -> log.warn("close"));
        tag.setOnClick(e -> log.warn("click"));


        addNodes(tag);
    }

    void demoToolBox() {

        ToolBox toolBox = new ToolBox();
        toolBox.setGraphic(new FontIcon("far-user"));
        toolBox.setCloseNode(new FontIcon("far-xmark"));
        toolBox.setLabel("Zalo Web");
        toolBox.addLeadingFontIcons("fas-turn-left", "fas-turn-right");
        toolBox.addTrailingFontIcons("fas-minus", "fas-maximize");

        Button button = new Button("Button");
        addNodes(toolBox, button);
    }

    void demoRating() {
        Rating rating = new Rating();
        rating.setRating(0);
        addNodes(rating);
    }

    void demoFlex() {
        // root.vBox.setDefaultFlexGrow(1);
        root.vBox.setDefaultFlexShrink(1);
        root.vBox.setPrefWidth(386);


        //FlexBox flexBox = new FlexBox(5, 5);

        //flexBox.setDefaultFlexGrow(1);
        //flexBox.setStyle("-fx-border-color: red");

        List<Double> heights = Lists.newList(100.0, 150.0, 160.0, 52.0, 25.0, 10.0);
        Button button = null;
        for (int i = 0; i < 5; i++) {
            Collections.shuffle(heights);
            Collections.shuffle(heights);
            Collections.shuffle(heights);
            button = new Button("button " + i);
            // button.setPrefWidth(150);
            button.getStyleClass().add("btn_" + i);
            //button.setPrefHeight(heights.get(0));
            //flexBox.getChildren().add(button);
            addNodes(button);
        }

        Button finalButton = button;
        button.setOnAction(e -> root.vBox.setPrefWidth(381));


    }


}