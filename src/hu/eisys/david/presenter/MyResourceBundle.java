package hu.eisys.david.presenter;

import java.util.Locale;
import java.util.ResourceBundle;

public class MyResourceBundle {

	public static ResourceBundle myResource;

	public static void setResource() {
		if (!Locale.getDefault().toString().equals("en_US") && !Locale.getDefault().toString().equals("hu_HU")) {
			Locale.setDefault(new Locale("en", "US"));
			myResource = ResourceBundle.getBundle(IConstans.RESOURCE_TEXTS);
		}

		else {
			myResource = ResourceBundle.getBundle(IConstans.RESOURCE_TEXTS);
		}
	}

}
