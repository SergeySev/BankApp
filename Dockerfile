FROM bellsoft/liberica-openjdk-alpine-musl:19

WORKDIR /app
COPY target/TelProject-0.0.1-SNAPSHOT.jar app.jar

RUN apk add --no-cache nodejs npm

COPY src/main/frontend /app/

RUN npm install &&  npm install --save react-dropzone && npm -S i axios

RUN npm install -g maildev --unsafe-perm

WORKDIR /app

CMD maildev & npm start & exec java -jar app.jar

