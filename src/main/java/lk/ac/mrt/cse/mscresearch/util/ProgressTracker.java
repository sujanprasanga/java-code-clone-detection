package lk.ac.mrt.cse.mscresearch.util;

import java.util.concurrent.atomic.AtomicInteger;

public class ProgressTracker {

	private static final AtomicInteger done = new AtomicInteger();
	private static final AtomicInteger count = new AtomicInteger();
	
	public static void addToCount(int c) {
		count.addAndGet(c);
	}
	
	public static void notifyDone() {
		done.incrementAndGet();
		System.out.println(done.get() + " of " + count.get());
	}
}
