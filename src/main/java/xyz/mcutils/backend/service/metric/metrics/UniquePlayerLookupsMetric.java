package xyz.mcutils.backend.service.metric.metrics;

import xyz.mcutils.backend.service.metric.impl.IntegerMetric;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UniquePlayerLookupsMetric extends IntegerMetric {
    private List<String> uniqueLookups = new ArrayList<>();

    public UniquePlayerLookupsMetric() {
        super("unique_player_lookups");
    }

    @Override
    public boolean isCollector() {
        return true;
    }

    /**
     * Adds a lookup to the list of unique lookups.
     *
     * @param uuid the query that was used to look up a player
     */
    public void addLookup(UUID uuid) {
        if (!uniqueLookups.contains(uuid.toString())) {
            uniqueLookups.add(uuid.toString());
        }
    }

    @Override
    public void collect() {
        setValue(uniqueLookups.size());
    }
}
