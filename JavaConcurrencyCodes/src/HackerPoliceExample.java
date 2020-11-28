import java.util.ArrayList;
import java.util.Random;

class Vault {
    private int password;

    public Vault(int password) {
        this.password = password;
    }

    public boolean isCorrectGuess(int guess) {
        try {
            Thread.sleep(5);
        }catch (InterruptedException e) {

        }
        return this.password == guess;
    }
}

class Police extends Thread{
    @Override
    public void run() {
        for(int i = 1; i < 11; i++) {
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {

            }
            System.out.println(i);
        }
        System.out.println("Hackers! You have lost!");
        System.exit(0);

    }
}

abstract class Hacker extends Thread {
    protected Vault vault;

    Hacker(Vault vault) {
        this.vault = vault;
        this.setName(this.getClass().getSimpleName());
        this.setPriority(Thread.MAX_PRIORITY);
    }

    @Override
    public void start() {
        System.out.println("Staring hacker thread: " + this.getName());
        super.start();
    }
}

class AscendingHacker extends Hacker {
    AscendingHacker(Vault vault) {
        super(vault);
    }

    @Override
    public void run() {
        for(int i = 0; i < HackerPoliceExample.MAX_LIMIT; i++) {
            if(this.vault.isCorrectGuess(i)) {
                System.out.println(this.getName()+" hacker thread guessed the password!");
                System.exit(0);
            }
        }
    }
}

class DescendingHacker extends Hacker {
    DescendingHacker(Vault vault) {
        super(vault);
    }

    @Override
    public void run() {
        for(int i = HackerPoliceExample.MAX_LIMIT; i>-1; i--) {
            if(this.vault.isCorrectGuess(i)) {
                System.out.println(this.getName()+" hacker thread guessed the password!");
                System.exit(0);
            }
        }
    }
}

public class HackerPoliceExample {
    public static int MAX_LIMIT = 9999;

    public static void main(String[] args) {
        Random random = new Random();
        Vault vault = new Vault(random.nextInt(MAX_LIMIT));

        ArrayList<Thread> threads = new ArrayList<Thread>();
        threads.add(new AscendingHacker(vault));
        threads.add(new DescendingHacker(vault));
        threads.add(new Police());
        for(Thread t: threads) {
            t.start();
        }
    }
}
