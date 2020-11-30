import java.util.ArrayList;
import java.util.Random;

class Item {
    private int price;
    Item(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

class Inventory {
    private java.util.concurrent.locks.ReentrantReadWriteLock lock = new java.util.concurrent.locks.ReentrantReadWriteLock();
    private ArrayList<Item> items;
    private static int NUM_ITEMS = 10000000;
    private Random random = new Random();

    Inventory() {
        this.items = new ArrayList<>();
        for(int i = 0; i < Inventory.NUM_ITEMS; i++) {
            this.items.add(new Item(random.nextInt(10000)));
        }
    }

    public java.util.concurrent.locks.ReentrantReadWriteLock getLock() {
        return lock;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}

class InventoryReadThread extends Thread {
    private Inventory inventory;
    private Random random;
    InventoryReadThread(Inventory inventory) {
        this.inventory = inventory;
        this.random = new Random();
    }
    @Override
    public void run() {
        this.inventory.getLock().readLock().lock();
        try {
            int randomPrice = random.nextInt(10000);
            int totalItems = 0;
            for(int i=0; i< this.inventory.getItems().size(); i++) {
                if (this.inventory.getItems().get(i).getPrice() == randomPrice) {
                    totalItems += 1;
                }
            }
        }finally {
            this.inventory.getLock().readLock().unlock();
        }
    }
}

class InventoryWriteThread extends Thread {
    private Inventory inventory;
    private Random random;
    InventoryWriteThread(Inventory inventory) {
        this.inventory = inventory;
        this.random = new Random();
    }
    @Override
    public void run() {
        this.inventory.getLock().writeLock().lock();
        try {
            int randomNumItems = random.nextInt(100);
            int totalItems = 0;
            for(int i=0; i< randomNumItems; i++) {
                this.inventory.getItems().add(new Item(this.random.nextInt(10000)));
            }
        }finally {
            this.inventory.getLock().writeLock().unlock();
        }
    }
}



public class ReentrantReadWriteLock {
    public static void main(String[] args) throws InterruptedException {
        Inventory inventory = new Inventory();
        int NUM_READERS = 10;
        int NUM_WRITERS = 2;
        ArrayList<InventoryReadThread> readers = new ArrayList<InventoryReadThread>();
        ArrayList<InventoryWriteThread> writers = new ArrayList<InventoryWriteThread>();

        for(int i=0; i< NUM_READERS; i++) {
            InventoryReadThread reader = new InventoryReadThread(inventory);
            reader.setDaemon(true);
            readers.add(reader);
        }

        for(int i=0; i< NUM_WRITERS; i++) {
            InventoryWriteThread writer = new InventoryWriteThread(inventory);
            writer.setDaemon(true);
            writers.add(writer);
        }

        for (Thread t: writers) {
            t.start();
        }

        long startReadingTime = System.currentTimeMillis();

        for (Thread t: readers) {
            t.start();
        }


        for(Thread t: readers) {
            t.join();
        }
        long endReadingTime = System.currentTimeMillis();

        System.out.println(endReadingTime - startReadingTime);

    }
}
