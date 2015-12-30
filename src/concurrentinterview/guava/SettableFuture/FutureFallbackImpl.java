package concurrentinterview.guava.SettableFuture;

import com.google.common.util.concurrent.FutureFallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import java.io.FileNotFoundException;

/**
 * 当线程池中一个任务发生异常时，会调用这里，可以重新新建一个ListenableFuture的实现类比如SettableFuture
 */
public class FutureFallbackImpl implements FutureFallback<String> {
    @Override
    public ListenableFuture<String> create(Throwable t) throws Exception {//此处泛型规定的是返回值类型
        if (t instanceof IllegalArgumentException) {

            SettableFuture<String> settableFuture = SettableFuture.create();
            settableFuture.set("Not Found");//这里规定了返回值类型
            return  settableFuture;
        }

        throw new Exception(t);
    }
}