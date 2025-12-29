# Stufe 1: Bauen der Angular App
FROM node:22-alpine AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --configuration production

# Stufe 2: Ausliefern mit Nginx
FROM nginx:alpine
# Pfad anpassen: 'dist/nina-frontend/browser' ist Standard bei Angular 17+
COPY --from=build-stage /app/dist/nina-frontend/browser /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
