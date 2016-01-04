package concurrentinterview.printABC;

/**
 * Created by DELL on 2015/9/9.
 */
public class Job implements  Runnable{
    private PrintABC printABC;
    private char chara;
    private int i;
    public Job(PrintABC printABC,char chara,int i){
        this.printABC = printABC;
        this.chara=chara;
        this.i=i;
    }


    @Override
    public void run() {
        printABC.printABC(chara,i);
    }
}
