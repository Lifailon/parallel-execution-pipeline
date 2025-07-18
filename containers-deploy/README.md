# Containers Deploy

Pipeline implements the deployment of agents for monitoring metrics and logs from Linux and the Docker containers using Ansible. Templating and parameterization are supported for 4 agents: `Node Exporter`, `cAdvisor`, `LogPorter` and `Loki promtail` (if you want to skip installation of a specific agent, leave the port parameter blank).

## Local run

```bash
git clone https://github.com/Lifailon/parallel-execution-pipeline
cd parallel-execution-pipeline/mon-agent-deploy

# Update host list and variables
# ./inventories/inventory.yml
# ./inventories/group_vars/*.yml

ansible-playbook -l <HOSTS_GROUP_NAME> -i ./inventories/inventory.yml ./playbooks/deploy.yml
```

## Parameters

![](/mon-agent-deploy/img/params.jpg)

## Deployment

![](/mon-agent-deploy/img/playbook-run.jpg)