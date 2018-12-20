# Webservice XML to JSON converter
Application starts HTTP server on localhost:8080 to process HTTP POST
requests with data (xml formatted string), providing converted to json
format string.

### Docker
Application is built with Gradle, which has tasks for making docker-image.
Uses bmuschko/gradle-docker-plugin.

- Build: `./gradlew build`
- Create docker-image: `./gradlew create`
- Start service: `./gradlew start`

### Usage
Start service with `./gradlew start`, then it will be running on
http://localhost:8080. Now you can send post requests to
http://localhost:8080/convert.

Example:
`curl -d @test.xml http://localhost:8080/convert`