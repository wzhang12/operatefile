package concurrentinterview.guava.futurecallback;

import com.google.common.util.concurrent.FutureCallback;
import concurrentinterview.CountLatchDownTest.CountDownLatchTest;

import java.util.concurrent.CountDownLatch;

/**
 *
 */
public class FutureCallbackImpl implements FutureCallback<String> {

    private StringBuilder builder = new StringBuilder();
    CountDownLatch countDownLatch;
    public FutureCallbackImpl(CountDownLatch countDownLatch){
        this.countDownLatch=countDownLatch;

    }
    @Override
    public void onSuccess(String result) {
        //System.out.println(result);
        builder.append(result).append("successfully");
        countDownLatch.countDown();
    }

    @Override
    public void onFailure(Throwable t) {
        System.out.println("failure");
        builder.append(t.toString());
    }

    public String getCallbackResult() {
        return builder.toString();
    }
}