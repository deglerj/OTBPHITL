package org.jd.otbphitl.client.fallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jd.otbphitl.client.Layer;
import org.jd.otbphitl.client.Tile;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;

public class FallbackMap extends org.jd.otbphitl.client.Map {

	private final FlowPanel						container		= new FlowPanel();

	private final Map<Layer, ImageElement[][]>	layerToImgs	= new LinkedHashMap<Layer, ImageElement[][]>();

	private final Collection<ImageElement[][]>	imgPool		= new ArrayList<ImageElement[][]>();

	public FallbackMap(final int tileSize, final int rows, final int columns) {
		super(tileSize, rows, columns);

		initWidget();
	}

	@Override
	public void addLayer(final Layer layer) {
		final int rows = getRows();
		final int columns = getColumns();

		final ImageElement[][] imgs = getOrCreateImgs();
		final Tile[][] tiles = layer.getTiles();

		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				final Tile tile = tiles[row][column];
				if (tile != null) {
					tile.populateImg(imgs[row][column]);
				}
			}
		}

		layerToImgs.put(layer, imgs);

		updateZIndex();
	}

	private ImageElement[][] createNewImgs() {
		final int rows = getRows();
		final int columns = getColumns();
		final double tileSize = getTileSize();

		final ImageElement[][] imgs = new ImageElement[rows][columns];

		final Document doc = Document.get();
		final DocumentFragment fragment = DocumentFragment.create();

		double y = 0;
		for (int row = 0; row < rows; row++) {
			double x = 0;
			for (int column = 0; column < columns; column++) {
				final ImageElement img = doc.createImageElement();
				fragment.appendChild(img);

				final Style style = img.getStyle();
				style.setPosition(Position.ABSOLUTE);
				style.setTop(y, Unit.PX);
				style.setLeft(x, Unit.PX);

				imgs[row][column] = img;

				x += tileSize;
			}
			y += tileSize;
		}

		container.getElement().appendChild(fragment);

		return imgs;
	}

	private ImageElement[][] getImgsFromPool() {
		final Iterator<ImageElement[][]> i = imgPool.iterator();
		final ImageElement[][] imgs = i.next();
		i.remove();
		return imgs;
	}

	private ImageElement[][] getOrCreateImgs() {
		if (imgPool.isEmpty()) {
			return createNewImgs();
		}
		else {
			return getImgsFromPool();
		}
	}

	private void initWidget() {
		setWidget(container);

		final int width = calculateWidth();
		final int height = calculateHeight();

		container.setHeight(height + "px");
		container.setWidth(width + "px");
		container.getElement().getStyle().setPosition(Position.RELATIVE);
	}

	private void pool(final ImageElement[][] imgs) {
		for (final ImageElement[] row : imgs) {
			for (final ImageElement img : row) {
				prepareImgForPool(img);
			}
		}

		imgPool.add(imgs);
	}

	private void prepareImgForPool(final ImageElement img) {
		img.setSrc("/empty.png");
	}

	@Override
	public void removeLayer(final Layer layer) {
		final ImageElement[][] imgs = layerToImgs.get(layer);
		pool(imgs);

		layerToImgs.remove(layer);
	}

	private void updateZIndex() {
		int z = 0;

		for (final ImageElement[][] imgs : layerToImgs.values()) {
			for (final ImageElement[] row : imgs) {
				for (final ImageElement img : row) {
					img.getStyle().setZIndex(z);
				}
			}

			z++;
		}

	}

}
