# socialnetwork
Simple 2 page application which emulates topic posting.

## Modules
The project contains of 5 modules with roles
* **:bom** - The bill of materials of the project which contains dependencies versions
* **:api** - The module contains interfaces which are used in the project
* **:persistence** - That module consists of DAO implementation and persistence config
* **:service** - Business logic of the project
* **:web** - Views, Controllers, Webconfig

## Installation & Run
run cmds

./gradlew :copyArtifacts

docker build -t my-base-book .

docker run -p 8080:8080  my-base-book 

## Database
[schema.sql](persistence/src/main/resources/schema.sql)

## Run tests
./gradlew test

## Build
./gradlew build