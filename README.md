# Plan2Go-user-server
Repo for the Plan2Go user service, including the user class, userController, userService and userRepository.\
This server is responsible for user management of Plan2Go.\
The default server.port = 8081

## Technology
MongoDB
Sprint Boot
WebSocket

## Launch & Deployment

### Building with Gradle

You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

### Test

```bash
./gradlew test
```
### Start the application

Run the UserServerApplication from src/main/java/ASESpaghettiCode/UserServer/UserServerApplication.java to start the server.
