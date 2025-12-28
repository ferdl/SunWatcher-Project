# Stage 1: Build
FROM node:22-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npx ng build --configuration production

# Stage 2: Serve
FROM nginx:alpine
# Hier kopieren wir deine eigene Konfiguration hinein
COPY nginx.conf /etc/nginx/conf.d/default.conf
# Hier kopieren wir die Angular-Build-Dateien (Pfad aus angular.json)
COPY --from=build-stage /app/dist/sun-watcher-frontend/browser /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
