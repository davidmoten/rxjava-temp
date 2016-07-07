import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.subjects.PublishSubject;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		PublishSubject<Long> subject = PublishSubject.create();
		subject.mergeWith(Observable.interval(1, TimeUnit.SECONDS)) //
				.takeWhile(n -> n != -1) //
				.doOnCompleted(() -> System.out.println("completed"))
				.doOnNext(System.out::println) //
				.subscribe();
		Thread.sleep(3100);
		subject.onNext(-1L);
		Thread.sleep(2000);
	}
}
