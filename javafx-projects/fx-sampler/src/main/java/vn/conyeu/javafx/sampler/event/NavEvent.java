package vn.conyeu.javafx.sampler.event;

import vn.conyeu.javafx.sampler.layout.Page;

public final class NavEvent extends Event {

    private final Class<? extends Page> page;

    public NavEvent(Class<? extends Page> page) {
        this.page = page;
    }

    public Class<? extends Page> getPage() {
        return page;
    }

    @Override
    public String toString() {
        return "NavEvent{"
                + "page=" + page
                + "} " + super.toString();
    }
}