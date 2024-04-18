package xyz.mcutils.backend.service.metric;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.influxdb.client.write.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import xyz.mcutils.backend.service.MetricService;

@AllArgsConstructor
@Getter @Setter @ToString
@Document("metrics")
public abstract class Metric<T> {
    /**
     * The id of the metric.
     */
    @Id private String id;

    /**
     * The value of the metric.
     */
    private T value;

    /**
     * Should this metric be collected
     * before pushing to Influx?
     */
    @Transient @JsonIgnore
    private boolean collector;

    /**
     * Collects the metric.
     */
    public void collect(MetricService metricService) {}

    /**
     * Gets this point as a {@link Point}.
     *
     * @return the point
     */
    public abstract Point toPoint();
}
