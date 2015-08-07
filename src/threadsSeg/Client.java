package threadsSeg;

import java.io.IOException;

/**
* Created by DELL on 2015/7/30.
        */
public class Client {
    public static void main(String args[]) throws Exception{
        long begin=System.currentTimeMillis();
        
        String[] stops=null;
        if(args.length==3){
        	stops=IOUtil.readLines(args[2]);
        }
        PageReader pageReader=new PageReader(args[0],4000);
        PageWriter pageWriter=new PageWriter(args[1]);
        Thread[] threads=new Thread[3];
        for (int i=0;i<3;i++){
            Thread thread = new Thread(new SegTask(pageReader,pageWriter,stops));
            threads[i]=thread;
            thread.start();
        }
        while(true){
            if(!threads[0].isAlive()&&!threads[1].isAlive()&&!threads[2].isAlive()){
            	try {
        			if (pageWriter != null && pageWriter.getOut() != null) {
        				pageWriter.getOut().flush();
        				pageWriter.getOut().close();
        			}
        			if(pageReader.getIn()!=null&&pageReader!=null){
        				pageReader.getIn().close();
                	}
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
                long end=System.currentTimeMillis();
                System.out.println((end-begin)/1000);
                break;
            }
        }
    }
}
