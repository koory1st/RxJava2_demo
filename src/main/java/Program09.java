/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

/**
 * 参考：
 * https://juejin.im/post/58807ef92f301e00697f6ad8
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

import java.io.BufferedReader;
import java.io.FileReader;

@Slf4j
public class Program09 {

    public static void main(String[] args) {
        Flowable<String> upstream = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> flowableEmitter) throws Exception {
                FileReader fileReader = new FileReader("/Users/koory1st/dev/00_demoProject/java/RxJava2_demo/src/main/resources/test.txt");
                BufferedReader br = new BufferedReader(fileReader);

                String str;

                while ((str = br.readLine()) != null && !flowableEmitter.isCancelled()) {
                    while (flowableEmitter.requested() == 0) {
                        if (flowableEmitter.isCancelled()) {
                            break;
                        }
                    }
                    flowableEmitter.onNext(str);
                }
                br.close();
                fileReader.close();
                flowableEmitter.onComplete();

//                flowableEmitter.onNext("1");
//                log.info("emit 1");
//                flowableEmitter.onNext("2");
//                log.info("emit 2");
//                flowableEmitter.onNext("3");
//                log.info("emit 3");
//                flowableEmitter.onNext("4");
//                log.info("emit 4");
//                flowableEmitter.onComplete();
            }
        }, BackpressureStrategy.ERROR);

        Subscriber<String> subscriber = new Subscriber<String>() {
            Subscription my;

            @Override
            public void onSubscribe(Subscription subscription) {
                my = subscription;
                my.request(1);
            }

            @Override
            public void onNext(String s) {
                log.info(s);
                my.request(1);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("", throwable);
            }

            @Override
            public void onComplete() {

            }
        };

        upstream
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .blockingSubscribe(subscriber);
    }
}
