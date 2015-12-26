package concurrentinterview.Semphore;


import java.util.concurrent.Semaphore;

public class Car implements Runnable {

    private final Semaphore parkingSlot;

    private int carNo;

    /**
     * @param parkingSlot
     */
    public Car(Semaphore parkingSlot, int carNo) {
        this.parkingSlot = parkingSlot;
        this.carNo = carNo;
    }

    public void run() {

        try {
            parkingSlot.acquire();
            parking();
            sleep(300);
            parkingSlot.release();
            leaving();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void parking() {
        System.out.println(String.format("%d号车泊车", carNo));
    }

    private void leaving() {
        System.out.println(String.format("%d号车离开车位", carNo));
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

}