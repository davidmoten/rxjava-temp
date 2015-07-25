import static rx.Observable.just;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Observable.Transformer;
import rx.Observer;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func3;

import com.github.davidmoten.rx.Transformers;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Observable<Integer> source = just(1, 1, 1, 2, 2, 3);

        Func0<List<Integer>> initialState = () -> Collections.emptyList();

        Func3<List<Integer>, Integer, Observer<List<Integer>>, List<Integer>> transition = (list,
                n, observer) -> {
            if (list.size() == 0)
                return add(list, n);
            else if (list.get(0).equals(n)) {
                return add(list, n);
            } else {
                observer.onNext(list);
                return Collections.singletonList(n);
            }

        };
        Action2<List<Integer>, Observer<List<Integer>>> completionAction = (list, observer) -> {
            if (list.size() > 0) {
                observer.onNext(list);
            }
        };
        Transformer<Integer, List<Integer>> transformer = Transformers
                .<List<Integer>, Integer, List<Integer>> stateMachine(initialState, transition,
                        completionAction);
        System.out.println(source.compose(transformer).toList().toBlocking().single());
    }

    private static <T> List<T> add(List<T> list, T item) {
        List<T> result = new ArrayList<T>(list);
        result.add(item);
        return result;
    }
}
