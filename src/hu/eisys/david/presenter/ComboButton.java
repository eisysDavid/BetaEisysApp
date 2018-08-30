package hu.eisys.david.presenter;

import java.awt.event.KeyEvent;

public class ComboButton {

	private boolean ctrl;
	private boolean uP;
	private boolean down;
	private boolean goUp;
	private boolean goDown;

	public boolean isGoUp() {
		return goUp;
	}

	public boolean isGoDown() {
		return goDown;
	}

	public ComboButton() {
		this.ctrl = false;
		this.uP = false;
		this.down = false;
		this.goUp = false;
		this.goDown = false;
	}

	public void setPressedbutton(int keyCode) {
		if (keyCode == KeyEvent.VK_CONTROL) {
			ctrl = true;
		}

		if (keyCode == KeyEvent.VK_UP) {
			uP = true;
		}

		if (keyCode == KeyEvent.VK_DOWN) {
			down = true;
		}

		if (ctrl == true && uP == true) {
			goUp = true;
		} else {
			goUp = false;
		}

		if (ctrl == true && down == true) {
			goDown = true;
		} else {
			goDown = false;
		}
	}

	public void setReleseButton(int keyCode) {
		if (keyCode == KeyEvent.VK_CONTROL) {
			ctrl = false;
		}

		if (keyCode == KeyEvent.VK_UP) {
			uP = false;
		}

		if (keyCode == KeyEvent.VK_DOWN) {
			down = false;
		}
	}

}
