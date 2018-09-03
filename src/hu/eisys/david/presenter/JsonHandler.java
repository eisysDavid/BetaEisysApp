package hu.eisys.david.presenter;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.eisys.david.model.Script;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonHandler {

	private boolean isJSON;

	public void createJson(String path, Object saver)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(path + File.separator + IConstans.JSON_FILE_NAME), saver);
	}

	public Object reloadJSon(String path) throws JsonParseException, JsonMappingException, IOException {
		Object scr = new Object();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		mapper.enableDefaultTyping();
		scr = (Object) mapper.readValue(new File(path + File.separator + IConstans.JSON_FILE_NAME), Script.class);

		return scr;
	}

	public boolean isJSON(String path) {
		if (new File(path).exists()) {
			File[] file = new File(path).listFiles();
			for (File f : file) {
				f.setReadable(true);
				if (f.getName().equals(IConstans.JSON_FILE_NAME)) {
					isJSON = true;
				}
				if (f.isDirectory()) {
					isJSON(f.getAbsolutePath());
				}
			}
			return isJSON;
		} else {
			return false;
		}
	}
}
