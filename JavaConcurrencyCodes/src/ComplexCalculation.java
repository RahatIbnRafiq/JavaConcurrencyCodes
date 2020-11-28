import com.sun.deploy.util.ArrayUtil;

import java.math.BigInteger;
import java.util.ArrayList;

public class ComplexCalculation {
    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) {
        BigInteger result;
        PowerCalculatingThread t1 = new PowerCalculatingThread(base1, power1);
        PowerCalculatingThread t2 = new PowerCalculatingThread(base2, power2);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result = t1.getResult().add(t2.getResult());
        return result;
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            for(BigInteger i = BigInteger.ONE; i.compareTo(power)  != 0 ; i = i.add(BigInteger.ONE)) {
                this.result = this.result.multiply(this.base);
            }
        }

        public BigInteger getResult() { return result; }
    }

    public static void main(String[] args) {
        ComplexCalculation complexCalculation = new ComplexCalculation();
        BigInteger base1 = new BigInteger("2");
        BigInteger base2 = new BigInteger("3");
        BigInteger power1 = new BigInteger("100");
        BigInteger power2 = new BigInteger("200");
        System.out.println("The result is: " + complexCalculation.calculateResult(base1, power1, base2, power2).toString());
    }
}