### Gateway Application Menu

1. **Admin** : The administration tasks

## Application Management Module

Allow to define custom form and configuration via YAML.

- `app-management/form/{folder}/{file.yaml}`: Render a custom form based on `assets/${folder}/${file}.yaml` path.
- `app-management/settings/{setting-form.yaml}`: Render a settings form that require the file `assets/config/${setting-form.yaml}` exists. Suitable for required application settings
- `app-management/configuration/{config-form.yaml}`: Render a settings form that require the file `assets/configuration/${config-form.yaml}` exists or not. Suitable for optional configuration.
