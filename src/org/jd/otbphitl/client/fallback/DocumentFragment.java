package org.jd.otbphitl.client.fallback;

import com.google.gwt.dom.client.Node;

public class DocumentFragment extends Node {

	public static native DocumentFragment create() /*-{
		return $doc.createDocumentFragment();
	}-*/;

	protected DocumentFragment() {

	}

}
