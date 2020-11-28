import java.util.ArrayList;
import java.util.List;

public class MultiExecutor {

    ArrayList<Thread> threads;

    /*
     * @param tasks to executed concurrently
     */
    public MultiExecutor(List<Runnable> tasks) {
        if (tasks.size() >  0) {
            threads = new ArrayList<>();
            for(Runnable task: tasks) {
                threads.add(new Thread(task));
            }
        }
    }

    /**
     * Starts and executes all the tasks concurrently
     */
    public void executeAll() {
        for(Thread thread: threads) {
            thread.start();
        }
    }
}