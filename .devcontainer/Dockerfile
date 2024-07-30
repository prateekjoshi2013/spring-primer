# Use an alpine base image smaller than ubuntu
FROM alpine:3.20

# Install necessary packages and tools
RUN apk add --no-cache bash curl git unzip nano make musl-utils musl-locales tzdata openssh

# Set the default shell to bash
ENV SHELL /bin/bash

# Set shell as bash
SHELL ["/bin/bash", "-c"]

RUN ln -s /usr/share/zoneinfo/America/Phoenix /etc/localtime

RUN rm -r /usr/share/zoneinfo/Africa && \
    rm -r /usr/share/zoneinfo/Antarctica && \
    rm -r /usr/share/zoneinfo/Arctic && \
    rm -r /usr/share/zoneinfo/Asia && \
    rm -r /usr/share/zoneinfo/Atlantic && \
    rm -r /usr/share/zoneinfo/Australia && \
    rm -r /usr/share/zoneinfo/Europe  && \
    rm -r /usr/share/zoneinfo/Indian && \
    rm -r /usr/share/zoneinfo/Mexico && \
    rm -r /usr/share/zoneinfo/Pacific && \
    rm -r /usr/share/zoneinfo/Chile && \
    rm -r /usr/share/zoneinfo/Canada

ENV TZ America/Phoenix

ENV LANG=en_US.UTF-8 \
    LANGUAGE=en_US:en \
    LC_ALL=en_US.UTF-8

# Verify locale settings
# locale configuration is important to make bash 
# work properly with newlines and such
RUN locale -a && echo "Locale setup completed."

# Run arch specificexpectations for specific arch (x86_64, arm64, aarch64)
COPY ./arch_spec_install_script.sh /scripts/

RUN chmod +x /scripts/arch_spec_install_script.sh

# installing oh my bash - https://github.com/ohmybash/oh-my-bash
RUN curl -fsSL https://raw.githubusercontent.com/ohmybash/oh-my-bash/master/tools/install.sh | bash

# customize the bash shell ans set vscode theme
RUN sed -i 's/^OSH_THEME=.*/OSH_THEME="vscode"/' ~/.bashrc

RUN /scripts/arch_spec_install_script.sh

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