
# creates an oci image with name: docker.io/library/gateway:0.0.1-SNAPSHOT
# this maven goal is configured in the parent pom
image: 
	mvn clean compile package spring-boot:build-image -DskipTests=true

.PHONY: image	