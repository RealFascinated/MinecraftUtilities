package xyz.mcutils.backend.service.metric.impl;

import com.influxdb.client.write.Point;
import xyz.mcutils.backend.service.metric.Metric;

import java.util.HashMap;
import java.util.Map;

public class MapMetric <A, B> extends Metric<Map<A, B>> {

    public MapMetric(String id) {
        super(id, new HashMap<>(), false);
    }

    @Override
    public Point toPoint() {
        if (getValue().isEmpty()) { // The map is empty
            return null;
        }
        Point point = Point.measurement(getId());
        for (Map.Entry<A, B> entry : getValue().entrySet()) {
            switch (entry.getValue().getClass().getSimpleName()) {
                case "Integer":
                    point.addField(entry.getKey().toString(), (int) entry.getValue());
                    break;
                case "Double":
                    point.addField(entry.getKey().toString(), (double) entry.getValue());
                    break;
                case "Long":
                    point.addField(entry.getKey().toString(), (long) entry.getValue());
                    break;
                default:
                    point.addField(entry.getKey().toString(), entry.getValue().toString());
                    break;
            }
        }
        return point;
    }
}
