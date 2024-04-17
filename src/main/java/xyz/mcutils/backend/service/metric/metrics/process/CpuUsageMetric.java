package xyz.mcutils.backend.service.metric.metrics.process;

import com.sun.management.OperatingSystemMXBean;
import xyz.mcutils.backend.service.metric.impl.DoubleMetric;

import java.lang.management.ManagementFactory;

public class CpuUsageMetric extends DoubleMetric {
    /**
     * The OperatingSystemMXBean instance to get the CPU load from.
     */
    private static final OperatingSystemMXBean OS_BEAN = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    public CpuUsageMetric() {
        super("cpu_usage");
    }

    @Override
    public void collect() {
        this.setValue(OS_BEAN.getProcessCpuLoad() * 100);
    }
}
