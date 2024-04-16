package xyz.mcutils.backend.service.metric;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.influxdb.annotations.Measurement;
import com.influxdb.client.write.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Getter @Setter @ToString
@RedisHash(value = "metric")
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
    @JsonIgnore
    private transient boolean collector;

    /**
     * Collects the metric.
     */
    public void collect() {}

    /**
     * Gets this point as a {@link Point}.
     *
     * @return the point
     */
    public abstract Point toPoint();
}
