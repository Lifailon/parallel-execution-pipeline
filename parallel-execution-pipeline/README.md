# Parallel Execution Pipeline

This is a simple Jenkins Pipeline that I use in my home environment to automate execution commands on multiple remote hosts.

Supports 2 operating modes:

- Parallel execution of commands on one host.
- Sequential execution of commands, but in parallel on multiple hosts.

By default, the `OpenSSH` client is used on the agent in output coloring mode.

## Execution examples

Example of a command list:

```bash
sleep 6 && echo Complete sleep 6 sec on $(hostname)
sleep 8 && echo Complete sleep 8 sec on $(hostname)
sleep 4 && echo Complete sleep 4 sec on $(hostname)
```

- Result of execution on one host:

> [!NOTE]
> Please note the order in which the executed commands.

![](/parallel-execution-pipeline/img/parallel-commands.jpg)

- Result of execution on two hosts:

> [!NOTE]
> Please note the `timestamp`.

![](/parallel-execution-pipeline/img/parallel-hosts.jpg)
