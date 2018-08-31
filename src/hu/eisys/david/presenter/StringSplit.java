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

		int index = p.length >= 2 ? 1 : 0;
		element = p[index];
		showElement = index == 0 ? IConstans.EMPTY_STRING
				: p[index].split(sep + sep)[p[index].split(sep + sep).length - 1];
		for (int i = index + 1; i < p.length; i++) {
			element += sep + p[i].split(sep + sep)[p[i].split(sep + sep).length - 1];
			showElement += sep + p[i].split(sep + sep)[p[i].split(sep + sep).length - 1];
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
