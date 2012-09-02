package org.jd.otbphitl.client;

import org.jd.otbphitl.client.canvas.CanvasMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class OTBPHITL implements EntryPoint {

	private static final int	COLUMNS	= 100;
	private static final int	ROWS	= 100;

	private void addBaseLayer(final Map map) {
		final Tile[][] tiles = new Tile[ROWS][COLUMNS];
		for (int row = 0; row < ROWS; row++) {
			for (int column = 0; column < COLUMNS; column++) {
				tiles[row][column] = new ImageURLTile("/" + (Random.nextBoolean() ? "red.png" : "blue.png"));
			}
		}
		final Layer layer = new FixedLayer(tiles);
		map.addLayer(layer);
	}

	private void addOverlayLayer(final Map map) {
		final Tile[][] tiles = new Tile[ROWS][COLUMNS];
		for (int row = 0; row < ROWS; row++) {
			for (int column = 0; column < COLUMNS; column++) {
				if (Random.nextInt(4) == 3) {
					tiles[row][column] = new ImageURLTile("/" + (Random.nextBoolean() ? "xoverlay.png" : "blackoverlay.png"));
				}
			}
		}
		final Layer layer = new FixedLayer(tiles);
		map.addLayer(layer);
	}

	@Override
	public void onModuleLoad() {
		final Map map = CanvasMap.create(32, ROWS, COLUMNS);
		RootPanel.get().add(map);

		addBaseLayer(map);

		addOverlayLayer(map);

	}
}
