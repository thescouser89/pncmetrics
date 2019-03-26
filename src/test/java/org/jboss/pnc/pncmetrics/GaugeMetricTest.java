package org.jboss.pnc.pncmetrics;

import com.codahale.metrics.MetricRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class GaugeMetricTest {

    @Mock
    private MetricRegistry registry;

    @Test
    public void simpleTestConcurrency() {
        GaugeMetric metric = new GaugeMetric(registry);

        metric.incrementMetric("test");
        metric.incrementMetric("test");
        metric.incrementMetric("test");
        metric.incrementMetric("test");

        assertEquals(metric.getAndSetMetricValueToNull("test"), (Integer) 4);

        // subsequent run with no incrementMetric call should return null
        assertNull(metric.getAndSetMetricValueToNull("test"));
        assertNull(metric.getAndSetMetricValueToNull("test"));
    }

    @Test
    public void complexTestConcurrency() throws Exception {
        GaugeMetric metric = new GaugeMetric(registry);

        ExecutorService service = Executors.newFixedThreadPool(10);
        AtomicInteger atomicInteger = new AtomicInteger();

        Callable incrementTask = () -> {
            for (int i = 0; i < 10; i++) {
                metric.incrementMetric("test2");
            }
            return null;
        };

        Callable getTask = () -> {
            Integer value = metric.getAndSetMetricValueToNull("test2");
            if (value != null) {
                atomicInteger.getAndAdd(value);
            }
            return null;
        };

        List<Callable<String>> callables = new ArrayList<>();
        callables.add(incrementTask);
        callables.add(getTask);
        callables.add(incrementTask);
        callables.add(incrementTask);
        callables.add(getTask);
        callables.add(incrementTask);
        callables.add(incrementTask);
        callables.add(getTask);

        service.invokeAll(callables);

        // run it one last time
        getTask.call();

        assertEquals(50, atomicInteger.get());
    }

    @Test
    public void complexMultiKeyTestConcurrency() throws Exception {

        GaugeMetric metric = new GaugeMetric(registry);

        ExecutorService service = Executors.newFixedThreadPool(10);
        AtomicInteger atomicIntegerTest1 = new AtomicInteger();
        AtomicInteger atomicIntegerTest2 = new AtomicInteger();

        Callable incrementTaskTest1 = () -> {
            for (int i = 0; i < 10; i++) {
                metric.incrementMetric("test1");
            }
            return null;
        };

        Callable incrementTaskTest2 = () -> {
            for (int i = 0; i < 10; i++) {
                metric.incrementMetric("test2");
            }
            return null;
        };

        Callable getTaskTest1 = () -> {
            Integer value = metric.getAndSetMetricValueToNull("test1");
            if (value != null) {
                atomicIntegerTest1.getAndAdd(value);
            }
            return null;
        };

        Callable getTaskTest2 = () -> {
            Integer value = metric.getAndSetMetricValueToNull("test2");
            if (value != null) {
                atomicIntegerTest2.getAndAdd(value);
            }
            return null;
        };

        List<Callable<String>> callables = new ArrayList<>();
        callables.add(incrementTaskTest1);
        callables.add(getTaskTest2);
        callables.add(incrementTaskTest2);
        callables.add(incrementTaskTest2);
        callables.add(getTaskTest1);
        callables.add(incrementTaskTest1);
        callables.add(incrementTaskTest2);
        callables.add(getTaskTest1);

        callables.add(incrementTaskTest1);
        callables.add(incrementTaskTest2);
        callables.add(getTaskTest1);

        callables.add(incrementTaskTest1);
        callables.add(getTaskTest2);
        callables.add(incrementTaskTest2);

        callables.add(incrementTaskTest1);
        callables.add(incrementTaskTest2);

        service.invokeAll(callables);

        // run it one last time
        getTaskTest1.call();
        getTaskTest2.call();

        assertEquals(50, atomicIntegerTest1.get());
        assertEquals(60, atomicIntegerTest2.get());
    }
}
