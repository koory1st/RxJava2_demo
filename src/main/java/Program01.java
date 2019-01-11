/**
 * 参考：
 * https://www.jianshu.com/p/464fa025229e
 */

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Program01 {
    public static void main(String[] args) {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> observableEmitter) throws Exception {
                observableEmitter.onNext(1);
                log.info("emmit:1");
                observableEmitter.onNext(2);
                log.info("emmit:2");
                observableEmitter.onNext(3);
                log.info("emmit:3");
            }
        });

        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                log.info("onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                log.info("onNext:" + integer);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("onError");
            }

            @Override
            public void onComplete() {
                log.info("onComplete");
            }
        };

        observable1.subscribe(observer);
    }
}
