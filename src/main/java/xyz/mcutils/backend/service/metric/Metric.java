package xyz.mcutils.backend.service.metric;

import com.influxdb.annotations.Measurement;
import com.influxdb.client.write.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Getter @Setter
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
     * Gets this point as a {@link Point}.
     *
     * @return the point
     */
    public abstract Point toPoint();
}
