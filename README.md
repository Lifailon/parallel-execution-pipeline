# Parallel Execution Pipeline

This is a simple Jenkins Pipeline that I use in my home environment to automate execution commands on multiple remote hosts.

Supports 2 operating modes:

- Parallel execution of commands on one host.
- Sequential execution of commands, but in parallel on multiple hosts.

## Launch

To run in your environment, use SCM Git:

- Repository URL: `https://github.com/Lifailon/parallel-execution-pipeline`
- Branch: `main`
- Script Path: `/parallel-execution-pipeline/Jenkinsfile_v2.groovy`

In the settings, you need to fill in the ID for `SSH Username with private key` in the credentials parameter and update the list of hosts.

## Plugins used (dependencies)

- [SSH Pipeline Steps](https://plugins.jenkins.io/ssh-steps) for connect to remote hosts.
- [AnsiColor](https://plugins.jenkins.io/ansicolor) (optional) for separate commands or hosts in the output by color.

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
