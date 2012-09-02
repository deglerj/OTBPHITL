package org.jd.otbphitl.client;

import com.google.gwt.user.client.ui.Image;

public class ImageURLTile implements Tile {

	private final String	url;

	public ImageURLTile(final String url) {
		this.url = url;
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
		final ImageURLTile other = (ImageURLTile) obj;
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		}
		else if (!url.equals(other.url)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public void populateImg(final Image img) {
		img.setUrl(url);
	}

}
