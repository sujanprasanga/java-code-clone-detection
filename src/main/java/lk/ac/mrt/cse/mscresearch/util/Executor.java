package lk.ac.mrt.cse.mscresearch.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.apache.log4j.Logger;

public class Executor {

	private final static Logger log = Logger.getLogger(Executor.class);
	
	private static final ExecutorService concurrentExecutorService = Executors.newCachedThreadPool();
	private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	private Executor(){}
	
//	public static <R, D, T> Collection<R> execute(Collection<D> data, Function<D,Collection<R>> function){
//		Executor<R, D, T> fastExecutor = new Executor();
//		Collection<R> submitTasks = fastExecutor.submitTasks(data, function);
//		fastExecutor.shutDown();
//		return submitTasks;
//	}
	
	public static void execute(Runnable r) {
		executorService.submit(r);
	}
	
	public static void executeConcurrent(Runnable r) {
		concurrentExecutorService.submit(r);
	}
	
//	private void shutDown() {
//		executorService.shutdownNow();
//	}

//	public Collection<R> submitTasks(Collection<D> data, Function<D,Collection<R>> function){
//		final List<R> e = Collections.synchronizedList(new ArrayList<>());
//		final List<Future<Collection<R>>> f = new ArrayList<>();
//		for(D d : data){
//				f.add(executorService.submit(()->getErrorLoggedFunction(function, d)));
//		}
//		for(Future<Collection<R>> r : f){
//			try {
//				if(r.get() != null || !r.get().isEmpty())
//				{
//					e.addAll(r.get());
//				}
//			} catch (Exception e1) {
//				log.error("", e1);
//			} 
//		}
//		return e;
//	}
//
//	private Collection<R> getErrorLoggedFunction(Function<D, Collection<R>> function, D d) {
//		try{
//			return function.apply(d);
//		}
//		catch(RuntimeException e){
//			log.error(d.toString(), e);
//		}
//		return Collections.emptyList();
//	}
}
