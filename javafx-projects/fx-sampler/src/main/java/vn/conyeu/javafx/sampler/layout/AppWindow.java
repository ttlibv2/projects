package vn.conyeu.javafx.sampler.layout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import vn.conyeu.javafx.controls.FlexBox;
import vn.conyeu.javafx.controls.flex.FlexDirection;
import vn.conyeu.javafx.controls.flex.FlexWrap;

public class AppWindow extends StackPane {
    public static final int MIN_WIDTH = 450;
    public static final int SIDEBAR_WIDTH = 250;
    public static final String MAIN_MODAL_ID = "modal-pane";
    public final FlexBox vBox = new FlexBox(5,5){
        {
           setFlexWrap(FlexWrap.NOWRAP);
           setDirection(FlexDirection.COLUMN);
            setPadding(new Insets(15));
          // setAlignItem(FlexAlignItem.STRETCH);
         //  setAlignContent(FlexAlignContent.STRETCH);
        }
        @Override
        protected void layoutChildren() {
            super.layoutChildren();
        }
    };


    public AppWindow() {
        setAlignment(Pos.TOP_LEFT);
      //  vBox.setFlexWrap(FlexWrap.WRAP);
      //  vBox.setDefaultFlexGrow(1);

        setStyle("-fx-border-color: blue");
        vBox.setStyle("-fx-border-color: #839826");
        vBox.getStyleClass().setAll("vbox");
      //  vBox. setPadding(new Insets(150));

        getChildren().setAll(vBox);
    }

    public void addNodes(Node... nodes) {
        vBox.getChildren().addAll(nodes);
    }



}