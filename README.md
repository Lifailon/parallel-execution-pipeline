# Parallel Execution Pipeline

This is a simple Jenkins pipeline that I use in my home environment to automate execution commands on multiple remote hosts.

Supports 2 operating modes:

- Parallel execution of commands on one host.
- Sequential execution of commands, but in parallel on multiple hosts.

## Install

To run in your environment, use SCM Git `https://github.com/Lifailon/parallel-execution-pipeline.git` (branch `main`) to download `Jenkinsfile.groovy` file and update parameters in job settings.

## Plugins used

- [SSH Pipeline Steps](https://plugins.jenkins.io/ssh-steps) to connect to remote hosts.
- [AnsiColor](https://plugins.jenkins.io/ansicolor) to separate commands or hosts in the output by color.

## Execution examples

Example of a command list:

```bash
sleep 6 && echo Complete sleep 6 sec on $(hostname)
sleep 8 && echo Complete sleep 8 sec on $(hostname)
sleep 4 && echo Complete sleep 4 sec on $(hostname)
```

- Result of execution on one host:

![](/img/parallel-commands.jpg)

- Result of execution on two hosts:

![](/img/parallel-hosts.jpg)