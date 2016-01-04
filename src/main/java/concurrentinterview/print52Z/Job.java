package concurrentinterview.print52Z;

/**
 * Created by DELL on 2015/9/10.
 */
public class Job implements Runnable {
    private int number;
    private Object[] objects;
    private Print52Z print52Z;
    public Job(int number,Object[] objects,Print52Z print52Z){
        this.objects=objects;
        this.number=number;
        this.print52Z=print52Z;
    }
    @Override
    public void run() {
        print52Z.print52Z(number,objects);
    }
}
