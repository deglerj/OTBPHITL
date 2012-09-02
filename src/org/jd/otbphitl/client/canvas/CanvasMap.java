package org.jd.otbphitl.client.canvas;

import java.util.ArrayList;
import java.util.List;

import org.jd.otbphitl.client.Map;
import org.jd.otbphitl.client.Layer;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;

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

	private final TileImageCache	tileImageCache	= new TileImageCache();

	private final Canvas			canvas			= Canvas.createIfSupported();

	private final List<Layer>		layers			= new ArrayList<Layer>();

	public CanvasMap(final int tileSize, final int rows, final int columns) {
		super(tileSize, rows, columns);

		initWidget();
	}

	@Override
	public void addLayer(final Layer layer) {
		layers.add(layer);
		redraw();
	}

	private void initWidget() {
		setWidget(canvas);

		final int width = calculateWidth();
		final int height = calculateHeight();

		canvas.setHeight(height + "px");
		canvas.setWidth(width + "px");

		canvas.setCoordinateSpaceHeight(height);
		canvas.setCoordinateSpaceWidth(width);
	}

	private void redraw() {
		final Context2d ctx = canvas.getContext2d();

		ctx.clearRect(0, 0, calculateWidth(), calculateHeight());

		final List<LayerDrawJob> drawJobs = new ArrayList<LayerDrawJob>();
		for (final Layer layer : layers) {
			drawJobs.add(new LayerDrawJob(layer, ctx, tileImageCache, getTileSize()));
		}

		for (int i = 0; i < drawJobs.size() - 1; i++) {
			drawJobs.get(i).setNextJob(drawJobs.get(i + 1));
		}

		drawJobs.get(0).execute();

	}

	@Override
	public void removeLayer(final Layer layer) {
		layers.remove(layer);
		redraw();
	}

}
