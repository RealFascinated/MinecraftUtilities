package xyz.mcutils.backend.service.metric.impl;

import com.influxdb.client.write.Point;
import xyz.mcutils.backend.service.metric.Metric;

public class DoubleMetric extends Metric<Double> {

    public DoubleMetric(String id) {
        super(id, 0D, false);
    }

    /**
     * Increment the value of this metric.
     *
     * @param amount the amount to increment by
     */
    public void increment(double amount) {
        setValue(getValue() + amount);
    }

    /**
     * Increment the value of this metric by 1.
     */
    public void increment() {
        increment(1);
    }

    /**
     * Decrement the value of this metric.
     *
     * @param amount the amount to decrement by
     */
    public void decrement(double amount) {
        setValue(getValue() - amount);
    }

    /**
     * Decrement the value of this metric by 1.
     */
    public void decrement() {
        decrement(1D);
    }

    @Override
    public Point toPoint() {
        return Point.measurement(getId())
                .addField("value", getValue());
    }
}
