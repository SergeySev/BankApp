FROM bellsoft/liberica-openjdk-alpine-musl:19

WORKDIR /app
# Копируем исходный код в контейнер ТОЛЬКО СКОМПИЛИРОВАННЫЙ
COPY target/TelProject-0.0.1-SNAPSHOT.jar app.jar

# Собираем и устанавливаем зависимости для React
#WORKDIR /app/frontend

RUN apk add --no-cache nodejs npm

COPY src/main/frontend /app/

RUN npm install &&  npm install --save react-dropzone && npm -S i axios

RUN npm install -g maildev --unsafe-perm

# Возвращаемся в корневую директорию
WORKDIR /app

# Запускаем приложение
CMD maildev & npm start & exec java -jar app.jar

