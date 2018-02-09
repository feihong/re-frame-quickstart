FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/quickstart.jar /quickstart/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/quickstart/app.jar"]
