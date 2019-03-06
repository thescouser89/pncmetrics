package org.jboss.pnc.pncmetrics;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import javax.inject.Inject;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class to help deal with Gauge Metrics, which is an instantaneous measurement of a value
 *
 * When the metric is set via 'updateMetric', it is stored in a map. When the metric gets sent
 * to the Graphite server, we send the data and reset the metric to null, until 'updateMetric' is called again.
 *
 * This allows us to only send data to Graphite when updateMetric is called.
 *
 */
public class GaugeHelper<T extends Number> {

    private ConcurrentHashMap<String, T> metrics = new ConcurrentHashMap<>();

    private MetricRegistry registry;

    @Inject
    public GaugeHelper(MetricsConfiguration metricsConfiguration) {

        registry = metricsConfiguration.getMetricRegistry();
    }


    /**
     * updateMetric stores the metric for the key in a map. When we are ready to send that metric to Graphite, we read
     * the value in the map then, set the value to null. This allows us to only send data to Graphite when updateMetric
     * is called.
     *
     * @param key Metrics key
     * @param value value to send
     */
    public void updateMetric(String key, T value) {

        // if new metric key, register it to the metrics registry first
        if (!metrics.containsKey(key)) {
            registerMetric(key);
        }

        metrics.put(key, value);
    }

    /**
     * Get the value of the metric for a particular key
     *
     * @param key
     * @return the value of the metric
     */
    public T getMetric(String key) {
        return metrics.get(key);
    }

    /**
     * Register the particular metric we want to send, and specify how to get that metric (via the map)
     *
     * @param key
     */
    private void registerMetric(String key) {

        registry.register(MetricRegistry.name(key), (Gauge<T>) () -> {
           T value = metrics.get(key);
           metrics.put(key, null);
           return value;
        });

    }


}
