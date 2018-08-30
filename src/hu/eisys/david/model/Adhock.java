package hu.eisys.david.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Adhock implements Step {

	private String scriptName;
	private String script;
	private String comment;

	public Adhock() {
	}

	public Adhock(String scriptName, String script) {

		this.scriptName = scriptName;
		this.script = script;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
			return comment;
	}

	@JsonIgnore
	public StepNames getType() {
		return StepNames.ADHOCK;
	}

	@JsonIgnore
	public String getShowElement() {
		return this.scriptName;
	}
}
