package hu.eisys.david.view;

public interface IPopupModify {

	public Object modifyEvent();
	public void modifyDbChangeEvent(String dbName,String userName,String password);
	public void modifyScriptEvent(String scriptName,String myScript);
	public void removeElementEvent();
}

