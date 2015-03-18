import rx.Observable;
import rx.schedulers.Schedulers;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Observable.just(1).subscribeOn(Schedulers.computation()).toBlocking()
				.first();
		System.out.println("finished");
	}
}
