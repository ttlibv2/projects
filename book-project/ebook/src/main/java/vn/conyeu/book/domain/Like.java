package vn.conyeu.book.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("like")
public class Like extends BookAction {

    public void setValue(Boolean value) {
        super.setValue("true");
    }

}