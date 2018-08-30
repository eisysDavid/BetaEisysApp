package hu.eisys.david.view;

public interface IPopup extends IPopupModify{
	
	public void dbChangeEvent(String dbName,String userName,String password);
	public void addScriptEvent(String scriptName,String myScript);
}
