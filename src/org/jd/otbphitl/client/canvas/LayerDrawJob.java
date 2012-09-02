package org.jd.otbphitl.client.canvas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jd.otbphitl.client.Layer;
import org.jd.otbphitl.client.Tile;
import org.jd.otbphitl.client.canvas.CanvasMap.Position;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;

class LayerDrawJob {

	private final Layer								layer;

	private final Context2d							ctx;

	private final TileImageCache					tileImageCache;

	private LayerDrawJob							nextJob;

	private final double							tileSize;

	private final Collection<HandlerRegistration>	handlerRegistrations	= new ArrayList<HandlerRegistration>();

	private int										pendingImageLoads		= 0;

	LayerDrawJob(final Layer layer, final Context2d ctx, final TileImageCache tileImageCache, final double tileSize) {
		this.layer = layer;
		this.ctx = ctx;
		this.tileImageCache = tileImageCache;
		this.tileSize = tileSize;
	}

	private void addTilePosition(final Map<Tile, Collection<Position>> tilesToPositions, final double y, final double x, final Tile tile) {
		Collection<Position> positions = tilesToPositions.get(tile);
		if (positions == null) {
			positions = new ArrayList<Position>();
			tilesToPositions.put(tile, positions);
		}
		positions.add(new Position(x, y));
	}

	private void clearHandlerRegistrations() {
		for (final HandlerRegistration registration : handlerRegistrations) {
			registration.removeHandler();
		}
	}

	private void drawTileAtPositions(final Context2d ctx, final Tile tile, final Collection<Position> positions) {
		final Image img = tileImageCache.getImageForTile(tile);
		final HandlerRegistration registration = img.addHandler(new LoadHandler() {
			@Override
			public void onLoad(final LoadEvent event) {
				for (final Position position : positions) {
					final ImageElement element = img.getElement().cast();
					ctx.drawImage(element, position.getX(), position.getY());
				}

				imageLoaded();
			}
		}, LoadEvent.getType());
		handlerRegistrations.add(registration);
		tile.populateImg(img);
	}

	void execute() {
		final Map<Tile, Collection<Position>> tilesToPositions = new HashMap<Tile, Collection<Position>>();

		final Tile[][] tiles = layer.getTiles();

		final int rows = tiles.length;
		final int columns = tiles[0].length;

		double y = 0;
		for (int row = 0; row < rows; row++) {
			double x = 0;
			for (int column = 0; column < columns; column++) {
				final Tile tile = tiles[row][column];

				if (tile != null) {
					addTilePosition(tilesToPositions, y, x, tile);
				}

				x += tileSize;
			}
			y += tileSize;
		}

		pendingImageLoads = tilesToPositions.size();

		for (final Entry<Tile, Collection<Position>> entry : tilesToPositions.entrySet()) {
			final Tile tile = entry.getKey();
			final Collection<Position> positions = entry.getValue();
			drawTileAtPositions(ctx, tile, positions);
		}
	}

	private void executeNext() {
		clearHandlerRegistrations();

		if (nextJob != null) {
			nextJob.execute();
		}
	}

	private void imageLoaded() {
		pendingImageLoads--;

		if (pendingImageLoads == 0) {
			executeNext();
		}
	}

	void setNextJob(final LayerDrawJob nextJob) {
		this.nextJob = nextJob;
	}

}
