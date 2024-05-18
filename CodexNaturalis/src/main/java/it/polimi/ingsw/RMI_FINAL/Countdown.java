package it.polimi.ingsw.RMI_FINAL;

public class Countdown {
    private int time;
    private Thread countdownThread;
    private boolean running;
    private final Object lock = new Object();

    public Countdown(int timeInSeconds) {
        this.time = timeInSeconds;
    }

    public void start() {
        if (countdownThread == null || !countdownThread.isAlive()) {
            running = true;
            countdownThread = new Thread(new Runnable() {
                public void run() {
                    while (time > 0) {
                        synchronized (lock) {
                            if (!running) {
                                try {
                                    lock.wait();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        if (running) {
                            time--;
                            System.out.println("Time left: " + time + " seconds");
                        }
                    }
                }
            });
            countdownThread.start();
        }
    }

    public int getTimeRemained() {
        return time;
    }

    public void stop() {
        synchronized (lock) {
            running = false;
        }
    }

    public void resume() {
        synchronized (lock) {
            if (!running) {
                running = true;
                lock.notify();
            }
        }
    }

    public static void main(String[] args) {
        Countdown countdown = new Countdown(10);
        countdown.start();

        try {
            Thread.sleep(3000);
            countdown.stop();
            System.out.println("Countdown stopped");

            Thread.sleep(2000);
            countdown.resume();
            System.out.println("Countdown resumed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}