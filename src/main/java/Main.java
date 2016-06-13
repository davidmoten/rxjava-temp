import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Observable<Integer> o = Observable.range(1, Integer.MAX_VALUE)
                // .doOnRequest(n -> System.out.println("requested b " + n)) //
                .onBackpressureBuffer() //
                // .doOnRequest(n -> System.out.println("requested a " + n)) //
                .subscribeOn(Schedulers.io()) //
                // .subscribeOn(Schedulers.io()) //
                .subscribeOn(Schedulers.io()) //
        // .doOnRequest(n -> System.out.println("requested m " + n)) //
        ; //
        o.doOnNext(System.out::println) //
                .take(1000) //
                .count() //
                .timeout(3, TimeUnit.SECONDS) //
                .toBlocking().single();
    }
}