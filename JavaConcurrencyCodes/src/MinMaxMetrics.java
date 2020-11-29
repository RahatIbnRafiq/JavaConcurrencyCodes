public class MinMaxMetrics {

    private volatile long minValue;
    private volatile long maxValue;

    public MinMaxMetrics() {
        this.maxValue = Long.MIN_VALUE;
        this.minValue = Long.MAX_VALUE;
    }

    public void addSample(long newSample) {
        synchronized (this) {
            if (newSample > this.maxValue) {
                this.maxValue = newSample;
            }
            else if (newSample < this.minValue) {
                this.minValue = newSample;
            }
        }

    }

    public long getMin() {
        return this.minValue;
    }

    public long getMax() {
        return this.maxValue;
    }
}
