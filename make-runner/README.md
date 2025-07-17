# Make Runner

A universal Jenkins Pipeline for running make from specified GitHub repository. Before starting, the `targets` parameter automatically detects all available targets in the main `Makefile` from the remote repository to select them, override the necessary flags (parallel mode is supported) and set variables. During operation, the repository is cloned, the specified list of selected targets is executed and, if necessary, the selected files are uploaded to artifacts.

