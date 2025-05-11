package it.polimi.ingsw.view.text;

public class TimerDisplay {
    private final int durationSeconds;
    private long startTime;
    private int expirationCount = 0;
    private Thread timerThread;

    public TimerDisplay(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        System.out.println("Timer started! Duration: " + durationSeconds + " seconds.");

        timerThread = new Thread(() -> {
            try {
                Thread.sleep(durationSeconds * 1000L);
            } catch (InterruptedException e) {
                System.out.println("Timer interrupted.");
                return;
            }

            expirationCount++;
            System.out.println("⏰ Timer ended for the " + ordinal(expirationCount) + " time.");
        });

        timerThread.setDaemon(true);
        timerThread.start();
    }

    public long getTimeLeft() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        long remaining = durationSeconds - elapsed;
        return Math.max(0, remaining);
    }

    public int getExpirationCount() {
        return expirationCount;
    }

    private String ordinal(int number) {
        if (number % 100 >= 11 && number % 100 <= 13) {
            return number + "th";
        }
        return switch (number % 10) {
            case 1 -> number + "st";
            case 2 -> number + "nd";
            case 3 -> number + "rd";
            default -> number + "th";
        };
    }
}
