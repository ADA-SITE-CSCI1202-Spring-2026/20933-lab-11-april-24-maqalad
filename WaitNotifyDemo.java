public class WaitNotifyDemo {

    // Shared Resource
    static class SharedResource {

        private int value;
        private boolean bChanged = false;

        // get() method (consumer)
        public synchronized int get() {

            while (!bChanged) {
                try {
                    wait(); // wait until value is set
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            bChanged = false; // reset flag
            notify(); // notify producer

            return value;
        }

        // set() method (producer)
        public synchronized void set(int value) {

            while (bChanged) {
                try {
                    wait(); // wait until value is consumed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            this.value = value;
            bChanged = true; // mark as updated

            notify(); // notify consumer
        }
    }

    public static void main(String[] args) {

        SharedResource resource = new SharedResource();

        // Producer thread
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                resource.set(i);
                System.out.println("Produced: " + i);
            }
        });

        // Consumer thread
        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                int val = resource.get();
                System.out.println("Consumed: " + val);
            }
        });

        producer.start();
        consumer.start();
    }
}