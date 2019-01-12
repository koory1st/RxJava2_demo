/**
 * 参考：
 * https://www.jianshu.com/p/8818b98c44e2
 * <p>
 * 使用blockingSubscribe 来回到main线程
 */

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Program02 {
    public static void main(String[] args) {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> observableEmitter) throws Exception {
                observableEmitter.onNext(1);
                log.info("emmit:1");
                log.info("Observable1 thread is :" + Thread.currentThread().getName());
                observableEmitter.onComplete();
            }
        });
        Observable<Integer> observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> observableEmitter) throws Exception {
                observableEmitter.onNext(1);
                log.info("emmit:1");
                log.info("Observable2 thread is :" + Thread.currentThread().getName());
                observableEmitter.onNext(2);
                log.info("emmit:2");
                observableEmitter.onComplete();
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                log.info("Observer thread is :" + Thread.currentThread().getName());
            }
        };

//        observable1
//                .subscribeOn(Schedulers.newThread())
//                .blockingSubscribe(consumer);

        observable2
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .blockingSubscribe(consumer);
    }
}
