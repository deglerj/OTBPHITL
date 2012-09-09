package org.jd.otbphitl.client;

import org.jd.otbphitl.client.canvas.CanvasMap;
import org.jd.otbphitl.client.fallback.FallbackMap;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.user.client.ui.SimplePanel;

public abstract class Map extends SimplePanel {

	public static Map create(final int tileSize, final int rows, final int columns) {
		if (Canvas.isSupported()) {
			return new CanvasMap(tileSize, rows, columns);
		}
		else {
			return new FallbackMap(tileSize, rows, columns);
		}
	}

	private final int			tileSize;

	private final int			columns;

	private final int			rows;

	private MapSelectionHandler	selectionHandler;

	public Map(final int tileSize, final int rows, final int columns) {
		this.tileSize = tileSize;
		this.columns = columns;
		this.rows = rows;

		initWidget();
	}

	public abstract void addLayer(final Layer layer);

	protected int calculateHeight() {
		return tileSize * rows;
	}

	protected int calculateWidth() {
		return tileSize * columns;
	}

	protected int getColumns() {
		return columns;
	}

	protected abstract HasAllMouseHandlers getMouseEventCaptureWidget();

	protected int getRows() {
		return rows;
	}

	public boolean[][] getSelection() {
		return selectionHandler.getSelected();
	}

	protected int getTileSize() {
		return tileSize;
	}

	private void initWidget() {
		setWidth(calculateWidth() + "px");
		setHeight(calculateHeight() + "px");
	}

	public abstract void removeLayer(final Layer layer);

	public abstract void setSelectionAlpha(double selectionAlpha);

	public abstract void setSelectionColor(String selectionColor);

	public void setSelectionHandlingEnabled(final boolean enabled) {
		if (selectionHandler == null) {
			selectionHandler = new MapSelectionHandler(getMouseEventCaptureWidget(), this);
		}

		selectionHandler.setSelectionHandlingEnabled(enabled);
	}

	protected abstract void setSelectionLayer(boolean[][] selectedTiles);

}
