# Pre-built Binary Deploy Pipeline

This is a simple Jenkins pipeline to automate the delete and installation of any pre-built binary of a selected version from a GitHub repository using [eget](https://github.com/zyedidia/eget) on remote hosts.

The pipeline downloads the latest version of `eget` on the agent, then downloads the binary of the selected version from the GitHub repository for two architectures (`amd64` and `arm64`) for Linux, for each remote machine determines the architecture, sends the binary to the remote host in the specified directory, grants execute permissions and checks the version.

## Parameters

![](/.img/deploy-params.jpg)