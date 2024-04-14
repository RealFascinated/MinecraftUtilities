package xyz.mcutils.backend.service.metric.metrics;

import xyz.mcutils.backend.service.metric.impl.MapMetric;

public class RequestsPerRouteMetric extends MapMetric<String, Integer> {

    public RequestsPerRouteMetric() {
        super("requests_per_route");
    }

    /**
     * Increment the value for this route.
     *
     * @param route the route to increment
     */
    public void increment(String route) {
        getValue().put(route, getValue().getOrDefault(route, 0) + 1);
    }
}
