package concurrentinterview.printABCD;

/**
 * Created by DELL on 2015/9/9.
 */
public class Job implements Runnable {
    private PrintABCD printABCD;
    private char chara;
    private int i;

    public Job(PrintABCD printABCD, char chara, int i) {
        this.printABCD = printABCD;
        this.chara = chara;
        this.i = i;
    }


    @Override
    public void run() {
        printABCD.printABC(chara, i);
    }
}
