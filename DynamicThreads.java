public class DynamicThreads {

    // Task class (implements Runnable)
    static class MathTask implements Runnable {

        @Override
        public void run() {
            long sum = 0;

            for (int i = 0; i < 10_000_000; i++) {
                sum += i * 3 + i;
            }

            // Optional: print to show thread finished
            // System.out.println("Thread finished with sum = " + sum);
        }
    }

    public static void main(String[] args) {

        // 1. Get number of cores
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Available cores: " + cores);

        // =========================
        // Case 1: Single thread
        // =========================
        long start1 = System.currentTimeMillis();

        Thread single = new Thread(new MathTask());
        single.start();

        try {
            single.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end1 = System.currentTimeMillis();
        System.out.println("Time with 1 thread: " + (end1 - start1) + " ms");

        // =========================
        // Case 2: Max threads
        // =========================
        Thread[] threads = new Thread[cores];

        long start2 = System.currentTimeMillis();

        // Create and start threads
        for (int i = 0; i < cores; i++) {
            threads[i] = new Thread(new MathTask());
            threads[i].start();
        }

        // Join all threads
        for (int i = 0; i < cores; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end2 = System.currentTimeMillis();
        System.out.println("Time with " + cores + " threads: " + (end2 - start2) + " ms");
    }
}