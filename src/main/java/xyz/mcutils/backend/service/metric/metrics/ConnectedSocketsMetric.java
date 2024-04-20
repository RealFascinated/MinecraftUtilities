package xyz.mcutils.backend.service.metric.metrics;

import xyz.mcutils.backend.service.metric.impl.IntegerMetric;
import xyz.mcutils.backend.websocket.WebSocketManager;

public class ConnectedSocketsMetric extends IntegerMetric {

    public ConnectedSocketsMetric() {
        super("connected_sockets");
    }

    @Override
    public boolean isCollector() {
        return true;
    }

    @Override
    public void collect() {
        setValue(WebSocketManager.getTotalConnections());
    }
}
