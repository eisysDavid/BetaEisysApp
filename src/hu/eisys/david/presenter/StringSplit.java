package hu.eisys.david.presenter;

import java.io.File;

public class StringSplit {

	private String element;
	private String showElement;

	public String getElement() {
		return element;
	}

	public StringSplit() {
	}

	public void splitElement(String treePath) {
		String[] p = treePath.split(", ");

		String sep = File.separator;

		element = p[0];
		showElement = p[0].split(sep + sep)[p[0].split(sep + sep).length - 1];
		for (int i = 1; i < p.length; i++) {
			element += sep + sep + p[i].split(sep + sep)[p[i].split(sep + sep).length - 1];
			showElement += sep + sep + p[i].split(sep + sep)[p[i].split(sep + sep).length - 1];
		}

		element = element.replace("]", IConstans.EMPTY_STRING);
		element = element.replace("[", IConstans.EMPTY_STRING);

		showElement = showElement.replace("]", IConstans.EMPTY_STRING);
		showElement = showElement.replace("[", IConstans.EMPTY_STRING);
	}

	public boolean isDirectory(String path) {
		java.io.File fl = new java.io.File(path);

		if (fl.isDirectory()) {
			return true;
		} else {
			return false;
		}
	}

	public String getShowElement() {
		return showElement;
	}

}
