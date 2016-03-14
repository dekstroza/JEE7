# Swarm and JEE7 Example II

## About

Example of JEE7 code using JAXRS with WELD, packaged and deployed in docker container as wildfly-swarm.
Database backend is PostgreSQL 9.x, with predifiend schema runing in docker container.

## Running the code

1. Build and deploy database container

```
$ cd database-container && docker build -t db_backend . 
$ docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d db_backend
```
Build the code with:
```
$ mvn clean install ## will just build code and run tests
$ mvn clean install -Pdocker ## will build swarm jar & docker container as well
```

To run the code:
```
## 
## Database host can be specified with -DdbHost=IP
## Database port can be specified wtih -DdbPort=PORT
## Database name can be specified wtih -DdbName=DB_NAME
## Database user and password can be specified with -DdbUser=USER (-DdbPassword=PASSWORD)
##
## Default values, if non of the above is specified: dbHost=localhost, dbPort=5432, dbName=swarmapp, dbUser=postgres, dbPassword=mysecretpassword
##

$ java -jar payment-backend/target/payment-backend-1.0.1-SNAPSHOT-swarm.jar
```

Or run the containers:
```
cd docker-compose && docker-compose up
```

Test everything is working by doing POST to ```http://IP:8080/api/v1.0/payment``` using content type: application/json

```
{
"firstName": "Dejan",
"lastName": "Kitic",
"phone": "0871984116",
"totalAmount": 500,
"feeDeductedAmount": 450,
"senderLocationId": 100,
"receiverLocationId": 200
}

```

To list all entries from database, do GET to ```http://IP:8080/api/v1.0/payment``` it will return JSON containing all records.

To update record (marking it as payed) do PUT to ```http://IP:8080/api/v1.0/payment/${UUID}?passport=${SOME_PASSPORT_DATA}```

To find out IP address use something like:
```
docker ps -a | grep swarm | grep Up | awk '{print $1}' | xargs -I {} docker inspect {} | grep IPAddress

```



Happy hacking,
Dejan