FROM jenkins/inbound-agent:latest

USER root

# Updating and installing additional packages
RUN apt-get update && apt-get install -y \
    git \
    curl \
    iputils-ping \
    netcat-openbsd \
    make \
    tmux

# Install Ansible
RUN apt-get -y install \
    python3-pip && \
    pip3 install --break-system-packages ansible && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install the latest version of Go
RUN ARCH=$(uname -m) && \
    case "$ARCH" in \
        "aarch64" | "arm64") ARCH="arm64" ;; \
        "x86_64"  | "amd64") ARCH="amd64" ;; \
    esac && \
    LATEST_GO_VERSION=$(curl -s https://go.dev/VERSION?m=text | head -1) && \
    curl -L "https://go.dev/dl/${LATEST_GO_VERSION}.linux-${ARCH}.tar.gz" | tar -xz -C /usr/local

# Add Go to PATH
ENV PATH="/usr/local/go/bin:${PATH}"

# Granting access rights to the jenkins group on the build directory
# RUN chown -R jenkins:jenkins /home/jenkins/workspace
RUN chown -R jenkins:jenkins /home/jenkins

USER jenkins

# Check versions
RUN ansible --version && \
    python3 --version && \
    go version

# Runtime variables are passed via environment (.env file) when running in docker compose
# /opt/java/openjdk/bin/java -jar /usr/share/jenkins/agent.jar -secret $JENKINS_SECRET -name $JENKINS_AGENT_NAME -url $JENKINS_SERVER_URL
ENTRYPOINT ["/usr/local/bin/jenkins-agent"]