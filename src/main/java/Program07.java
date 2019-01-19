/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

/**
 * 参考：
 * https://juejin.im/post/5857a5e48e450a006c752701
 * <p>
 */

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
public class Program07 {

    public static void main(String[] args) {
        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> flowableEmitter) throws Exception {
                flowableEmitter.onNext(1);
                log.info("emit 1");
                flowableEmitter.onNext(2);
                log.info("emit 2");
                flowableEmitter.onNext(3);
                log.info("emit 3");
                flowableEmitter.onNext(4);
                log.info("emit 4");
                flowableEmitter.onComplete();
            }
        }, BackpressureStrategy.ERROR);

        Subscriber<Integer> downstream = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                log.info("onSubscribe");
                subscription.request(3);
            }

            @Override
            public void onNext(Integer integer) {
                log.info("onNext:" + integer);
            }

            @Override
            public void onError(Throwable throwable) {
                log.warn("onNext:", throwable);
            }

            @Override
            public void onComplete() {
                log.info("onComplete");
            }
        };

        upstream.subscribeOn(Schedulers.io());
        upstream.observeOn(Schedulers.newThread());

        upstream.subscribe(downstream);
    }
}
