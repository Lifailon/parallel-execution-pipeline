# Go Build and Deploy

This is a universal Pipeline for building, testing and deployment any Go application.

## Supported functions

- Application preparation (basic Go command set) and and checking the launch.
- Updating dependencies.
- List of available tests with the ability to run all or selected.
- Installing and checking on linters in parallel with `golangci`, `gocritic` and `gosec`.
- Checking the application startup in `TMUX` (useful for checking the functionality of the interface).
- Parallel build for all platforms and architectures (automatic version detection for the name).
- Uploading all binaries to artifacts.
- Deployment to remote hosts by copying the prepared binary with architecture detection.

## Parameters

![](/go-build-deploy/img/params.jpg)

## Build

![](/go-build-deploy/img/build.jpg)

## Artifacts

![](/go-build-deploy/img/artifacts.jpg)