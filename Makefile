start: dev build execute

dev:
	docker-compose down --volumes && \
	docker-compose up --detach

build:
	./gradlew clean build

execute: build
	./gradlew bootRun