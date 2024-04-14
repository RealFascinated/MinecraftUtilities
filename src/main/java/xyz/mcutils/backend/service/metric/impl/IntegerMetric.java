package xyz.mcutils.backend.service.metric.impl;

import com.influxdb.client.write.Point;
import xyz.mcutils.backend.service.metric.Metric;

public class IntegerMetric extends Metric<Integer> {

    public IntegerMetric(String id) {
        super(id, 0);
    }

    /**
     * Increment the value of this metric.
     *
     * @param amount the amount to increment by
     */
    public void increment(int amount) {
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
    public void decrement(int amount) {
        setValue(getValue() - amount);
    }

    /**
     * Decrement the value of this metric by 1.
     */
    public void decrement() {
        decrement(1);
    }

    @Override
    public Point toPoint() {
        return Point.measurement(getId())
                .addField("value", getValue());
    }
}
