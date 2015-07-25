import static rx.Observable.just;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Observable.Transformer;
import rx.Observer;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func2;
import rx.functions.Func3;

import com.github.davidmoten.rx.Transformers;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Observable<Integer> source = just(1, 1, 1, 2, 2, 3);

        List<List<Integer>> lists = source.compose(toListUntilChanged((a, b) -> a.equals(b)))
                .toList().toBlocking().single();
        System.out.println(lists);
    }

    public static <T> Transformer<T, List<T>> toListUntilChanged(Func2<T, T, Boolean> together) {
        Func0<List<T>> initialState = () -> Collections.emptyList();

        Func3<List<T>, T, Observer<List<T>>, List<T>> transition = (list, n, observer) -> {
            if (list.size() == 0 || together.call(list.get(list.size() - 1), n)) {
                return add(list, n);
            } else {
                observer.onNext(list);
                return Collections.singletonList(n);
            }

        };
        Action2<List<T>, Observer<List<T>>> completionAction = (list, observer) -> {
            if (list.size() > 0) {
                observer.onNext(list);
            }
        };
        return Transformers.stateMachine(initialState, transition, completionAction);
    }

    private static <T> List<T> add(List<T> list, T item) {
        List<T> result = new ArrayList<T>(list);
        result.add(item);
        return result;
    }
}
