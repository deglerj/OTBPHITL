package org.jd.otbphitl.client.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Images extends ClientBundle {

	Images	BUNDLE	= GWT.create(Images.class);

	ImageResource empty();

}
