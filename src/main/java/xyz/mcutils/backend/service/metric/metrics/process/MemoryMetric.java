package xyz.mcutils.backend.service.metric.metrics.process;

import xyz.mcutils.backend.service.metric.impl.IntegerMetric;
import xyz.mcutils.backend.service.metric.impl.MapMetric;

public class MemoryMetric extends MapMetric<String, Long> {

    public MemoryMetric() {
        super("memory");
    }

    @Override
    public boolean isCollector() {
        return true;
    }

    @Override
    public void collect() {
        Runtime runtime = Runtime.getRuntime();

        this.getValue().put("total", runtime.maxMemory());
        this.getValue().put("allocated", runtime.totalMemory());
        this.getValue().put("used", runtime.totalMemory() - runtime.freeMemory());
    }
}
