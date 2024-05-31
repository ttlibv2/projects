package vn.conyeu.javafx.controls.skin;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import vn.conyeu.javafx.controls.SearchBar;
import vn.conyeu.javafx.icons.core.SimpleIcon;

public class SearchBarSkin extends InputGroupSkin<SearchBar, TextField> {
    private Node searchIcon, clearIcon;

    public SearchBarSkin(SearchBar control) {
        super(control);
    }

    @Override
    public void initializeDataUI(SearchBar c) {
        super.initializeDataUI(c);
        resolveSearchIcon(c);
        resolveClearIcon(c);
    }

    @Override
    public void initializeListener(SearchBar c) {
        super.initializeListener(c);
        registerChangeListener(c.searchIconProperty(), e -> resolveSearchIcon(c));
        registerChangeListener(c.clearIconProperty(), e -> resolveClearIcon(c));
    }

    protected void resolveSearchIcon(SearchBar c) {
        if(searchIcon == null) {
            searchIcon = resolveIcon(c.getSearchIcon());
            searchIcon.getStyleClass().add("search");
            c.getLeadingNodes().add(searchIcon);
        }
        else if(searchIcon instanceof SimpleIcon ic) {
            ic.setIconLiteral(c.getSearchIcon());
        }
    }

    protected void resolveClearIcon(SearchBar c) {
        if(clearIcon == null) {
            clearIcon = resolveIcon(c.getClearIcon());
            clearIcon.getStyleClass().add("clear");
            c.getTrailingNodes().add(clearIcon);
        }
        else if(clearIcon instanceof SimpleIcon ic) {
            ic.setIconLiteral(c.getClearIcon());
        }
    }
}