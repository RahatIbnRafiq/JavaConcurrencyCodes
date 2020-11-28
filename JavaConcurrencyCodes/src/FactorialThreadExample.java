import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class FactorialThread extends Thread {
    private boolean isFinished;
    private BigInteger result;
    private long inputNumber;

    FactorialThread(long inputNumber) {
        this.inputNumber = inputNumber;
        this.result = BigInteger.ONE;
        this.isFinished = false;
    }

    @Override
    public void run() {
        this.factorial();
        this.isFinished = true;
    }

    private void factorial() {
        for(long i = 1; i < this.inputNumber; i++) {
            if(this.isInterrupted())
                return;
            this.result = this.result.multiply(new BigInteger(Long.toString(i)));
        }
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public BigInteger getResult() {
        return this.result;
    }
}

public class FactorialThreadExample {
    public static void main(String[] args) throws InterruptedException {
        List<Long> numbers = Arrays.asList(0L,123L, 2456L, 12345L, 10000000L);
        List<FactorialThread> threads = new ArrayList<>();
        for (Long number: numbers) {
            threads.add(new FactorialThread(number));
        }

        for(Thread thread: threads) {
            thread.start();
        }
        for(Thread thread: threads) {
            thread.join(3000);
        }

        for(int i = 0; i < numbers.size(); i++) {
            FactorialThread factorialThread = threads.get(i);
            if (factorialThread.isFinished()) {
                System.out.println("Factorial of " + numbers.get(i) +" is : " + factorialThread.getResult().toString());
            } else {
                System.out.println("Factorial of " + numbers.get(i) +" is taking too long. Let's interrupt it.");
                factorialThread.interrupt();
            }
        }
    }

}
