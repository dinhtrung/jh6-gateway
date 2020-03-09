# User Guide

### Object Storage Support

By default Gateway support file upload via minio.

```yaml
# Support AWS S3 Storage
minio:
  endpoint: 'https://play.min.io'
  accessKey: 'Q3AM3UQ867SPQQA43P2F'
  secretKey: 'zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG'
  bucketName: jh6-gateway
```

The following endpoint allow file upload via Min IO:

`TBD`

### Theme Development

`TBD`

### Audit Log Profile

Turn on Audit log profile will keep track of all `PUT`, `POST` and `DELETE` request served by the gateway.

E.g, by setting the environment variables like this:

`SPRING_PROFILES_ACTIVE=prod,swagger,audit,deployment`

The app should read `application-deployment.yaml` file for app custom, and keep track of all request and response (turn on by profile `audit`).
