###Microservice-client

#### Pre-requisites
- Java 8
- Maven 3.x.x
- Cuenta de aws
- Git

#### Build
##### Lambda create client
- cd client && mvn package

##### Lambda get client's kpi
- cd client-kpi && mvn package

##### Lambda list clients
- cd get-clients && mvn package

#### Deploy
#### Database
- create table with name CLIENTS
    - configure to pay to use

#### Lambda
- For each lambda create function
    - select runtime : Java 8
    - add dynamo's access to lambda's role
    
#### Api Gateway
- Create Api Gateway and asociate each lambda to endpoint        