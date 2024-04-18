package xyz.mcutils.backend.service.metric.metrics;

import xyz.mcutils.backend.service.metric.impl.IntegerMetric;

public class TotalServerLookupsMetric extends IntegerMetric {

    public TotalServerLookupsMetric() {
        super("total_server_lookups");
    }
}
