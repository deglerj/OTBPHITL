package org.jd.otbphitl.client;

public class FixedLayer implements Layer {

	private final Tile[][]	tiles;

	public FixedLayer(final Tile[][] tiles) {
		this.tiles = tiles;
	}

	@Override
	public Tile[][] getTiles() {
		return tiles;
	}

}
