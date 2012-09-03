package org.jd.otbphitl.client.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface TestResources extends ClientBundle {

	TestResources	INSTANCE	= GWT.create(TestResources.class);

	ImageResource blackoverlay();

	ImageResource blue();

	ImageResource red();

	ImageResource xoverlay();

}
