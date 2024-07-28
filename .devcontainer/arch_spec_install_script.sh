#!/bin/bash

# Detect architecture
ARCH=$(uname -m)

apk add --update openjdk17

if [ "$ARCH" = "x86_64" ]; then
    echo "Running on x86_64 architecture"
    # Commands for x86_64 architecture
    # Set JAVA_HOME and update PATH
    echo "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk" >> ~/.bashrc
elif [ "$ARCH" = "arm64" ] || [ "$ARCH" = "aarch64" ]; then
    echo "Running on arm64 architecture"
    # Commands for arm64/aarch64 architecture
    echo "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk" >> ~/.bashrc
else
    echo "Unknown architecture: $ARCH"
    exit 1
fi

echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc
source ~/.bashrc