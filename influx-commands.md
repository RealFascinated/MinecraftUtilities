# Useful InfluxDB commands

## Delete data from bucket

```bash
influx delete --bucket mcutils --start 2024-01-01T00:00:00Z --stop 2025-01-05T00:00:00Z --org mcutils --token setme --predicate '_measurement="requests_per_route"
```