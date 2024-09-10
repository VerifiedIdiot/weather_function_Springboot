#Dockerfile

FROM openjdk:17
ARG JAR_FILE=build/libs/weather.jar
COPY ${JAR_FILE} ./weather.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "./weather.jar"]