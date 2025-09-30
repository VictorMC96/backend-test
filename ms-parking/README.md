servicios:

POST:
http://localhost:8080/ms-parking/api/vehicle

{
"plate": "ABC-123-RES",
"type": "RESIDENTE"
}

<br>
GET:
http://localhost:8080/ms-parking/api/vehicle

<br>
GET:
http://localhost:8080/ms-parking/api/parking

<br>
POST:
http://localhost:8080/ms-parking/api/parking/entry/ABC-NOT-RES

<br>
POST:
http://localhost:8080/ms-parking/api/parking/exit/ABC-NOT-RES
