package vn.conyeu.javafx.utils;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import vn.conyeu.javafx.beans.PositionBean;

public class Layouts {

	private Layouts() {
	}

	public static PositionBean computePosition(Region parent, Node child, double areaX, double areaY, double areaWidth, double areaHeight,
											   double areaBaselineOffset, Insets margin, HPos hAlignment, VPos vAlignment) {
		return computePosition(parent, child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, margin, hAlignment, vAlignment, true, true);
	}

	public static PositionBean computePosition(Region parent, Node child, double areaX, double areaY, double areaWidth, double areaHeight,
	                                       double areaBaselineOffset, Insets margin, HPos hAlignment, VPos vAlignment, boolean snapToPixel, boolean computeSizes) {

		Insets snappedMargin = margin == null ? Insets.EMPTY : margin;
		if (snapToPixel) {
			snappedMargin = new Insets(
					parent.snapSpaceY(snappedMargin.getTop()),
					parent.snapSpaceX(snappedMargin.getRight()),
					parent.snapSpaceY(snappedMargin.getBottom()),
					parent.snapSpaceX(snappedMargin.getLeft())
			);
		}

		double xPosition = computeXPosition(parent, child, areaX, areaWidth, snappedMargin, false, hAlignment, snapToPixel, computeSizes);
		double yPosition = computeYPosition(parent, child, areaY, areaHeight, areaBaselineOffset, snappedMargin, false, vAlignment, snapToPixel, computeSizes);
		return PositionBean.of(xPosition, yPosition);
	}

	public static double computeXPosition(Region parent, Node child, double areaX, double areaWidth, Insets margin, boolean snapMargin, HPos hAlignment, boolean snapToPixel, boolean computeSizes) {
		Insets snappedMargin = margin == null ? Insets.EMPTY : margin;
		if (snapMargin) {
			snappedMargin = new Insets(
					parent.snapSpaceY(snappedMargin.getTop()),
					parent.snapSpaceX(snappedMargin.getRight()),
					parent.snapSpaceY(snappedMargin.getBottom()),
					parent.snapSpaceX(snappedMargin.getLeft())
			);
		}

		final double leftMargin = snappedMargin.getLeft();
		final double rightMargin = snappedMargin.getRight();
		final double xOffset = leftMargin + computeXOffset(areaWidth - leftMargin - rightMargin, computeSizes ? boundWidth(child) : child.getLayoutBounds().getWidth(), hAlignment);
		final double xPosition = areaX + xOffset;
		return snapToPixel ? parent.snapPositionX(xPosition) : xPosition;
	}

	public static double computeYPosition(Region parent, Node child, double areaY, double areaHeight, double areaBaselineOffset, Insets margin, boolean snapMargin, VPos vAlignment, boolean snapToPixel, boolean computeSizes) {
		Insets snappedMargin = margin == null ? Insets.EMPTY : margin;
		if (snapMargin) {
			snappedMargin = new Insets(
					parent.snapSpaceY(snappedMargin.getTop()),
					parent.snapSpaceX(snappedMargin.getRight()),
					parent.snapSpaceY(snappedMargin.getBottom()),
					parent.snapSpaceX(snappedMargin.getLeft())
			);
		}

		final double topMargin = snappedMargin.getTop();
		final double bottomMargin = snappedMargin.getBottom();
		final double yOffset;
		if (vAlignment == VPos.BASELINE) {
			double bo = child.getBaselineOffset();
			if (bo == Node.BASELINE_OFFSET_SAME_AS_HEIGHT) {
				yOffset = areaBaselineOffset - (computeSizes ? boundHeight(child) : child.getLayoutBounds().getHeight());
			} else {
				yOffset = areaBaselineOffset - bo;
			}
		} else {
			yOffset = topMargin + computeYOffset(areaHeight - topMargin - bottomMargin, computeSizes ? boundHeight(child) : child.getLayoutBounds().getHeight(), vAlignment);
		}
		final double yPosition = areaY + yOffset;
		return snapToPixel ? parent.snapPositionY(yPosition) : yPosition;
	}

	private static double computeXOffset(double areaWidth, double contentWidth, HPos hAlignment) {
		return switch (hAlignment) {
			case LEFT -> 0;
			case CENTER -> (areaWidth - contentWidth) / 2;
			case RIGHT -> areaWidth - contentWidth;
		};
	}

	private static double computeYOffset(double areaHeight, double contentHeight, VPos vAlignment) {
		return switch (vAlignment) {
			case BASELINE, TOP -> 0;
			case CENTER -> (areaHeight - contentHeight) / 2;
			case BOTTOM -> areaHeight - contentHeight;
		};
	}

	/**
	 * If the node is resizable, will set its layout bounds to the specified
	 * width and height.   If the node is not resizable, this method is a no-op.
	 * @param width the target layout bounds width
	 * @param height the target layout bounds height
	 */
	public static void resize(Region parent, Node node, double width, double height) {
		node.resize(parent.snapSizeX(width), parent.snapSizeY(height));
	}

	public static void relocate(Region parent, Node node, double x, double y) {
		node.relocate(parent.snapPositionX(x), parent.snapPositionY(y));
	}

	public static void resizeRelocate(Region parent, Node node, double x, double y, double w, double h) {
		node.resizeRelocate(parent.snapPositionX(x), parent.snapPositionY(y), parent.snapSizeX(w), parent.snapSizeY(h));
	}

	public static double boundedSize(double min, double pref, double max) {
		return Math.min(Math.max(pref, min), Math.max(min, max));
	}

	public static double boundWidth(Node node) {
		return boundedSize(node.minWidth(-1), node.prefWidth(-1), node.maxWidth(-1));
	}

	public static double boundHeight(Node node) {
		return boundedSize(node.minHeight(-1), node.prefHeight(-1), node.maxHeight(-1));
	}

	public static double snappedBoundWidth(Node node) {
		return node.getParent() instanceof Region r ? r.snapSizeX(boundWidth(node)) : boundWidth(node);
	}

	public static double snappedBoundHeight(Node node) {
		return node.getParent() instanceof Region r ? r.snapSizeY(boundHeight(node)) : boundHeight(node);
	}


}