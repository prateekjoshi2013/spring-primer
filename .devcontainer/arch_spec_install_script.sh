#!/bin/bash

# Detect architecture
ARCH=$(uname -m)

apt-get install -y openjdk-17-jdk

if [ "$ARCH" = "x86_64" ]; then
    echo "Running on x86_64 architecture"
    # Commands for x86_64 architecture
    # Set JAVA_HOME and update PATH
    echo "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> ~/.bashrc
elif [ "$ARCH" = "arm64" ] || [ "$ARCH" = "aarch64" ]; then
    echo "Running on arm64 architecture"
    # Commands for arm64/aarch64 architecture
    echo "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-arm64" >> ~/.bashrc
else
    echo "Unknown architecture: $ARCH"
    exit 1
fi

echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc
source ~/.bashrc