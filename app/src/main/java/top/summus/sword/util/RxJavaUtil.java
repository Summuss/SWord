package top.summus.sword.util;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxJavaUtil {
//    public Observable

//    public void fun(){
//        Observable.just(1).compose(new ObservableTransformer<Integer, Object>() {
//        }
//
//        )
//    }

    public static SingleTransformer singleTransformer() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                ;
    }
}
