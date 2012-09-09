package org.jd.otbphitl.client.canvas;

import java.util.ArrayList;
import java.util.List;

import org.jd.otbphitl.client.Layer;
import org.jd.otbphitl.client.Map;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class CanvasMap extends Map {

	public static class Position {
		private final double	x;
		private final double	y;

		public Position(final double x, final double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

	}

	private final TileImageCache		tileImageCache	= new TileImageCache();

	private final FlowPanel				container		= new FlowPanel();

	private int							currentCanvas	= 0;

	private final Canvas[]				canvases		= { Canvas.createIfSupported(), Canvas.createIfSupported() };

	private final List<Layer>			layers			= new ArrayList<Layer>();

	private Image						mouseEventWidget;

	private SelectionCanvas	selectionDelegate;

	public CanvasMap(final int tileSize, final int rows, final int columns) {
		super(tileSize, rows, columns);

		initWidget();
	}

	@Override
	public void addLayer(final Layer layer) {
		layers.add(layer);
		redraw();
	}

	private Canvas getHiddenCanvas() {
		if (currentCanvas == 0) {
			return canvases[1];
		}
		else {
			return canvases[0];
		}
	}

	@Override
	protected HasAllMouseHandlers getMouseEventCaptureWidget() {
		if (mouseEventWidget == null) {
			mouseEventWidget = new Image();
			mouseEventWidget.getElement().getStyle().setPosition(com.google.gwt.dom.client.Style.Position.ABSOLUTE);
			mouseEventWidget.getElement().getStyle().setZIndex(9001);
			final int width = calculateWidth();
			final int height = calculateHeight();

			mouseEventWidget.setHeight(height + "px");
			mouseEventWidget.setWidth(width + "px");

			container.add(mouseEventWidget);
		}

		return mouseEventWidget;
	}

	private void initCanvas(final Canvas canvas) {
		canvas.getElement().getStyle().setPosition(com.google.gwt.dom.client.Style.Position.ABSOLUTE);
		final int width = calculateWidth();
		final int height = calculateHeight();

		canvas.setHeight(height + "px");
		canvas.setWidth(width + "px");

		canvas.setCoordinateSpaceHeight(height);
		canvas.setCoordinateSpaceWidth(width);
	}

	private void initSelectionLayer() {
		selectionDelegate = new SelectionCanvas(getRows(), getColumns(), getTileSize());

		final Canvas canvas = selectionDelegate.getCanvas();
		initCanvas(canvas);
		canvas.getElement().getStyle().setZIndex(9000);
		canvas.getElement().setId("selectionCanvas");
		container.add(canvas);
	}

	private void initWidget() {
		setWidget(container);
		container.getElement().getStyle().setPosition(com.google.gwt.dom.client.Style.Position.RELATIVE);

		for (final Canvas canvas : canvases) {
			initCanvas(canvas);

			container.add(canvas);
		}

		canvases[1].setVisible(false);

		canvases[0].getElement().setId("mapCanvas1");
		canvases[0].getElement().setId("mapCanvas2");
	}

	private void redraw() {
		final Context2d ctx = getHiddenCanvas().getContext2d();

		ctx.clearRect(0, 0, calculateWidth(), calculateHeight());

		final List<LayerDrawJob> drawJobs = new ArrayList<LayerDrawJob>();
		for (final Layer layer : layers) {
			drawJobs.add(new LayerDrawJob(layer, ctx, tileImageCache, getTileSize()));
		}

		for (int i = 0; i < drawJobs.size() - 1; i++) {
			drawJobs.get(i).setNextJob(drawJobs.get(i + 1));
		}

		drawJobs.get(drawJobs.size() - 1).setNextJob(new Job() {
			@Override
			public void execute() {
				swapCanvas();
			}
		});

		drawJobs.get(0).execute();
	}

	@Override
	public void removeLayer(final Layer layer) {
		layers.remove(layer);
		redraw();
	}

	@Override
	public void setSelectionAlpha(final double selectionAlpha) {
		selectionDelegate.setSelectionAlpha(selectionAlpha);
	}

	@Override
	public void setSelectionColor(final String selectionColor) {
		selectionDelegate.setSelectionColor(selectionColor);
	}

	@Override
	protected void setSelectionLayer(final boolean[][] selectedTiles) {
		if (selectionDelegate == null) {
			initSelectionLayer();
		}

		selectionDelegate.setSelectionLayer(selectedTiles);
	}

	private void swapCanvas() {
		if (currentCanvas == 0) {
			canvases[0].setVisible(false);
			canvases[1].setVisible(true);
			currentCanvas = 1;
		}
		else {
			canvases[0].setVisible(true);
			canvases[1].setVisible(false);
			currentCanvas = 0;
		}
	}

}
