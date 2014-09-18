package me.creativei.metronome;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import static me.creativei.metronome.Constants.LOG_TAG;

public class BeatsTimer {
    private int delay;
    private Runnable onStartTask;
    private Runnable onStopTask;
    private Timer timer;
    private boolean isRunning;
    private long lastRun;

    public BeatsTimer(int delay, final Runnable onStartTask, Runnable onStopTask) {
        this.delay = delay;
        this.onStartTask = onStartTask;
        this.onStopTask = onStopTask;
    }

    public void start() {
        isRunning = true;
        timer = new Timer(true);
        timer.scheduleAtFixedRate(newTimerTask(), 0, delay);
    }

    public void update(int delay) {
        this.delay = delay;
        Log.d(LOG_TAG, "Current delay is " + delay);
        if (isRunning) {
            timer.cancel();
            long initialDelay = Math.max(0, delay - (System.currentTimeMillis() - lastRun));
            timer = new Timer(true);
            timer.scheduleAtFixedRate(newTimerTask(), (int) initialDelay, delay);
        }
    }

    public void stop() {
        timer.cancel();
        isRunning = false;
        onStopTask.run();
    }

    private TimerTask newTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                lastRun = System.currentTimeMillis();
                onStartTask.run();
            }
        };
    }
}