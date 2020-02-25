package org.jboss.pnc.pncmetrics;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to help deal with Gauge Metrics, which is an instantaneous measurement of a value
 * <p>
 * When the metric is incremented via 'incrementMetric', it is stored in a map. When the metric gets sent to the
 * Graphite server, we send the data and reset the metric to null, until 'incrementMetric' is called again.
 * <p>
 * This allows us to only send data to Graphite when incrementMetric is called.
 */
public class GaugeMetric {

    private static GaugeMetric gaugeMetric;

    private Map<String, Integer> metrics = new HashMap<>();

    private MetricRegistry registry;

    GaugeMetric(MetricRegistry registry) {
        this.registry = registry;
    }

    /**
     * Increment integer for a key
     *
     * @param key
     */
    public synchronized void incrementMetric(String key) {

        // first time using the key
        if (!metrics.containsKey(key)) {
            registerMetric(key);
        }

        metrics.putIfAbsent(key, 0);
        Integer toIncrement = metrics.get(key);
        metrics.put(key, toIncrement + 1);
    }

    public synchronized Integer getAndSetMetricValueToNull(String key) {

        Integer value = metrics.get(key);
        metrics.put(key, null);
        return value;
    }

    /**
     * Register the particular metric we want to send, and specify how to get that metric (via the map)
     *
     * @param key
     */
    private void registerMetric(String key) {

        registry.register(MetricRegistry.name(key), (Gauge<Integer>) () -> getAndSetMetricValueToNull(key));
    }
}
