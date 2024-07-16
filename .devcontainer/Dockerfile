# Use an Ubuntu base image
FROM ubuntu:22.04

# Set the default shell to bash
ENV SHELL /bin/bash

# Set shell as bash
SHELL ["/bin/bash", "-c"]

# Install necessary packages and tools
RUN apt-get update && apt-get install -y

RUN apt-get install curl git unzip nano make locales -y 

# Run arch specificexpectations for specific arch (x86_64, arm64, aarch64)
COPY ./arch_spec_install_script.sh /scripts/

RUN chmod +x /scripts/arch_spec_install_script.sh


# installing oh my bash - https://github.com/ohmybash/oh-my-bash
RUN curl -fsSL https://raw.githubusercontent.com/ohmybash/oh-my-bash/master/tools/install.sh | bash

# customize the bash shell ans set vscode theme
RUN sed -i 's/^OSH_THEME=.*/OSH_THEME="vscode"/' ~/.bashrc

RUN /scripts/arch_spec_install_script.sh

# locale generation is important to make bash 
# work properly with newlines and such
RUN locale-gen en_US.UTF-8

# Install Maven
RUN mkdir -p /usr/share/maven /usr/share/maven/ref && \
    curl -fsSL https://dlcdn.apache.org/maven/maven-3/3.9.8/binaries/apache-maven-3.9.8-bin.zip -o /tmp/maven.zip && \
    unzip /tmp/maven.zip -d /usr/share/maven && \
    ln -s /usr/share/maven/apache-maven-3.9.8 /usr/share/maven/latest && \
    rm /tmp/maven.zip

# Set MAVEN_HOME and update PATH
RUN echo "export MAVEN_HOME=/usr/share/maven/latest" >> ~/.bashrc && \
    echo "export PATH=\$MAVEN_HOME/bin:\$PATH" >> ~/.bashrc

# Create .m2 folder with user settings
RUN mkdir -p /root/.m2

# Copy the settings.xml file to the .m2 directory
COPY ./settings.xml /root/.m2/
    
# Verify the installation
RUN java -version