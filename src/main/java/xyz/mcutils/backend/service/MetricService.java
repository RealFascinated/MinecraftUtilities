package xyz.mcutils.backend.service;

import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.write.Point;
import com.influxdb.spring.influx.InfluxDB2AutoConfiguration;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.mcutils.backend.common.EnvironmentUtils;
import xyz.mcutils.backend.common.Timer;
import xyz.mcutils.backend.repository.mongo.MetricsRepository;
import xyz.mcutils.backend.service.metric.Metric;
import xyz.mcutils.backend.service.metric.metrics.*;
import xyz.mcutils.backend.service.metric.metrics.process.CpuUsageMetric;
import xyz.mcutils.backend.service.metric.metrics.process.MemoryMetric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service @Log4j2
public class MetricService {
    /**
     * The metrics that are registered.
     */
    private final Map<Class<?>, Metric<?>> metrics = new HashMap<>();

    /**
     * The interval in which the metrics are saved.
     */
    private final long saveInterval = TimeUnit.SECONDS.toMillis(15L);

    private final WriteApiBlocking influxWriteApi;
    private final MetricsRepository metricsRepository;

    @Autowired
    public MetricService(InfluxDB2AutoConfiguration influxAutoConfiguration, MetricsRepository metricsRepository) {
        this.influxWriteApi = influxAutoConfiguration.influxDBClient().getWriteApiBlocking();
        this.metricsRepository = metricsRepository;

        Map<Metric<?>, Boolean> collectorEnabled = new HashMap<>();

        // Register the metrics
        registerMetric(new TotalRequestsMetric());
        registerMetric(new RequestsPerRouteMetric());
        registerMetric(new MemoryMetric());
        registerMetric(new CpuUsageMetric());
        registerMetric(new ConnectedSocketsMetric());
        registerMetric(new UniquePlayerLookupsMetric());
        registerMetric(new UniqueServerLookupsMetric());

        // please god forgive my sins; this is the worst code I've ever written
        for (Metric<?> metric : metrics.values()) {
            collectorEnabled.put(metric, metric.isCollector());
        }

        if (EnvironmentUtils.isProduction()) {
            // Load the metrics from Redis
            loadMetrics();

            for (Map.Entry<Metric<?>, Boolean> entry : collectorEnabled.entrySet()) {
                entry.getKey().setCollector(entry.getValue());
            }

            Timer.scheduleRepeating(() -> {
                saveMetrics();
                writeToInflux();
            }, saveInterval, saveInterval);
        }
    }

    /**
     * Register a metric.
     *
     * @param metric the metric to register
     */
    public void registerMetric(Metric<?> metric) {
        if (metrics.containsKey(metric.getClass())) {
            throw new IllegalArgumentException("A metric with the class " + metric.getClass().getName() + " is already registered");
        }
        metrics.put(metric.getClass(), metric);
    }

    /**
     * Get a metric by its class.
     *
     * @param clazz the class of the metric
     * @return the metric
     * @throws IllegalArgumentException if there is no metric with the class registered
     */
    public Metric<?> getMetric(Class<?> clazz) throws IllegalArgumentException {
        if (!metrics.containsKey(clazz)) {
            throw new IllegalArgumentException("No metric with the class " + clazz.getName() + " is registered");
        }
        return metrics.get(clazz);
    }

    /**
     * Load all metrics from Redis.
     */
    public void loadMetrics() {
        log.info("Loading metrics");
        for (Metric<?> metric : metrics.values()) {
            metricsRepository.findById(metric.getId()).ifPresent(loaded -> metrics.put(loaded.getClass(), loaded));
        }
        log.info("Loaded {} metrics", metrics.size());
    }

    /**
     * Save all metrics to Redis.
     */
    private void saveMetrics() {
        for (Metric<?> metric : metrics.values()) {
            saveMetric(metric);
        }
        log.info("Saved {} metrics to MongoDB", metrics.size());
    }

    /**
     * Save a metric to Redis.
     *
     * @param metric the metric to save
     */
    private void saveMetric(Metric<?> metric) {
        try {
            metricsRepository.save(metric); // Save the metric to the repository
        } catch (Exception e) {
            log.error("Failed to save metric to MongoDB", e);
        }
    }

    /**
     * Push all metrics to InfluxDB.
     */
    private void writeToInflux() {
        try {
            List<Point> points = new ArrayList<>();
            for (Metric<?> metric : metrics.values()) {
                if (metric.isCollector()) {
                    metric.collect();
                }
                Point point = metric.toPoint();
                if (point != null) {
                    points.add(point);
                }
            }
            influxWriteApi.writePoints(points);
            log.info("Wrote {} metrics to Influx", metrics.size());
        } catch (Exception e) {
            log.error("Failed to write metrics to Influx", e);
        }
    }
}
