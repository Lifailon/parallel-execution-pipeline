# Parallel Execution Pipeline

A set of universal Jenkins Pipelines that I use in my home environment to automate base tasks.

- [Parallel Execution Pipeline](/parallel-execution-pipeline/README.md)
- [Go Build and Deploy](/go-build-deploy/README.md)
- [Make Runner](/make-runner/README.md)
- [Pre-built Binary Deploy](/pre-built-binary-deploy/README.md)
- [Update authorized_keys](/update-authorized_keys/README.md)
- [Container Build Deploy](/container-build-deploy/README.md)
- [Export and Backup Jobs](/export-and-backup-jobs/README.md)
- [Monitoring Agent Stack Deploy](/mon-agent-stack-deploy/README.md)
- [Parallel SSH Pipeline](/parallel-ssh-pipeline/README.md)

## Launch

To use any pipeline in your environment, use SCM Git:

- Repository URL: `https://github.com/Lifailon/parallel-execution-pipeline`
- Branch: `main`
- Script Path: `<folder_name_pipeline>/Jenkinsfile.groovy`
- - Example: `parallel-execution-pipeline/Jenkinsfile.groovy`

## Dependencies

Plugins used in most pipelines:

- [SSH Pipeline Steps](https://plugins.jenkins.io/ssh-steps) to extracting the private key from credentials and connection to remote hosts.
- [AnsiColor](https://plugins.jenkins.io/ansicolor) (optional) to separate commands or hosts in the output by color.
- [Active Choices](https://plugins.jenkins.io/uno-choice) to support the use of dynamic parameters.
- [Ansible](https://plugins.jenkins.io/ansible) to pass parameters to Ansible for projects that use templating.

Need to create an `SSH Username with private key` for pass to the `credentials` parameter.

To keep all Pipelines running, I use a pre-configured [image](/jenkins-agent/README.md) of Jenkins agent with a predefined list of packages.