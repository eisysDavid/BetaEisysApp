package hu.eisys.david.testDrive;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.eisys.david.model.*;
import hu.eisys.david.presenter.*;
import hu.eisys.david.view.IPopup;
import hu.eisys.david.view.IPopupModify;
import hu.eisys.david.view.MainWindow;
import hu.eisys.david.view.ModifyScript;
import hu.eisys.david.view.TreePathShow;
import hu.eisys.david.view.ViewPopupFrame;
import hu.eisys.david.view.MyTreePath;;

@ApplicationScoped
public class Presenter {

	@Inject
	MainWindow mainWindow;
	@Inject
	Script script;
	private final Logger LOGGER = LoggerFactory.getLogger(Presenter.class);
	private JFileChooser jfileChooser;
	private JsonHandler jsn;
	private StringSplit strngSlt;
	private boolean firstReload;
	private String path;
	private boolean isSelected;
	private ComboButton myCombo;

	public void start(@Observes ContainerInitialized startEvent) {
		MyResourceBundle.setResource();

		mainWindow.displayViewFrame();
		jfileChooser = new JFileChooser();
		jsn = new JsonHandler();
		strngSlt = new StringSplit();
		firstReload = true;
		isSelected = false;
		mainWindow.setTreeCellRenderel(new MyTreeCellRenderer());
		mainWindow.setTableCellRenderel(new MyTableRenderer());
		myCombo = new ComboButton();
		LOGGER.debug("Program Started: {}", Presenter.class.getName());
	}

	@SuppressWarnings("static-access")
	public void onSetTreePathEvent(@Observes @CDIEvent(MainWindow.SET_TREEMODEL) final ParameterDTO parameters) {
		path = parameters.getPrimaryParameter(String.class);
		isSelected = parameters.getSecondaryParameter(0, boolean.class);
		Component modelPath = parameters.getSecondaryParameter(1, Component.class);
		int keyCode = parameters.getSecondaryParameter(2, int.class);
		int eventID = parameters.getSecondaryParameter(3, int.class);
		LOGGER.debug("Key code {} , event Id {} ", keyCode, eventID);

		if (eventID == MainWindow.KEY_PRESSED) {
			if (keyCode == KeyEvent.VK_ENTER) {

				setTreeModel();
				mainWindow.expandTreeNodes(0);
				try {
					if (jsn.isJSON(path) && firstReload) {
						script = (Script) jsn.reloadJSon(path);
						setTableModel();
					}
				} catch (IOException e) {
					LOGGER.error("Json file error:{} ", e);

				}
				firstReload = false;

			}
		} else if (eventID == MainWindow.ACTION_LISTENER) {
			if (keyCode == mainWindow.CHECKBOX_NUMBER) {
				setTreeModel();
				mainWindow.expandTreeNodes(0);
			} else {
				jfileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (jfileChooser.showOpenDialog(modelPath) == JFileChooser.APPROVE_OPTION) {
					path = jfileChooser.getSelectedFile().getPath();
					mainWindow.setStringPath(path);
					mainWindow.setTreeModel(new MyTreeModel(jfileChooser.getSelectedFile(), isSelected, script));
					if (jsn.isJSON(jfileChooser.getSelectedFile().getAbsolutePath()) && firstReload) {
						try {
							script = (Script) jsn.reloadJSon(path);
							setTableModel();
						} catch (IOException e) {
							LOGGER.error("Json file reload error:{} ", e);
						}
					}
					firstReload = false;
					mainWindow.expandTreeNodes(0);
				}
			}
		}
	}

	public void onUploadTableEvent(@Observes @CDIEvent(MainWindow.ON_UPLOADTABLE) final ParameterDTO parameters) {
		if (parameters.getSecondaryParameter(1, String.class) != null) {
			int keyCode = parameters.getPrimaryParameter(int.class);
			int eventID = parameters.getSecondaryParameter(0, int.class);
			strngSlt.splitElement((parameters.getSecondaryParameter(1, String.class)));
			String element = strngSlt.getElement();
			String showElement = strngSlt.getShowElement();
			boolean isDirectory = strngSlt.isDirectory(element);
			int position = parameters.getSecondaryParameter(2, int.class);
			int selection = parameters.getSecondaryParameter(3, int.class);

			if ((eventID == MainWindow.MOUSE_LISTENER) && (keyCode == 2)) {
				if (position == -1) {
					if (isDirectory) {
						script.addSteps(element, showElement);
						LOGGER.info(element + MyResourceBundle.myResource.getString("addContentText"));
					} else {
						script.addStep(new Efile(element, showElement));
						LOGGER.info(showElement + MyResourceBundle.myResource.getString("itemAddText"));
					}
				} else {
					if (isDirectory) {
						script.addSteps(element, showElement, position + 1);
						LOGGER.info(showElement + MyResourceBundle.myResource.getString("addContentText"));
					} else {
						script.addStep(new Efile(element, showElement), position + 1);
						LOGGER.info(element + MyResourceBundle.myResource.getString("itemAddText"));
					}
				}
				if (position != -1) {
					setTableModel(position + 1);
				} else {
					setTableModel(script.getStepContainer().size() - 1);
				}
				LOGGER.debug("Selected: " + isSelected + " IsEmpty?: " + script.getStepContainer().size() + " Path: "
						+ path);
				onCreateJsonEvent();

			} else if ((eventID == MainWindow.KEY_PRESSED) && (keyCode == KeyEvent.VK_ENTER)) {
				if (position == -1) {
					if (isDirectory) {
						script.addSteps(element, showElement);
						LOGGER.info(element + MyResourceBundle.myResource.getString("addContentText"));
					} else {
						script.addStep(new Efile(element, showElement));
						LOGGER.info(showElement + MyResourceBundle.myResource.getString("itemAddText"));
					}
				} else {
					if (isDirectory) {
						script.addSteps(element, showElement, position + 1);
						LOGGER.info(element + MyResourceBundle.myResource.getString("addContentText"));
					} else {
						script.addStep(new Efile(element, showElement), position + 1);
						LOGGER.info(showElement + MyResourceBundle.myResource.getString("itemAddText"));
					}
				}
				if (position != -1) {
					setTableModel(position + 1);
				} else {
					setTableModel(script.getStepContainer().size() - 1);
				}
				onCreateJsonEvent();

			}
			if (isSelected) {
				setTreeModel();
				mainWindow.expandTreeNodes(0);
				mainWindow.setTreeSelection(selection);
			}
		}

	}

	public void onExpendTreePathEvent(@Observes @CDIEvent(MainWindow.ON_TREEPOPUP) final ParameterDTO parameters) {
		boolean clicked = ((parameters.getPrimaryParameter(int.class)
				& parameters.getSecondaryParameter(0, int.class)) != 0);

		int selectedRow = parameters.getSecondaryParameter(1, int.class);

		if (clicked) {
			mainWindow.setTreePopup(new TreePathShow(new MyTreePath() {
				public void expasndTree() {
					mainWindow.expandTreeNodes(selectedRow);
				}

				public void collapseTree() {
					mainWindow.collapseTreeNodes(selectedRow);

				}
			}));
		}
	}

	public void onExitEvent(@Observes @CDIEvent(MainWindow.ON_EXIT) final ParameterDTO parameters) {
		int dialogButton = JOptionPane.showConfirmDialog(null, MyResourceBundle.myResource.getString("exitText"),
				MyResourceBundle.myResource.getString("exitTitle"), JOptionPane.OK_CANCEL_OPTION);
		LOGGER.info("Save: {}", dialogButton);
		if (dialogButton == JOptionPane.YES_OPTION) {
			mainWindow.systemExit();
		}
	}

	public void onPopupEvent(@Observes @CDIEvent(MainWindow.ON_POPUP) final ParameterDTO parameters) {
		boolean clicked = ((parameters.getPrimaryParameter(int.class)
				& parameters.getSecondaryParameter(0, int.class)) != 0);
		Container container = parameters.getSecondaryParameter(1, Container.class);
		int tableRow = parameters.getSecondaryParameter(2, int.class);
		int[] removeElement = parameters.getSecondaryParameter(3, int[].class);
		if (clicked) {
			new ViewPopupFrame().display(container, new IPopup() {
				public void dbChangeEvent(String dbName, String userName, String password) {
					if (tableRow == -1) {
						script.addStep(new DbChange(dbName, userName, password));
						LOGGER.info(script.getStepContainer().get(script.getStepContainer().size() - 1).getShowElement()
								+ MyResourceBundle.myResource.getString("itemAddText"));
					} else {
						script.addStep(new DbChange(dbName, userName, password), tableRow + 1);
						LOGGER.info(script.getStepContainer().get(tableRow + 1).getShowElement()
								+ MyResourceBundle.myResource.getString("itemAddText"));
					}
					setTableModel(tableRow + 1);
					onCreateJsonEvent();
				}

				public void addScriptEvent(String scriptName, String myScript) {
					if (tableRow == -1) {
						script.addStep(new Adhock(scriptName, myScript));
						LOGGER.info(script.getStepContainer().get(script.getStepContainer().size() - 1).getShowElement()
								+ MyResourceBundle.myResource.getString("itemAddText"));
					} else {
						script.addStep(new Adhock(scriptName, myScript), tableRow + 1);
						LOGGER.info(script.getStepContainer().get(tableRow + 1).getShowElement()
								+ MyResourceBundle.myResource.getString("itemAddText"));
					}
					setTableModel(tableRow + 1);
					onCreateJsonEvent();
				}

				public void removeElementEvent() {
					removeElem(removeElement);
					onCreateJsonEvent();
				}

				public Object modifyEvent() {
					mainWindow.setTableSelection(tableRow);
					return script.getStepContainer().get(tableRow);
				}

				public void modifyDbChangeEvent(String dbName, String userName, String password) {
					LOGGER.info(script.getStepContainer().get(tableRow).getShowElement()
							+ MyResourceBundle.myResource.getString("itemChangeText") + userName + "@" + dbName);
					script.modifyDbchange(tableRow, dbName, userName, password, script.getComment(tableRow));
					setTableModel(tableRow);
					onCreateJsonEvent();
				}

				public void modifyScriptEvent(String scriptName, String myScript) {
					LOGGER.info(script.getStepContainer().get(tableRow).getShowElement()
							+ MyResourceBundle.myResource.getString("itemChangeText") + scriptName);
					script.modifyAdhock(tableRow, scriptName, myScript, script.getComment(tableRow));
					setTableModel(tableRow);
					onCreateJsonEvent();
				}

			}, script.getType(tableRow));
		}
	}

	public void onModifyEnterEvent(@Observes @CDIEvent(MainWindow.ON_MODIFYENTER) final ParameterDTO parameters) {
		boolean enterPressed = (parameters.getPrimaryParameter(int.class) == KeyEvent.VK_ENTER);
		int index = parameters.getSecondaryParameter(0, int.class);
		int column = parameters.getSecondaryParameter(1, int.class);

		if (enterPressed && column == 1) {
			new ModifyScript().onlyModify(new IPopupModify() {
				public void modifyScriptEvent(String scriptName, String myScript) {
					LOGGER.info(script.getStepContainer().get(index).getShowElement()
							+ MyResourceBundle.myResource.getString("itemChangeText") + scriptName);
					script.modifyAdhock(index, scriptName, myScript, script.getComment(index));
					setTableModel(index);
					onCreateJsonEvent();
				}

				public Object modifyEvent() {
					mainWindow.setTableSelection(index - 1);
					return script.getStepContainer().get(index);
				}

				public void modifyDbChangeEvent(String dbName, String userName, String password) {
					LOGGER.info(script.getStepContainer().get(index).getShowElement()
							+ MyResourceBundle.myResource.getString("itemChangeText") + userName + "@" + dbName);
					script.modifyDbchange(index, dbName, userName, password, script.getComment(index));
					setTableModel(index);
					onCreateJsonEvent();
				}

				public void removeElementEvent() {
				}
			}, script.getType(index));

		}
	}

	public void onModifyDoubleClickEvent(
			@Observes @CDIEvent(MainWindow.ON_MODIFYDOUBLECLICK) final ParameterDTO parameters) {
		boolean doubleClicked = (parameters.getPrimaryParameter(int.class) == 2);
		int index = parameters.getSecondaryParameter(1, int.class);
		int column = parameters.getSecondaryParameter(2, int.class);
		int[] removeElement = parameters.getSecondaryParameter(3, int[].class);

		if (doubleClicked && column == 1) {
			new ModifyScript().onlyModify(new IPopupModify() {
				public void modifyScriptEvent(String scriptName, String myScript) {
					LOGGER.info(script.getStepContainer().get(index).getShowElement()
							+ MyResourceBundle.myResource.getString("itemChangeText") + scriptName);
					script.modifyAdhock(index, scriptName, myScript, script.getComment(index));
					setTableModel(index);
					onCreateJsonEvent();
				}

				public Object modifyEvent() {
					mainWindow.setTableSelection(index);
					return script.getStepContainer().get(index);
				}

				public void modifyDbChangeEvent(String dbName, String userName, String password) {
					LOGGER.info(script.getStepContainer().get(index).getShowElement()
							+ MyResourceBundle.myResource.getString("itemChangeText") + userName + "@" + dbName);
					script.modifyDbchange(index, dbName, userName, password, script.getComment(index));
					setTableModel(index);
					onCreateJsonEvent();
				}

				public void removeElementEvent() {
					removeElem(removeElement);
					onCreateJsonEvent();
				}
			}, script.getType(index));

		}

	}

	public void onRemoveElement(@Observes @CDIEvent(MainWindow.ON_REMOVE) final ParameterDTO parameters) {
		int keyEvent = parameters.getPrimaryParameter(int.class);
		int[] removeElement = parameters.getSecondaryParameter(0, int[].class);

		if (keyEvent == KeyEvent.VK_DELETE) {
			removeElem(removeElement);
			onCreateJsonEvent();
		}
	}

	public void onSetCommentEvent(@Observes @CDIEvent(MainWindow.ON_SETCOMMENT) final ParameterDTO parameters) {
		String comment = parameters.getPrimaryParameter(String.class);
		int xPosition = parameters.getSecondaryParameter(0, int.class);
		int yPosition = parameters.getSecondaryParameter(1, int.class);
		LOGGER.debug("onSetCommentEvent {} ", Arrays.toString(parameters.getSecondaryParameters()));
		if (yPosition == 2 && xPosition >= 0) {
			script.setComment(comment, xPosition);
			// ezt lehet nem kell posotionalni
			setTableModel(xPosition);
			onCreateJsonEvent();
		}

	}

	public void onComboMoveEvent(@Observes @CDIEvent(MainWindow.ON_COMBOKEYMOVE) final ParameterDTO parameters) {
		int keyCode = parameters.getPrimaryParameter(int.class);
		int[] rowNumber = parameters.getSecondaryParameter(0, int[].class);
		int columnNumber = parameters.getSecondaryParameter(1, int.class);
		int getID = parameters.getSecondaryParameter(2, int.class);

		if (getID == MainWindow.KEY_PRESSED) {
			myCombo.setPressedbutton(keyCode);

			if (myCombo.isGoUp() && (rowNumber[0] - 1 >= 0) && columnNumber == 1) {
				script.moveStep(rowNumber, rowNumber[0] - 1, IConstans.WAY_UP);
				setTableModel(rowNumber[0] - 1);
				onCreateJsonEvent();
			}

			if (myCombo.isGoDown() && (rowNumber[rowNumber.length - 1] + 1) < script.getStepContainer().size()
					&& columnNumber == 1) {
				script.moveStep(rowNumber, rowNumber[rowNumber.length - 1] + 1, IConstans.WAY_DOWN);
				setTableModel(rowNumber[rowNumber.length - 1] + 1);
				onCreateJsonEvent();
			}
		}

		else if (getID == MainWindow.KEY_RELEASE) {
			myCombo.setReleseButton(keyCode);
		}
	}

	public void onDND(@Observes @CDIEvent(MainWindow.ON_DND) final ParameterDTO parameters) {
		int[] arrayParam = parameters.getPrimaryParameter(int[].class);
		int dropPos = parameters.getSecondaryParameter(0, int.class);
		if (arrayParam[0] > dropPos) {
			script.moveStepDND(arrayParam, dropPos, IConstans.WAY_UP);
		} else if (arrayParam[0] < dropPos) {
			script.moveStepDND(arrayParam, dropPos, IConstans.WAY_DOWN);
		}

		int selecTion = !script.getStepContainer().isEmpty() && dropPos - (arrayParam.length) >= 0
				? dropPos - (arrayParam.length - 1)
				: dropPos;
		setTableModel(selecTion);
		onCreateJsonEvent();
	}

	public void removeElem(int[] removeElement) {
		LOGGER.debug("{}", Arrays.toString(removeElement));
		for (int i = removeElement.length - 1; i > -1; i--) {
			if (script.getType(removeElement[i]).equals(StepNames.ADHOCK)) {
				int dialogButton = JOptionPane.showConfirmDialog(null,
						MyResourceBundle.myResource.getString("deleteAdhockText"),
						MyResourceBundle.myResource.getString("deleteAdhockTitle"), JOptionPane.OK_CANCEL_OPTION);
				if (dialogButton == JOptionPane.YES_OPTION) {
					LOGGER.info(script.getStepContainer().get(removeElement[i]).getShowElement()
							+ MyResourceBundle.myResource.getString("tableRemove"));
					script.removeStep(script.getStepContainer().get(removeElement[i]));
				}
			} else {
				LOGGER.info(script.getStepContainer().get(removeElement[i]).getShowElement()
						+ MyResourceBundle.myResource.getString("tableRemove"));
				script.removeStep(script.getStepContainer().get(removeElement[i]));
			}

			int select = script.getStepContainer().isEmpty() ? -1 : removeElement[0] - 1;
			setTableModel(select);
			if (isSelected) {
				setTreeModel();
				mainWindow.expandTreeNodes(0);
			}
		}
	}

	public void setTreeModel() {
		if (path != null && script != null) {
			mainWindow.setTreeModel(new MyTreeModel(new File(path), isSelected, script));
		} else {
			LOGGER.error(path + " is null \n" + script + " is null");
		}
	}

	public void setTableModel(int focus) {
		try {
			mainWindow.setTableMode(new MyTModel(script));
			mainWindow.setTableSelection(focus);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	public void setTableModel() {
		try {
			mainWindow.setTableMode(new MyTModel(script));
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	public void onCreateJsonEvent() {
		try {
			if (path != null) {
				jsn.createJson(path, script);
			}
			LOGGER.debug("JSON file has been created: {}", path);
		} catch (IOException e) {
			LOGGER.error("Json create error {}", e);
		}
	}
}
