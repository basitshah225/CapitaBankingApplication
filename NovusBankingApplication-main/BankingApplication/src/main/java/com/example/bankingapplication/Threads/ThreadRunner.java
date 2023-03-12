package com.example.bankingapplication.Threads;


/**
 * Class to control the different threads
 * Controls the ISA updater thread
 * Only 1 instance can exist
 */
public final class ThreadRunner {

    /**
     * Static volatile instance for a thread safe version of thread runner
     */
    private static volatile ThreadRunner INSTANCE;

    /**
     * Mutual exclusion lock for getting the instance of thread runner
     */
    private static final Object mutex = new Object();

    /**
     * The updater thread for the ISA APY updater
     */
    private final ISAUpdaterThread isaUpdaterThread;

    /**
     * The business thread for charging
     */
    private final BusinessThread businessThread;

    /**
     * The business thread for charging
     */
    private final ISAResetterThread isaResetterThread;

    /**
     * Private constructor, creates an instance of the ISA updater thread
     * Starts the thread
     */
    private ThreadRunner(){
        isaUpdaterThread = new ISAUpdaterThread();
        businessThread = new BusinessThread();
        isaResetterThread = new ISAResetterThread();

        isaUpdaterThread.start();
        businessThread.start();
        isaResetterThread.start();
    }

    /**
     * Thread runner expressed as a singleton, as only one should exist
     * Sets the result to instance
     * Checks to see if its null
     * If it is null it needs initialising
     * Gets the lock for the object so no other thread could instantiate it
     * Checks again for the instance when in the lock, to make sure its not been created
     * If its null create call private constructor
     * @return Instance of the class
     */
    public static ThreadRunner getInstance() {
        ThreadRunner result = INSTANCE;
        if (result == null) {
            synchronized (mutex) {
                result = INSTANCE;
                if (result == null)
                    INSTANCE = result = new ThreadRunner();
            }
        }
        return result;
    }

    /**
     * Method to cancel the threads, calls the ISA and business thread cancel
     */
    public void cancelThread(){
        isaUpdaterThread.cancel();
        businessThread.cancel();
        isaResetterThread.cancel();
    }



}
