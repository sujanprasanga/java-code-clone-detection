package lk.ac.mrt.cse.mscresearch.codeclones;

import java.util.function.Consumer;

public class EventManager {

	
	private final static EventManager instance = new EventManager();
	
	
	private Runnable updateView;
	private Consumer<String> updateStatus;
	
	public void fireUpdateView() {
		updateView.run();
	}
	
	public static EventManager get() {
		return instance;
	}

	public void setUpdateView(Runnable updateView) {
		this.updateView = updateView;
	}
	
	public void setUpdateStatusConsumer(Consumer<String> updateStatus) {
		this.updateStatus = updateStatus;
	}
	
	public void setStatus(String status) {
		this.updateStatus.accept(status);
	}
}
