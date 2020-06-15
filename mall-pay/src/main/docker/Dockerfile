FROM docker.io/relateiq/oracle-java8
#VOLUME /tmp
ADD mall-pay.jar app.jar
#RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

