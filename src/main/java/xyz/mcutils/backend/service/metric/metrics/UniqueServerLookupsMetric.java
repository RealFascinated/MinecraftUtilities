package xyz.mcutils.backend.service.metric.metrics;

import xyz.mcutils.backend.service.MetricService;
import xyz.mcutils.backend.service.metric.impl.IntegerMetric;

import java.util.ArrayList;
import java.util.List;

public class UniqueServerLookupsMetric extends IntegerMetric {
    private List<String> uniqueLookups = new ArrayList<>();

    public UniqueServerLookupsMetric() {
        super("unique_server_lookups");
    }

    @Override
    public boolean isCollector() {
        return true;
    }

    /**
     * Adds a lookup to the list of unique lookups.
     *
     * @param lookup the query that was used to look up a player
     */
    public void addLookup(String lookup) {
        lookup = lookup.toLowerCase();
        if (!uniqueLookups.contains(lookup)) {
            uniqueLookups.add(lookup);
        }
    }

    @Override
    public void collect(MetricService metricService) {
        setValue(uniqueLookups.size());
    }
}
