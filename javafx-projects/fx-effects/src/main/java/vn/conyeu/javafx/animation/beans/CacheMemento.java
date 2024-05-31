package vn.conyeu.javafx.animation.beans;


import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.util.concurrent.atomic.AtomicBoolean;

public class CacheMemento {
    private boolean cache;
    private boolean cacheShape;
    private boolean snapToPixel;
    private final Node node;
    private CacheHint cacheHint = CacheHint.DEFAULT;
    private final AtomicBoolean isCached = new AtomicBoolean(false);

    public CacheMemento(Node node) {
        this.node = node;
    }

    /**
     * this method will cache the node only if it wasn't cached before
     */
    public void cache() {
        if (!isCached.getAndSet(true)) {
            this.cache = node.isCache();
            this.cacheHint = node.getCacheHint();
            node.setCache(true);
            node.setCacheHint(CacheHint.SPEED);
            if (node instanceof Region region) {
                this.cacheShape = region.isCacheShape();
                this.snapToPixel = region.isSnapToPixel();
                region.setCacheShape(true);
                region.setSnapToPixel(true);
            }
        }
    }

    public void restore() {
        if (isCached.getAndSet(false)) {
            node.setCache(cache);
            node.setCacheHint(cacheHint);
            if (node instanceof Region region) {
                region.setCacheShape(cacheShape);
                region.setSnapToPixel(snapToPixel);
            }
        }
    }
}