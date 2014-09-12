package my.cool.demo;

import java.util.Random;
import java.util.concurrent.Callable;

public class MonkeyCallable implements Callable<Integer>, Runnable {

	private Random random;

	private Integer numberOfBananasGathered;
	
	public MonkeyCallable() {
		numberOfBananasGathered = 0;
		random = new Random();
	}
	
	@Override
	public void run() {
		numberOfBananasGathered+= this.random.nextInt(10);
	}
	
	public Integer call() throws Exception {
		return this.numberOfBananasGathered;
	}

}
