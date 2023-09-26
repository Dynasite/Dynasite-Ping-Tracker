package org.pingtracker;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.*;

public class PingRecord {

    public static final int DEFAULT_COUNT = 100;

    public static final int DEFAULT_TIMEOUT = 1500;

    private final String serverName;

    private final String serverIP;

    private final int totalResultCount;

    private final long[] results;

    private int resultsCount = 0;

    public PingRecord(String serverName, String serverIP) {
        this(serverName, serverIP, DEFAULT_COUNT);
    }

    public PingRecord(String serverName, String serverIP, int totalResultCount) {
        if(totalResultCount < 10) {
            throw new IllegalArgumentException("totalResultCount < 10");
        }

        this.serverName = Objects.requireNonNull(serverName);
        this.serverIP = Objects.requireNonNull(serverIP);
        this.totalResultCount = totalResultCount;

        results = new long[totalResultCount];
    }

    public synchronized void ping(int count) {

    }

    public synchronized int getAverage() { return -1; }

    public synchronized int getMax() { return -1; }

    public synchronized int getMin() { return -1; }

    public synchronized float getLoss() { return -1F; }

    public static long[] pingIP(String ip, int pings, int timeout) {
        long[] returnPings = new long[pings];

        for(int i = 0; i < pings; i++) {
            long start = System.currentTimeMillis();
            try {
                boolean isPinged = InetAddress.getByName(ip).isReachable(DEFAULT_TIMEOUT); // 2 seconds
            } catch (SocketTimeoutException e) {
                returnPings[i] = -1;
                continue;
            } catch (IOException e) {
                returnPings[i] = -2;
                continue;
            }
            long stop = System.currentTimeMillis();
            returnPings[i] = (stop - start);
        }

        return returnPings;
    }
}
