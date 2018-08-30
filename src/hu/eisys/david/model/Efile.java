package hu.eisys.david.model;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Efile implements Step {

	private String comment;
	private String showElement;
	private File file;

	public Efile(String path, String showElement) {
		this.file = new File(path);
		this.showElement = showElement;
	}

	public Efile() {
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
			return comment;
	}

	@JsonIgnore
	public StepNames getType() {
		return StepNames.EFIEL;
	}

	public String getShowElement() {
		return showElement;
	}
}
