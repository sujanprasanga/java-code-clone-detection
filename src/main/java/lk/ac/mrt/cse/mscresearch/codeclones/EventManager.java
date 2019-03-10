package lk.ac.mrt.cse.mscresearch.codeclones;

public class EventManager {

	
	private final static EventManager instance = new EventManager();
	
	
	private Runnable updateView;
	
	public void fireUpdateView() {
		updateView.run();
	}
	
	public static EventManager get() {
		return instance;
	}

	public void setUpdateView(Runnable updateView) {
		this.updateView = updateView;
	}
}
