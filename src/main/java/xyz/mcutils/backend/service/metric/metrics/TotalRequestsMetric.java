package xyz.mcutils.backend.service.metric.metrics;

import xyz.mcutils.backend.service.metric.impl.IntegerMetric;

public class TotalRequestsMetric extends IntegerMetric {

    public TotalRequestsMetric() {
        super("total_requests");
    }
}
