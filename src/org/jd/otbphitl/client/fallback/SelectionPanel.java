package org.jd.otbphitl.client.fallback;

import java.util.ArrayList;
import java.util.List;

import org.jd.otbphitl.client.Arrays;
import org.jd.otbphitl.client.Position;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.ui.HTML;

class SelectionPanel extends HTML {

	private final double			tileSize;

	private double					selectionAlpha	= 0.25;

	private String					selectionColor	= "blue";

	private final List<DivElement>	divs			= new ArrayList<DivElement>();

	SelectionPanel(final int tileSize) {
		this.tileSize = tileSize;
	}

	private void createDiv() {
		final DivElement div = Document.get().createDivElement();
		final Style style = div.getStyle();
		style.setPosition(com.google.gwt.dom.client.Style.Position.ABSOLUTE);
		style.setZIndex(9002);

		divs.add(div);
		getElement().appendChild(div);
	}

	private Position findBottomRight(final int topLeftX, final int topLeftY, final boolean[][] selected) {
		int maxX = selected.length - 1;
		int maxY = selected[0].length - 1;

		for (int y = topLeftY; y <= maxY; y++) {
			if (!selected[topLeftX][y]) {
				maxY = y - 1;
				break;
			}

			for (int x = topLeftX; x <= maxX; x++) {
				if (!selected[x][y]) {
					maxX = x - 1;
					break;
				}
			}
		}

		return new Position(maxX, maxY);
	}

	private void hideRemainingDivs(final int divsInUse) {
		for (int i = divsInUse; i < divs.size(); i++) {
			divs.get(i).getStyle().setVisibility(Visibility.HIDDEN);
		}
	}

	private void markAsUnselected(final Position topLeft, final Position bottomRight, final boolean[][] selected) {
		for (int x = topLeft.getX(); x <= bottomRight.getX(); x++) {
			for (int y = topLeft.getY(); y <= bottomRight.getY(); y++) {
				selected[x][y] = false;
			}
		}
	}

	private void placeSelectionDiv(final Position topLeft, final Position bottomRight, final int divsInUse) {
		// Ensure the divs collection contains an unused div
		if (divsInUse == divs.size()) {
			createDiv();
		}

		final DivElement div = divs.get(divsInUse);
		final Style style = div.getStyle();
		style.setBackgroundColor(selectionColor);
		style.setOpacity(selectionAlpha);
		style.setTop(topLeft.getY() * tileSize, Unit.PX);
		style.setLeft(topLeft.getX() * tileSize, Unit.PX);
		style.setWidth((bottomRight.getX() - topLeft.getX() + 1) * tileSize, Unit.PX);
		style.setHeight((bottomRight.getY() - topLeft.getY() + 1) * tileSize, Unit.PX);
		style.clearVisibility();
	}

	void setSelectionAlpha(final double selectionAlpha) {
		this.selectionAlpha = selectionAlpha;
	}

	void setSelectionColor(final String selectionColor) {
		this.selectionColor = selectionColor;
	}

	void setSelectionLayer(final boolean[][] orgSelected) {
		int divsInUse = 0;

		// Create a copy, since we'll be modifying the array's values
		final boolean[][] selected = Arrays.copyOf(orgSelected);

		for (int x = 0; x < selected.length; x++) {
			for (int y = 0; y < selected[0].length; y++) {
				// Is this tile selected?
				if (selected[x][y]) {
					final Position topLeft = new Position(x, y);

					// Find the maximum connected selection rectangle where x,y
					// is the top left corner
					final Position bottomRight = findBottomRight(x, y, selected);

					placeSelectionDiv(topLeft, bottomRight, divsInUse);
					markAsUnselected(topLeft, bottomRight, selected);
					divsInUse++;
				}
			}
		}

		hideRemainingDivs(divsInUse);
	}

}
