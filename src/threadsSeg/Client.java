package threadsSeg;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.List;

/**
* Created by DELL on 2015/7/30.
        */
public class Client {
    public static void main(String args[]){
        long begin=System.currentTimeMillis();
        PageReader pageReader=new PageReader("",4000);
        PageWriter pageWriter=new PageWriter("");
        Thread[] threads=new Thread[3];
        for (int i=0;i<3;i++){
            Thread thread = new Thread(new SegTask(pageReader,pageWriter));
            threads[i]=thread;
            thread.start();
        }
        while(true){
            if(!threads[0].isAlive()&&!threads[0].isAlive()&&!threads[0].isAlive()){
                long end=System.currentTimeMillis();
                System.out.println((end-begin)/1000);
                break;
            }
        }
    }
}
