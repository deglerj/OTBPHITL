package org.jd.otbphitl.client;


import org.jd.otbphitl.client.canvas.CanvasMap;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.SimplePanel;

public abstract class Map extends SimplePanel {

	public static Map create(final int tileSize, final int rows, final int columns) {
		if (Canvas.isSupported()) {
			return new CanvasMap(tileSize, rows, columns);
		}
		else {
			// FIXME JD use fallback map
			return null;
		}
	}

	private final int	tileSize;

	private final int	columns;

	private final int	rows;

	public Map(final int tileSize, final int rows, final int columns) {
		this.tileSize = tileSize;
		this.columns = columns;
		this.rows = rows;

		initWidget();

		getElement().getStyle().setBackgroundColor("black");
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

	protected int getRows() {
		return rows;
	}

	protected int getTileSize() {
		return tileSize;
	}

	private void initWidget() {
		setWidth(calculateWidth() + "px");
		setHeight(calculateHeight() + "px");
	}

	public abstract void removeLayer(final Layer layer);

}
