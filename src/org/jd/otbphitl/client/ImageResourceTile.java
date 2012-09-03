package org.jd.otbphitl.client;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

public class ImageResourceTile implements Tile {

	private final ImageResource	resource;

	public ImageResourceTile(final ImageResource resource) {
		this.resource = resource;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ImageResourceTile other = (ImageResourceTile) obj;
		if (resource == null) {
			if (other.resource != null) {
				return false;
			}
		}
		else if (!resource.equals(other.resource)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		return result;
	}

	@Override
	public void populateImg(final Image img) {
		img.setUrl(resource.getSafeUri());
	}

	@Override
	public void populateImg(final ImageElement img) {
		img.setSrc(resource.getSafeUri().asString());
	}

}
