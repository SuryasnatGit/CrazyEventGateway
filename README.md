# Getting Started

### Application run
1. Setup application as eclipse spring boot project and run spring boot application from there.
OR
2. Do mvn clean install. It will create CrazyEventGateway-0.0.1-SNAPSHOT.jar in the target directory. Then run java -jar target\CrazyEventGateway-0.0.1-SNAPSHOT.jar

### API testing
<code>
curl --location --request POST 'http://localhost:8080/api/v1/events' \
--header 'Content-Type: application/json' \
--header 'ClientID: 1' \
--header 'Authorization: Basic dXNlcjE6cGFzczE=' \
--data-raw '[
    {
        "appId": 11,
        "eventType": 1,
        "data": "11"
    },
    {
        "appId": 11,
        "eventType": 1,
        "data": "22"
    },
    {
        "appId": 11,
        "eventType": 1,
        "data": "33"
    },
    {
        "appId": 11,
        "eventType": 1,
        "data": "44"
    }
]'
</code>