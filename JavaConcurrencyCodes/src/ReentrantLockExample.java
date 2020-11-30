import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class PriceContainer {
    private Lock lock = new ReentrantLock();
    private double appleStockPrice;
    private double teslaStockPrice;
    private double facebookStockPrice;
    private double microsoftStockPrice;
    private double googleStockPrice;

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public double getAppleStockPrice() {
        return appleStockPrice;
    }

    public void setAppleStockPrice(double appleStockPrice) {
        this.appleStockPrice = appleStockPrice;
    }

    public double getTeslaStockPrice() {
        return teslaStockPrice;
    }

    public void setTeslaStockPrice(double teslaStockPrice) {
        this.teslaStockPrice = teslaStockPrice;
    }

    public double getFacebookStockPrice() {
        return facebookStockPrice;
    }

    public void setFacebookStockPrice(double facebookStockPrice) {
        this.facebookStockPrice = facebookStockPrice;
    }

    public double getMicrosoftStockPrice() {
        return microsoftStockPrice;
    }

    public void setMicrosoftStockPrice(double microsoftStockPrice) {
        this.microsoftStockPrice = microsoftStockPrice;
    }

    public double getGoogleStockPrice() {
        return googleStockPrice;
    }

    public void setGoogleStockPrice(double googleStockPrice) {
        this.googleStockPrice = googleStockPrice;
    }
}


class PriceViewer extends Thread {
    PriceContainer priceContainer;
    PriceViewer(PriceContainer priceContainer) {
        this.priceContainer = priceContainer;
    }

    @Override
    public void run() {
        while(true) {
            if(this.priceContainer.getLock().tryLock()) {
                try {
                    System.out.println("Apple Stock Price: " + this.priceContainer.getAppleStockPrice());
                    System.out.println("Google Stock Price: " + this.priceContainer.getGoogleStockPrice());
                    System.out.println("Tesla Stock Price: " + this.priceContainer.getTeslaStockPrice());
                    System.out.println("Microsoft Stock Price: " + this.priceContainer.getMicrosoftStockPrice());
                    System.out.println("Facebook Stock Price: " + this.priceContainer.getFacebookStockPrice());
                    System.out.println("__________________________________________________________________");
                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {

                    }
                } finally {
                    this.priceContainer.getLock().unlock();
                }

            }
        }


    }
}



class PriceUpdater extends Thread {
    PriceContainer priceContainer;
    private Random random = new Random();

    PriceUpdater(PriceContainer priceContainer) {
        this.priceContainer = priceContainer;
    }

    @Override
    public void run() {
        while(true) {
            this.priceContainer.getLock().lock();
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {

            }
            try {
                priceContainer.setAppleStockPrice(random.nextDouble());
                priceContainer.setFacebookStockPrice(random.nextDouble());
                priceContainer.setGoogleStockPrice(random.nextDouble());
                priceContainer.setMicrosoftStockPrice(random.nextDouble());
                priceContainer.setTeslaStockPrice(random.nextDouble());
            } finally {
                this.priceContainer.getLock().unlock();
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {

            }
        }
    }
}
public class ReentrantLockExample {
    public static void main(String[] args) {
        PriceContainer priceContainer = new PriceContainer();
        PriceUpdater priceUpdater = new PriceUpdater(priceContainer);
        PriceViewer priceViewer = new PriceViewer(priceContainer);
        priceUpdater.start();
        priceViewer.start();
    }


}
