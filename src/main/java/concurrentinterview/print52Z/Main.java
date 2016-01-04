package concurrentinterview.print52Z;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *using two threads one thread print 12, the other thread print A
 * 12A34B56C78D910E1112F1314G1516H1718I1920J2122K2324L2526M2728N2930O3132P3334Q3536R3738S3940T4142U4344V4546W4748X4950Y5152Z
 */
public class Main {
    public static void main(String[] args){
        ExecutorService executorService= Executors.newFixedThreadPool(2);
        String[] charc=new String[26];
        char a='A';
        for (int i = 0; i <charc.length ; i++) {
            charc[i]=""+(char)(a+i);
        }
        Integer[] number = new Integer[52];
        for (int j = 0; j <52 ; j++) {
            number[j]=j+1;

        }
        Print52Z print52Z=new Print52Z();
        executorService.execute(new Job(2,number,print52Z));
        executorService.execute(new Job(1,charc,print52Z));
        executorService.shutdown();
    }
}
