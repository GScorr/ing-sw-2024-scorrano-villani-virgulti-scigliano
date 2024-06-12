package it.polimi.ingsw.RMI_FINAL;

/**
 * Implements a simple countdown timer with start and pause functionality.
 *
 * This class provides functionality for creating a countdown timer that starts
 * at a specified time in seconds. It offers methods to start the countdown,
 * and retrieve the remaining time.
 *
 */
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

    /*
    da eliminare
    public void stop() {
        synchronized (lock) {
            running = false;
        }
    }

     */

    /*
    da eliminare
    public void resume() {
        synchronized (lock) {
            if (!running) {
                running = true;
                lock.notify();
            }
        }
    }

     */

    public static void main(String[] args) {
        Countdown countdown = new Countdown(10);
        countdown.start();
        while (countdown.getTimeRemained() > 0) {
            System.out.println("Mancano " + countdown.getTimeRemained());
        }
    }
}