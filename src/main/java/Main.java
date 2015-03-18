import rx.Observable;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Observable.just(1).subscribe();
		System.out.println("waiting...");
		Thread.sleep(10000);
		System.out.println("finished");
	}
}
