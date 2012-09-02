package org.jd.otbphitl.client.canvas;

import java.util.HashMap;
import java.util.Map;

import org.jd.otbphitl.client.Tile;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

class TileImageCache {

	private final Map<Tile, Image>	tileToImg	= new HashMap<Tile, Image>();

	Image getImageForTile(final Tile tile) {
		Image img = tileToImg.get(tile);

		if (img == null) {
			img = new Image();
			img.setVisible(false);
			img.getElement().getStyle().setPosition(com.google.gwt.dom.client.Style.Position.FIXED);
			RootPanel.get().add(img);
			tileToImg.put(tile, img);
		}

		return img;
	}
}
