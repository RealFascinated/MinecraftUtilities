package xyz.mcutils.backend.service;

import com.influxdb.client.WriteApiBlocking;
import com.influxdb.spring.influx.InfluxDB2AutoConfiguration;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.mcutils.backend.common.Timer;
import xyz.mcutils.backend.repository.MetricsRepository;
import xyz.mcutils.backend.service.metric.Metric;
import xyz.mcutils.backend.service.metric.metrics.RequestsPerRouteMetric;
import xyz.mcutils.backend.service.metric.metrics.TotalRequestsMetric;

import java.util.HashMap;
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

        // Register the metrics
        registerMetric(new TotalRequestsMetric());
        registerMetric(new RequestsPerRouteMetric());

        // Load the metrics from Redis
        loadMetrics();

        Timer.scheduleRepeating(() -> {
            saveMetrics();
            writeToInflux();
        }, saveInterval, saveInterval);
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
        for (Metric<?> metric : metricsRepository.findAll()) {
            metrics.put(metric.getClass(), metric);
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
        log.info("Saved {} metrics to Redis", metrics.size());
    }

    /**
     * Save a metric to Redis.
     *
     * @param metric the metric to save
     */
    private void saveMetric(Metric<?> metric) {
        metricsRepository.save(metric); // Save the metric to the repository
    }

    /**
     * Push all metrics to InfluxDB.
     */
    private void writeToInflux() {
        for (Metric<?> metric : metrics.values()) {
            influxWriteApi.writePoint(metric.toPoint());
        }
        log.info("Wrote {} metrics to Influx", metrics.size());
    }
}
