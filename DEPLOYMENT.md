🚀 SunWatcher Deployment Guide
Dieses Dokument beschreibt den Workflow für die lokale Entwicklung und das Server-Deployment.

💻 1. Lokale Entwicklung (Windows/WSL)
Erstmalige Einrichtung der Zertifikate
Nginx benötigt SSL-Zertifikate, um zu starten. Lokal nutzen wir selbstsignierte Zertifikate im Ordner certs_local/.

PowerShell

docker run --rm -v "${PWD}/certs_local:/certs" alpine sh -c "apk add --no-cache openssl && mkdir -p /certs/live/sauerburg.at /certs/live/fotografie-nina.at && openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /certs/live/sauerburg.at/privkey.pem -out /certs/live/sauerburg.at/fullchain.pem -subj '/CN=sauerburg.at' && openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /certs/live/fotografie-nina.at/privkey.pem -out /certs/live/fotografie-nina.at/fullchain.pem -subj '/CN=fotografie-nina.at'"
Projekt starten
PowerShell

docker-compose up -d
URLs für den Browser (nip.io)
Dank nip.io ist keine Änderung der hosts-Datei mehr nötig:

SunWatcher: https://sauerburg.at.127.0.0.1.nip.io:8443

Nina Fotografie: https://fotografie-nina.at.127.0.0.1.nip.io:8443

Tipp: Wenn Chrome "Dies ist keine sichere Verbindung" anzeigt, tippe einfach blind thisisunsafe auf der Tastatur.

🌐 2. Server Deployment (Linux)
Update einspielen
Bash

git pull
docker compose up -d --build
Nginx Konfiguration neu laden (ohne Neustart)
Bash

docker compose exec frontend nginx -s reload
Logs prüfen
Backend: docker compose logs -f backend

Frontend/Nginx: docker compose logs -f frontend

🔐 3. Konfiguration & Sicherheit
Environment-Variablen (.env)
Die Datei .env wird niemals eingecheckt. Sie muss auf jedem System manuell erstellt werden:

Lokal: Nutze SSL_PATH=./certs_local und Ports 8082/8443.

Server: Nutze SSL_PATH=/etc/letsencrypt und Ports 80/443.

Das MAIL_PASSWORD  und POSTGRES_PASSWORD werden hier zentral verwaltet.

CORS Einstellungen
Bei Problemen mit dem API-Zugriff prüfe die APP_CORS_ALLOWED_ORIGINS in der docker-compose.yml. Neue Domains müssen dort ohne Leerzeichen nach dem Komma hinzugefügt werden.

Datenbank-Passwort ändern (SQL)
Falls das Passwort im Container geändert werden muss, ohne das Volume zu löschen:

docker compose exec db psql -U postgres -d sundb

ALTER USER postgres WITH PASSWORD 'neues_passwort';

🛠 4. Fehlerbehebung
502 Bad Gateway: Meistens ist das Backend abgestürzt (DB-Verbindung prüfen via docker logs sun_backend).

Seite öffnet falsche Domain: Nginx Config prüfen und docker compose restart frontend ausführen.

HSTS-Sperre: In Chrome chrome://net-internals/#hsts aufrufen und die Domain unter "Delete domain security policies" löschen.