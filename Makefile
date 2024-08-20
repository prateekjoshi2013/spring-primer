auth-server:
	mvn -f ./auth.server/ clean compile package  -DskipTests=true
	docker build -f Dockerfile -t auth.server:latest ./auth.server/

gateway:
	mvn -f ./gateway/ clean compile package -DskipTests=true
	docker build -f Dockerfile -t gateway:latest ./gateway/

reactive-mongo:
	mvn -f ./mongo/ clean compile package -DskipTests=true
	docker build -f Dockerfile -t reactive-mongo:latest ./mongo/

reactive:
	mvn -f ./r2dbcapp/ clean compile package -DskipTests=true
	docker build -f Dockerfile -t r2dbcapp:latest ./r2dbcapp/

rest-mvc:
	mvn -f ./spring-rest-demo/ clean compile package -DskipTests=true
	docker build -f Dockerfile -t spring-rest-demo:latest ./spring-rest-demo/

all: auth-server gateway reactive-mongo reactive rest-mvc

.PHONY: auth-server	gateway reactive-mongo reactive rest-mvc all