package demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import rx.Observable;
import rx.Observer;
import rx.functions.Func0;
import rx.functions.Func3;
import rx.observables.ConnectableObservable;
import rx.subjects.PublishSubject;

import com.github.davidmoten.rx.Actions;
import com.github.davidmoten.rx.Transformers;
import com.github.davidmoten.rx.util.Pair;

public class DemoMain {

	public static void main(String[] args) {
		PublishSubject<Pair<Integer, Integer>> subject = PublishSubject
				.create();
		Map<Integer, Integer> initialMap = Collections.emptyMap();
		Pair<Map<Integer, Integer>, Integer> initial = Pair.create(initialMap,
				null);
		ConnectableObservable<Pair<Map<Integer, Integer>, Integer>> o = subject
				.scan(initial,
						(mapAndLatestKey, pair) -> {
							HashMap<Integer, Integer> map2 = new HashMap<>(
									mapAndLatestKey.left());
							map2.put(pair.left(), pair.right());
							return Pair.create(map2, pair.left());
						})
				//
				.replay(1);
		o.connect();

		Func0<Boolean> stateFactory = () -> true;
		Func3<Boolean, Pair<Map<Integer, Integer>, Integer>, Observer<Pair<Integer, Integer>>, Boolean> transition = (
				isFirst, pair, observer) -> {
			if (isFirst) {
				for (Entry<Integer, Integer> entry : pair.left().entrySet()) {
					Pair<Integer, Integer> p = Pair.create(entry.getKey(),
							entry.getValue());
					observer.onNext(p);
				}
			} else {
				Map<Integer, Integer> map = pair.left();
				Pair<Integer, Integer> p = Pair.create(pair.right(),
						map.get(pair.right()));
				observer.onNext(p);
			}
			return false;
		};

		Observable<Pair<Integer, Integer>> obs = o.compose(Transformers
				.stateMachine(stateFactory, transition, Actions.doNothing2()));

		final List<Pair<Integer, Integer>> a = new ArrayList<>();
		subject.onNext(Pair.create(0, 1));
		subject.onNext(Pair.create(0, 2));
		subject.onNext(Pair.create(1, 0));
		subject.onNext(Pair.create(1, 1));
		obs.subscribe(pair -> a.add(pair));
		subject.onNext(Pair.create(0, 3));
		subject.onNext(Pair.create(1, 2));
		final List<Pair<Integer, Integer>> b = new ArrayList<>();
		obs.subscribe(pair -> b.add(pair));
		subject.onNext(Pair.create(2, 1));
		System.out.println("a sees " + a);
		System.out.println("b sees " + b);

	}

}
