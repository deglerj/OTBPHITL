package org.jd.otbphitl.client;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

class MapSelectionHandler {

	private boolean						selectionHandlingEnabled;

	private final HasAllMouseHandlers	hasAllMouseHandlers;

	private final Map					map;

	private boolean						mouseDown;

	private int							lastX	= -1;

	private int							lastY	= -1;

	private boolean						lastMouseDown;

	private boolean[][]					selected;

	private int							downOnX;

	private int							downOnY;

	private boolean						downOnSelected;

	private boolean						pendingSelection;

	public MapSelectionHandler(final HasAllMouseHandlers hasAllMouseHandlers, final Map map) {
		this.hasAllMouseHandlers = hasAllMouseHandlers;
		this.map = map;
		selected = new boolean[map.getRows()][map.getColumns()];

		initSelectionHandling();
	}

	private void applyPendingSelection(final int tileX, final int tileY, final boolean[][] highlighted) {
		// Find min and max coordinates
		int minX, maxX;
		if (tileX > downOnX) {
			minX = downOnX;
			maxX = tileX;
		}
		else {
			minX = tileX;
			maxX = downOnX;
		}
		int minY, maxY;
		if (tileY > downOnY) {
			minY = downOnY;
			maxY = tileY;
		}
		else {
			minY = tileY;
			maxY = downOnY;
		}

		// Loop from min to max coordinated and change the selection state
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				highlighted[x][y] = !downOnSelected;
			}
		}
	}

	private void clearSelection() {
		selected = new boolean[selected.length][selected[0].length];
	}

	private void completePendingSelection(final int tileX, final int tileY) {
		applyPendingSelection(tileX, tileY, selected);

		map.setSelectionLayer(selected);

		pendingSelection = false;
	}

	public boolean[][] getSelected() {
		return Arrays.copyOf(selected);
	}

	private void handleMouseEvent(final int x, final int y, final boolean ctrlDown) {
		if (!selectionHandlingEnabled) {
			return;
		}

		final int tileX = relativeCoordinateToTile(x);
		final int tileY = relativeCoordinateToTile(y);

		// Has the tile underneath the cursor changed?
		if (lastX != tileX || lastY != tileY || lastMouseDown != mouseDown) {
			if (mouseDown) {
				// Pending selection (mouse was already down)? -> Highlight the
				// pending selection based on the current position
				if (pendingSelection) {
					highlightPendingSelection(tileX, tileY);
				}
				// No pending selection (mouse was previously up)? -> Start a
				// new pending selection, then highlight it
				else {
					startPendingSelection(tileX, tileY, ctrlDown);
					highlightPendingSelection(tileX, tileY);
				}
			}

			else {
				// Pending selection (mouse was previously down)? -> Add
				// selected tiles to selection
				if (pendingSelection) {
					completePendingSelection(tileX, tileY);
				}
				// No pending selection (mouse was already up)? Just "highlight"
				// the tile underneath the cursor (draw
				// it as selected if its currently unselected and vice versa)
				else {
					highlightTile(tileX, tileY);
				}
			}

			lastX = tileX;
			lastY = tileY;
			lastMouseDown = mouseDown;
		}

	}

	private void highlightPendingSelection(final int tileX, final int tileY) {
		final boolean[][] highlighted = Arrays.copyOf(selected);

		applyPendingSelection(tileX, tileY, highlighted);

		map.setSelectionLayer(highlighted);
	}

	private void highlightTile(final int tileX, final int tileY) {
		// Copy the current selection array
		final boolean[][] highlighted = Arrays.copyOf(selected);

		// Flip selected state of the tile to "highlight"
		highlighted[tileX][tileY] = !highlighted[tileX][tileY];

		map.setSelectionLayer(highlighted);

	}

	private void initSelectionHandling() {
		hasAllMouseHandlers.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(final MouseDownEvent event) {
				mouseDown = true;
				handleMouseEvent(event.getRelativeX(map.getElement()), event.getRelativeY(map.getElement()), event.getNativeEvent()
						.getCtrlKey());

				event.preventDefault();
			}
		});
		hasAllMouseHandlers.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(final MouseMoveEvent event) {
				handleMouseEvent(event.getRelativeX(map.getElement()), event.getRelativeY(map.getElement()), event.getNativeEvent()
						.getCtrlKey());

				event.preventDefault();
			}
		});
		hasAllMouseHandlers.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(final MouseUpEvent event) {
				mouseDown = false;
				handleMouseEvent(event.getRelativeX(map.getElement()), event.getRelativeY(map.getElement()), event.getNativeEvent()
						.getCtrlKey());

				event.preventDefault();
			}
		});
	}

	private int relativeCoordinateToTile(final int coordinate) {
		final int tileSize = map.getTileSize();
		return coordinate / tileSize;
	}

	public void setSelectionHandlingEnabled(final boolean selectionHandlingEnabled) {
		this.selectionHandlingEnabled = selectionHandlingEnabled;
	}

	private void startPendingSelection(final int tileX, final int tileY, final boolean ctrlDown) {
		if (!ctrlDown) {
			clearSelection();
		}

		downOnX = tileX;
		downOnY = tileY;

		downOnSelected = selected[tileX][tileY];

		pendingSelection = true;
	}

}
