package org.jd.otbphitl.client;

import org.jd.otbphitl.client.canvas.CanvasMap;
import org.jd.otbphitl.client.resources.TestResources;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class OTBPHITL implements EntryPoint {

	private static final int	COLUMNS	= 250;
	private static final int	ROWS	= 250;

	private void addBaseLayer(final Map map) {
		final Tile[][] tiles = new Tile[ROWS][COLUMNS];
		for (int row = 0; row < ROWS; row++) {
			for (int column = 0; column < COLUMNS; column++) {
				tiles[row][column] = new ImageResourceTile(Random.nextBoolean() ? TestResources.INSTANCE.red()
						: TestResources.INSTANCE.blue());
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

		Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
			boolean	added;

			@Override
			public boolean execute() {
				if (added) {
					map.removeLayer(layer);
				}
				else {
					map.addLayer(layer);
				}

				added = !added;

				return true;
			}
		}, 5000);

	}

	@Override
	public void onModuleLoad() {
		final Map map = CanvasMap.create(32, ROWS, COLUMNS);
		RootPanel.get().add(map);

		addBaseLayer(map);

		addOverlayLayer(map);
	}
}
