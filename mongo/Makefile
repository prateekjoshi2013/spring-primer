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

# Rule to run verify phase which generates swagger/openapi spec
# verify step does not run compile step so run it after compile
verify:
	mvn verify -Dspring.profiles.active=default 


# Rule to test with mysql
test-mongo:
	mvn clean compile test -Dspring.profiles.active=test

# run the rest assured tests with test profile
test-rest:
	mvn clean compile test -Dspring.profiles.active=default

# Rule to start spring boot app with mysql
run:
	mvn clean compile spring-boot:run -Dspring-boot.run.profiles=default


test-all: test-rest test-mongo

# creates an oci image with name: docker.io/library/mongo:0.0.1-SNAPSHOT
# this maven goal is configured in the parent pom
image: 
	mvn clean compile package spring-boot:build-image -DskipTests=true


# Phony targets to avoid conflicts with files of the same name
.PHONY: all clean compile test package install deploy test-rest test-all test-mongo image
