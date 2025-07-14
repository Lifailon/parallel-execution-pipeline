# Parallel Execution Pipeline

This is a simple Jenkins Pipeline that I use in my home environment to automate execution commands on multiple remote hosts.

Supports 2 operating modes:

- Parallel execution of commands on one host.
- Sequential execution of commands, but in parallel on multiple hosts.

## Launch

To run in your environment, use SCM Git:

- Repository URL: `https://github.com/Lifailon/parallel-execution-pipeline`
- Branch: `main`
- Script Path: `parallel-execution-pipeline/Jenkinsfile.groovy`

Need to create an `SSH Username with private key` for pass to the credentials parameter.

## Plugins used (dependencies)

- [SSH Pipeline steps](https://plugins.jenkins.io/ssh-steps) for extracting private key from credentials and connection to remote hosts in output coloring disabled mode (by default used `OpenSSH` client on agent for correct output coloring).
- [AnsiColor](https://plugins.jenkins.io/ansicolor) (optional) for separate commands or hosts in the output by color.

## Execution examples

Example of a command list:

```bash
sleep 6 && echo Complete sleep 6 sec on $(hostname)
sleep 8 && echo Complete sleep 8 sec on $(hostname)
sleep 4 && echo Complete sleep 4 sec on $(hostname)
```

- Result of execution on one host:

![](/.img/parallel-commands.jpg)

- Result of execution on two hosts:

![](/.img/parallel-hosts.jpg)
