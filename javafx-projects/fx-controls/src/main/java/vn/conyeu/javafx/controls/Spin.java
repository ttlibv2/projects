package vn.conyeu.javafx.controls;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.util.Duration;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;

public class Spin  {
    static final Duration ONE_SECOND = Duration.seconds(1);
    private final Timeline timeline;
    private final Node node;
    private final Duration duration;

    public static void uninstall(Node node) {
        if(node.getProperties().containsKey(Spin.class)) {
            Object spin = node.getProperties().get(Spin.class);
            if(spin instanceof Spin sp) sp.stop();
        }
    }

    public static Spin install(Node node) {
        return install(node, ONE_SECOND);
    }

    public static Spin install(Node node, Duration duration) {
        Spin spin = new Spin(node, ONE_SECOND);
        spin.play();
        return spin;
    }

    public Spin(Node node) {
        this(node, Duration.seconds(1));
    }

    public Spin(Node node, Duration duration) {
        this.node = Asserts.notNull(node, "@Node");

        if(node.getProperties().containsKey(Spin.class)) {
            String msg = "The node `%s` exist install spin.";
            throw Objects.newIllegal(msg, node.getClass().getName());
        }

        this.duration = duration == null ? ONE_SECOND : duration;
        this.timeline = new Timeline();
        this.installTimeline();
    }

    private void installTimeline() {
        timeline.stop();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setDelay(Duration.ZERO);
        timeline.getKeyFrames().setAll(createFrames());
    }

    private ObservableList<KeyFrame> createFrames() {
        final ObservableList<KeyFrame> keyFrames = FXCollections.observableArrayList();
        keyFrames.add(new KeyFrame(Duration.millis(0), new KeyValue(node.rotateProperty(), 0)));
        keyFrames.add(new KeyFrame(duration, new KeyValue(node.rotateProperty(), 360)));
        return keyFrames;
    }

    public void play() {
        node.getProperties().put(Spin.class, this);
        timeline.playFromStart();
    }

    public void stop() {
        timeline.stop();
        node.getProperties().remove(Spin.class);
    }


}