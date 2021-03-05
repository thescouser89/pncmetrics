package org.jboss.pnc.pncmetrics;

import com.codahale.metrics.MetricRegistry;
import lombok.Getter;
import org.jboss.pnc.pncmetrics.exceptions.NoPropertyException;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * used to inject in EJBs
 */
@Singleton
@Startup
public class MetricsConfiguration {

    @Inject
    private MetricsCDIConfiguration metricsCDIConfiguration;

    public MetricRegistry getMetricRegistry() {
        return metricsCDIConfiguration.getMetricRegistry();
    }

    public MetricRegistry getProcessMetricRegistry() {
        return metricsCDIConfiguration.getProcessMetricRegistry();
    }

    public GaugeMetric getGaugeMetric() {
        return metricsCDIConfiguration.getGaugeMetric();
    }

    public String getValueFromProperty(String propertyName, String description) throws NoPropertyException {
        return metricsCDIConfiguration.getValueFromProperty(propertyName, description);
    }
}
