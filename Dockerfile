FROM openjdk:8
ADD target/docker-spring-boot.jar docker-spring-boot.jar


MAINTAINER patel patelavinashbirgunj@gmail.com
EXPOSE 8088

#CMD [ "mongod", "--bind_ip", "0.0.0.0" ]

ENTRYPOINT ["java", "-jar", "docker-spring-boot.jar"]