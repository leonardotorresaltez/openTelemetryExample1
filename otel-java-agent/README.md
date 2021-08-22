download the last version of the agent in :

https://github-releases.githubusercontent.com/210933087/1775533a-704d-4a79-af8d-fca08e04d173?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20210822%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20210822T144540Z&X-Amz-Expires=300&X-Amz-Signature=4ee03a2edeff0517423d5d7adf93e279ba9d9c98c8973eaa73834a44366c8800&X-Amz-SignedHeaders=host&actor_id=6289630&key_id=0&repo_id=210933087&response-content-disposition=attachment%3B%20filename%3Dopentelemetry-javaagent-all.jar&response-content-type=application%2Foctet-stream


start the app in the following way:

java -javaagent:path/to/opentelemetry-javaagent-all.jar \
     -Dotel.resource.attributes=service.name=microservice-service-name \
     -jar pkc-patients-3-1.jar