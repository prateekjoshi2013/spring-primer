# Default target
all: clean compile test package

# Rule to clean the project
clean:
	mvn clean

# Rule to compile the project
compile:
	mvn compile

# Rule to run tests
test:
	mvn test -Dspring.profiles.active=default

# Rule to package the project
package:
	mvn package

# Rule to install the project to local repository
install:
	mvn install

# Rule to deploy the project
deploy:
	mvn deploy

# Rule to start spring boot app with mysql
run:
	mvn clean compile test spring-boot:run -Dspring-boot.run.profiles=default

# Phony targets to avoid conflicts with files of the same name
.PHONY: all clean compile test package install deploy run
