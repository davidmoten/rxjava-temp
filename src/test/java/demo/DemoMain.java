package demo;

import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;

public class DemoMain {

    public static void main(String[] args) {
        Observable.range(1, 10).groupBy(i -> i % 2 == 0).flatMap(g -> Observable.defer(() -> {
            final AtomicBoolean isFirst = new AtomicBoolean(true);
            return g.filter(x -> isFirst.compareAndSet(true, false));
        })).subscribe(System.out::println);

    }

}
