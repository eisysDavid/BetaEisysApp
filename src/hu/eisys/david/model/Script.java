package hu.eisys.david.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import hu.eisys.david.presenter.IConstans;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Script {

	private List<Step> stepContainer = new ArrayList<>();

	// Lista getter
	public List<Step> getStepContainer() {
		return stepContainer;
	}

	// Lista setter
	public void setStepContainer(ArrayList<Step> stepContainer) {
		this.stepContainer = stepContainer;
	}

	// Item hozzáadás
	public void addStep(Step step) {
		stepContainer.add(step);
	}

	// Item konkrét helyre adás
	public void addStep(Step step, int position) {
		stepContainer.add(position, step);
	}

	// Item eltávolítása
	public void removeStep(Step step) {
		int position = stepContainer.indexOf(step);
		if (position > -1) {
			stepContainer.remove(position);
		}
	}

	// átírni tömbösre!
	public void moveStep(int startIndex[], int targetIndex, String way) {

		for (int rowIndex : startIndex) {
			Step elementOne = stepContainer.get(rowIndex);
			stepContainer.add(rowIndex, stepContainer.get(targetIndex));
			stepContainer.remove(rowIndex + 1);
			stepContainer.add(targetIndex, elementOne);
			stepContainer.remove(targetIndex + 1);
			if (way.equals(IConstans.WAY_UP)) {
				targetIndex = rowIndex;
			} else {
			}

		}
	}

	public void moveStepDND(int startIndex[], int targetIndex, String way) {
		int uPindex = startIndex[0];
		for (int rowIndex : startIndex) {
			if (way == IConstans.WAY_DOWN) {
				stepContainer.add(targetIndex + 1, stepContainer.get(uPindex));
				stepContainer.remove(uPindex);
			} else if (way == IConstans.WAY_UP) {
				stepContainer.add(targetIndex, stepContainer.get(rowIndex));
				stepContainer.remove(rowIndex + 1);
				targetIndex++;
			}
		}
	}

	// Comment beállítása
	public void setComment(String comment, int position) {
		stepContainer.get(position).setComment(comment);
	}

	// Comment lekérdezése
	public String getComment(int position) {
		return stepContainer.get(position).getComment();
	}

	// Milyen típúsú az adott item
	public StepNames getType(int position) {

		if (position != -1)
			return stepContainer.get(position).getType();

		else
			return StepNames.NOMYFILE;
	}

	// Adott lista módosítása
	public void modifyDbchange(int position, String dbName, String userName, String password, String comment) {
		DbChange db = (DbChange) stepContainer.get(position);
		db.setDataBase(dbName, userName, password);
		db.setComment(comment);
		stepContainer.remove(position);
		stepContainer.add(position, db);
	}

	// Adott lista módosítása
	public void modifyAdhock(int position, String scriptName, String script, String comment) {
		Adhock adhock = (Adhock) stepContainer.get(position);
		adhock.setScriptName(scriptName);
		adhock.setScript(script);
		adhock.setComment(comment);
		stepContainer.remove(position);
		stepContainer.add(position, adhock);
	}

	// Több elem hozzáadása egyszerre
	public void addSteps(String normalPath, String strBuild) {
		File[] file = new File(normalPath).listFiles();
		for (File fl : file) {
			if (fl.isFile()) {
				stepContainer.add(new Efile(normalPath + File.separator + fl.getName(),
						strBuild + File.separator + fl.getName()));
			}
		}
	}

	public void addSteps(String normalPath, String strBuild, int position) {
		File[] file = new File(normalPath).listFiles();
		for (File fl : file) {
			if (fl.isFile()) {
				stepContainer.add(position, new Efile(normalPath + File.separator + fl.getName(),
						strBuild + File.separator + fl.getName()));
			}
		}
	}

	public String showStepName(int index) {
		if (stepContainer.get(index).getType().equals(StepNames.EFIEL)) {
			Efile ef = (Efile) stepContainer.get(index);
			return ef.getFile().getAbsolutePath();
		}

		else if (stepContainer.get(index).getType().equals(StepNames.DBCHANGE)) {
			DbChange db = (DbChange) stepContainer.get(index);
			return db.getDbName();
		}

		else {
			Adhock ad = (Adhock) stepContainer.get(index);
			return ad.getScriptName();
		}
	}
}
