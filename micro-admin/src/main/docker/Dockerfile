FROM docker.io/relateiq/oracle-java8
VOLUME /tmp
ADD springboot-web.jar app.jar
#RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8881

