package vn.conyeu.javafx.controls.skin;

import javafx.beans.binding.StringBinding;
import javafx.scene.Node;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.javafx.bindings.BindingBuilder;
import vn.conyeu.javafx.controls.MaskField;

import java.util.List;
import java.util.Set;

@Slf4j
public class MaskFieldSkin extends TextFieldSkin {
    StringBinding textBinding;

    public MaskFieldSkin(MaskField control) {
        super(control);
        initializeUI(control);
    }

    private void initializeUI(MaskField c) {
        Pane textGroup = (Pane) getChildren().get(0);
        List<Node> groupChildren = textGroup.getChildren();

        Set<Node> nodes = textGroup.lookupAll("Text");


//        Text promptNode = (Text) textGroup.getChildren().get(0);
//        promptNode.textProperty().unbind();
//        promptNode.textProperty().bind(c.inputMaskProperty());
//
//        Text textNode = (Text) textGroup.getChildren().get(2);
      //  textNode.textProperty().unbind();

        //registerChangeListener(c.textProperty(), e -> {
       //     String text = c.textProperty().getValueSafe();
      //      textNode.setText(maskText(text));
      //  });

        //registerChangeListener(c.focusedProperty(), e ->{
           // if(c.isFocused() && c.getText().isBlank()) {
            //    String initialText = c.getInitialText() != null ? c.getInitialText() : c.getInitialTextFromMask();
           //     textNode.setText(maskText(initialText));
          //  }
        //    log.warn(c.getText());
      //  });

        //registerInvalidationListener(c.getMaskVerifiers(), e -> c.clear());

//        textNode.textProperty().bind(new StringBinding() {
//            { bind(control.textProperty()); }
//            @Override protected String computeValue() {
//                return maskText(control.textProperty().getValueSafe());
//            }
//        });


    }

}