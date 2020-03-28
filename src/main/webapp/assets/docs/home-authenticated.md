### Gateway Application Menu

1. **Admin** : The administration tasks

## Application Management Module

Allow to define custom form and configuration via YAML.

- `app-management/form/{folder}/{file.yml}`: Render a custom form based on `assets/config/${folder}/${file}.yml` path.
- `app-management/settings/{setting-form.yml}`: Render a settings form that require the file `assets/config/${setting-form.yml}` exists. Suitable for required application settings
- `app-management/configuration/{config-form.yml}`: Render a settings form that require the file `assets/configuration/${config-form.yml}` exists or not. Suitable for optional configuration.
