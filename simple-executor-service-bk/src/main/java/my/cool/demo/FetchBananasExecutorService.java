package my.cool.demo;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FetchBananasExecutorService {
	
	private Integer numberOfBananasCollected;
	private static ExecutorService executorService = Executors.newFixedThreadPool(10);

	public FetchBananasExecutorService() {
		numberOfBananasCollected = 0;
	}
	
	private void gatherThemIn() {
		
		// store all our future objects
		ArrayList<FutureTask<Integer>> allMonkeys = new ArrayList<FutureTask<Integer>>();
		
		for(int i = 0; i < 10; i++) {
			MonkeyCallable monkeyCallable = new MonkeyCallable();
	        FutureTask<Integer> future = new FutureTask<Integer>(monkeyCallable);
	        executorService.execute(monkeyCallable);
			executorService.submit(future);
			allMonkeys.add(future);
		}

		for(FutureTask<Integer> futureMonkey : allMonkeys) {
			try {
				this.numberOfBananasCollected+= futureMonkey.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		executorService.shutdown();
		System.out.println("We have shutdown");
		System.out.println("Our monkeys have gathered us " + 
				this.numberOfBananasCollected + " bananas");
	}

	public static void main(String[] args) {
		FetchBananasExecutorService bananas = new FetchBananasExecutorService();
		bananas.gatherThemIn();
	}
}
