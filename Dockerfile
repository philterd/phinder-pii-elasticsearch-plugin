FROM elasticsearch:8.17.0

ARG VERSION="1.0.0-SNAPSHOT"

COPY ./build/distributions/phinder-${VERSION}.zip /tmp/

RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install --batch file:/tmp/phinder-${VERSION}.zip