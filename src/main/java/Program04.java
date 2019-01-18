/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

/**
 * 参考：
 * https://www.jianshu.com/p/bb58571cdb64
 * <p>
 * 使用zip合并两个Observable
 */

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Program04 {
    public static void main(String[] args) {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> observableEmitter) throws Exception {
                observableEmitter.onNext(1);
                log.info("emit 1");
                Thread.sleep(1000);
                observableEmitter.onNext(2);
                log.info("emit 2");
                Thread.sleep(1000);
                observableEmitter.onNext(3);
                log.info("emit 3");
                Thread.sleep(1000);
                observableEmitter.onNext(4);
                log.info("emit 4");
                Thread.sleep(1000);
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                observableEmitter.onNext("A");
                log.info("emit A");
                Thread.sleep(1000);
                observableEmitter.onNext("B");
                log.info("emit B");
                Thread.sleep(1000);
                observableEmitter.onNext("C");
                log.info("emit C");
                Thread.sleep(1000);
                observableEmitter.onNext("D");
                log.info("emit D");
                Thread.sleep(1000);
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).blockingSubscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                log.info("onSubscribe");
            }

            @Override
            public void onNext(String s) {
                log.info("onNext:" + s);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("onError");
            }

            @Override
            public void onComplete() {
                log.info("onComplete");
            }
        });

    }
}
