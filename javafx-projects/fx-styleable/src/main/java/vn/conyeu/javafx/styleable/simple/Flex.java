package vn.conyeu.javafx.styleable.simple;

import java.io.Serializable;

public class Flex implements Serializable {
    static final Integer DEFAULT_GROW = null;
    static final Integer DEFAULT_SHRINK = null;
    static final String DEFAULT_BASIC = null;

    private final Integer grow;
    private final Integer shrink;
    private final String basic;

    public static Flex empty() {
        return new Flex();
    }

    public Flex() {
        this(DEFAULT_GROW, DEFAULT_SHRINK, DEFAULT_BASIC);
    }

    public Flex(int grow) {
        this(grow, DEFAULT_SHRINK, DEFAULT_BASIC);
    }

    public Flex(int grow, int shrink) {
        this(grow, shrink,DEFAULT_BASIC);
    }

    public Flex(String basic) {
        this(DEFAULT_GROW, DEFAULT_SHRINK, basic);
    }

    public Flex(Integer grow, Integer shrink, String basic) {
        this.grow = grow;
        this.shrink = shrink;
        this.basic = basic;
    }

    public Integer getGrow() {
        return grow;
    }

    public Integer getShrink() {
        return shrink;
    }

    public String getBasic() {
        return basic;
    }


}