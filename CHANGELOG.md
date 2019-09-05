# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).


## [Unreleased]

### Added

### Changed

## 1.1.3 - 2019-09-04
### Added
- Error metrics to TimedMetricFilter

## 1.1.0 - 2019-03-26
### Changed

- GaugeHelper was renamed to GaugeMetric and is now obtained from the
  MetricsConfiguration singleton. It was simplified to only deal with Integers.
  The only method to call is 'gaugeHelper.incremementMetric(key)' to increment
  a value. If a metric key is non-null, it is sent to Graphite and the metric
  key value is reset to null.

## 1.0.2 - 2019-03-23
### Added

- GaugeHelper to help with instantaneous data

## 1.0.1 - 2018-08-13
### Changed

- Key used to send REST metrics does not have 'pnc' in it anymore


## 1.0.0 - 2018-08-13
### Added

- Annotations to REST endpoint to gather data about a REST endpoint
- Auto configuration of Dropwizards metrics to send by default JVM and other
  data to the graphite server

# Template

## [<version>] - <yyyy>-<mm>-<dd>
### Added
- Section

### Changed
- Section

### Deprecated
- Section

### Removed
- Section

### Fixed
- Section

### Security
- Section

