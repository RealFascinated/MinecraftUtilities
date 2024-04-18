package xyz.mcutils.backend.service.metric.metrics;

import xyz.mcutils.backend.service.metric.impl.IntegerMetric;

public class TotalPlayerLookupsMetric extends IntegerMetric {

    public TotalPlayerLookupsMetric() {
        super("total_player_lookups");
    }
}
