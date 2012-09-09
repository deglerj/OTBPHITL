package org.jd.otbphitl.client.canvas;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

class SelectionCanvas {

	private final Canvas		canvas			= Canvas.createIfSupported();

	private final int			rows;

	private final int			columns;

	private final double		tileSize;

	private double				selectionAlpha	= 0.25;

	private CssColor			selectionColor	= CssColor.make("blue");

	private final boolean[][]	painted;

	SelectionCanvas(final int rows, final int columns, final int tileSize) {
		this.rows = rows;
		this.columns = columns;
		this.tileSize = tileSize;

		painted = new boolean[rows][columns];
	}

	Canvas getCanvas() {
		return canvas;
	}

	private void paintLayer(final boolean[][] selectedTiles) {
		final Context2d ctx = canvas.getContext2d();

		ctx.setFillStyle(selectionColor);
		ctx.setGlobalAlpha(selectionAlpha);

		for (int row = 0; row < rows; row++) {
			final double x = row * tileSize;
			for (int column = 0; column < columns; column++) {
				// Is this tile currently painted but should not be painted?
				// -> Clear
				if (painted[row][column] && !selectedTiles[row][column]) {
					ctx.clearRect(x, column * tileSize, tileSize, tileSize);
				}
				// Is it currently not painted but should be painted? ->
				// Paint
				else if (!painted[row][column] && selectedTiles[row][column]) {
					ctx.fillRect(x, column * tileSize, tileSize, tileSize);
				}

				painted[row][column] = selectedTiles[row][column];
			}
		}
	}

	void setSelectionAlpha(final double selectionAlpha) {
		this.selectionAlpha = selectionAlpha;
	}

	void setSelectionColor(final String selectionColor) {
		this.selectionColor = CssColor.make(selectionColor);
	}

	void setSelectionLayer(final boolean[][] selectedTiles) {
		paintLayer(selectedTiles);
	}

}
