package vn.conyeu.javafx.sampler.layout;

import javafx.scene.Node;

import java.util.List;

public class SideNav {
    private String title;
    private Node graphic;
    private Class<Page> pageCls;
    private List<String> keywords;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Node getGraphic() {
        return graphic;
    }

    public void setGraphic(Node graphic) {
        this.graphic = graphic;
    }

    public Class<Page> getPageCls() {
        return pageCls;
    }

    public void setPageCls(Class<Page> pageCls) {
        this.pageCls = pageCls;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}