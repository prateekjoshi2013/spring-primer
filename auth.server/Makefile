# creates an oci image with name: docker.io/library/auth.server:0.0.1-SNAPSHOT
# this maven goal is configured in the parent pom
image: 
	mvn clean compile package spring-boot:build-image -DskipTests=true
	docker build -t auth.server:latest .

.PHONY: image	