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

# Rule to start spring boot app with mysql
run:
	mvn clean compile test spring-boot:run -Dspring-boot.run.profiles=default

# cleans compiles tests and generates swagger doc
verify: clean test verify

# creates an oci image with name: docker.io/library/r2dbcapp:0.0.1-SNAPSHOT
# this maven goal is configured in the parent pom
image: 
	mvn clean compile package spring-boot:build-image -DskipTests=true
	docker build -t r2dbcapp:latest .
# Phony targets to avoid conflicts with files of the same name
.PHONY: all clean compile test package install deploy run image
