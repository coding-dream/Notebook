package application.util;

public class ThreadUtils {
	public static void run(Runnable runnable){
		Thread thread = new Thread(runnable);
		thread.start();
	}
}
