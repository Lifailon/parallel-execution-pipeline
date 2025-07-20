# Monitoring Agent Stack Deploy

Pipeline implements deployment of agents for monitoring metrics and logs from Linux systems and Docker containers using Ansible. Templating and parameterization are supported for the following agents:

- [Node Exporter](https://github.com/prometheus/node_exporter)
- [cAdvisor](https://github.com/google/cadvisor)
- [LogPorter](https://github.com/Lifailon/logporter)
- [Loki promtail](https://github.com/grafana/loki)
- [Dozzle](https://github.com/amir20/dozzle)
- [Beszel](https://github.com/henrygd/beszel)
- [Watchtower](https://github.com/containrrr/watchtower)

## Parameters

![](/mon-agent-stack-deploy/img/params.jpg)

## Deployment

![](/mon-agent-stack-deploy/img/playbook-run.jpg)