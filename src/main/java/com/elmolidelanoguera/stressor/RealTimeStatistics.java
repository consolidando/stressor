/*
 * Copyright (c) 2024 joanribalta@elmolidelanoguera.com
 * License: CC BY-NC-ND 4.0 (https://creativecommons.org/licenses/by-nc-nd/4.0/)
 * Blog Consolidando: https://diy.elmolidelanoguera.com/
 */
package com.elmolidelanoguera.stressor;


import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

public class RealTimeStatistics
{
    private final AtomicLong minimum = new AtomicLong(1000_000_000L);
    private final AtomicLong maximum = new AtomicLong(0L);
    private final DoubleAdder sum = new DoubleAdder(); // Sum of all values
    private final DoubleAdder sumOfSquares = new DoubleAdder(); // Sum of squares of all values
    private final LongAdder count = new LongAdder(); // Count of values        

    // Method to register a value during a request concurrently
    public void addValue(long value)
    {
        sum.add(value);
        sumOfSquares.add(value * value);
        count.increment();

        minimum.updateAndGet(current -> Math.min(current, value));
        maximum.updateAndGet(current -> Math.max(current, value));                
    }
    
    public long getMaximum()
    {
        return(maximum.get());
    }
    
    public long getMinimum()
    {
        return(minimum.get());
    }
       
    public long getCount()
    {
        return(count.sum());
    }

    // Method to calculate the mean
    public double calculateMean()
    {
        long totalCount = count.sum();
        if (totalCount == 0)
        {
            return 0;
        }

        return sum.sum() / totalCount;
    }

    // Method to calculate the standard deviation
    public double calculateStandardDeviation()
    {
        long totalCount = count.sum();
        if (totalCount == 0)
        {
            return 0;
        }

        double mean = sum.sum() / totalCount;
        double variance = sumOfSquares.sum() / totalCount - mean * mean;
        return Math.sqrt(variance);
    }
}
