# pncmetrics: Add metrics to your Jakarta EE projects!
test

The metrics collected are sent to a Graphite server every minute. pncmetrics is currently only tested to work on Jakarta EE 8 projects.

You get for free JVM metrics such as memory, threads, classloaders by default.


## Setup

1. Add the pncmetrics dependency to your pom.xml and to any modules that require metrics.
2. Add the pncmetrics jar into your jar module in your ear definition
3. Optionally add the pncmetrics dependency to your testsuite if it fails after the changes above
4. Optionally activate the REST metrics helper if you want to measure REST data
```
 resources.add(GeneralRestMetricsFilter.class);
 resources.add(TimedMetric.class);
 resources.add(TimedMetricFilter.class);
```

## Configuration

To make sure pncmetrics properly report data, you need to set those environment variables / properties (aka -D<property>=<value>) in your project:

1. `metrics_graphite_server`: the Graphite server to send data
2. `metrics_graphite_port`: the port the Graphite server is using (typically 2003)
3. `metrics_graphite_prefix`: prefix to use on metrics reported to use as namespace (e.g <serverUrl>)
4. `metrics_graphite_interval`: (Optional) interval in seconds to send data to Graphite (default: 60s)

## Usage

### Profile a REST endpoint

Add the annotation `@TimedMetric` before the method defining a REST endpoint to get time data about the endpoint.

The data will be reported to Graphite as '<prefix>.rest.<RestClass>.<methodName>.rate' and '<key>.rest.<RestClass>.<methodName>.timer'

Once pncmetrics is properly configured, it will also by default track the time and usage of all the REST endpoints into 2 metrics: '<prefix>.rest.all.rate' and '<key>.rest.all.timer'


### Profile anything else

In your POJO, inject the `MetricsConfiguration` singleton:

```
@Inject
private MetricsConfiguration metricsConfig;

...
// In your method
MetricRegistry registry = metricsConfig.getMetricRegistry();
// then consult the Dropwizard metrics on how to use the registry object
// https://metrics.dropwizard.io/4.0.0/getting-started.html
```
