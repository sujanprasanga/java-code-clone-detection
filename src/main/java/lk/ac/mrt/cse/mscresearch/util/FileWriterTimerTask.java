package lk.ac.mrt.cse.mscresearch.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

public class FileWriterTimerTask extends java.util.TimerTask {

	
	private final File file;
	private final Timer timer;
	private boolean cancel;
	
	public FileWriterTimerTask(File file) {
		this.file = file;
		timer = new Timer(false);
		timer.scheduleAtFixedRate(this, 10000, 10000);
	}
	
	private final List<String> tasks = new LinkedList<>();
	
	public synchronized void addTask(String s) {
		tasks.add(s);
	}
	
	public boolean scheduleCancel() {
		
		return true;
	}
	
	@Override
	public void run() {
		if(tasks.isEmpty()) {
			return;
		}
		List<String> tmpTasks = new ArrayList<>(tasks);
		try(FileWriter w = new FileWriter(file, true)){
			tasks.stream().forEach(
					s->{
				try {
					w.write(s+"\r\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			tasks.removeAll(tmpTasks);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(cancel) {
			super.cancel();
		}
	}

}
