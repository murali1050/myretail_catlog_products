# openjdk does not have a supported alpine image for Java 11, using adoptopenjdk instead
FROM adoptopenjdk/openjdk11:alpine-jre

ARG VERSION
ENV APP_VERSION ${VERSION}

WORKDIR /opt/app

RUN addgroup -S appy \
    && adduser -D -S -h /opt/app -s /sbin/nologin -G appy appy \
    && chown -R appy /opt/app

COPY target/catalog-${VERSION}.jar /opt/app/catalog.jar

RUN chown appy:appy /opt/app/catalog.jar

# run as non-root user
USER appy

# urandom = http://stackoverflow.com/a/33882286/5608849 (one of several references but this one specific to docker)
# heap size = http://stackoverflow.com/a/41098096/5608849 (one of serveral, just very specific here)
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "-Xmx1024m", "/opt/app/catalog.jar"]
