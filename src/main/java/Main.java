import rx.Observable;
import rx.schedulers.Schedulers;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Observable.just(1).subscribeOn(Schedulers.computation()).toBlocking()
				.first();
		System.out.println("finished");
		Observable<Integer> o = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		Observable<Integer> o2 = o.groupBy(i -> i % 5).concatMap(g -> g);

	}
}
