#!/bin/bash

# Ins Projektverzeichnis wechseln
PROJECT_DIR="/opt/sun-watcher"
cd $PROJECT_DIR || exit

echo "=========================================="
echo "🚀 SUN-WATCHER DEPLOYMENT START"
echo "=========================================="

# 1. Code-Update von GitHub
echo "📥 1. Aktualisiere Code von GitHub..."
git fetch origin
git reset --hard origin/main
git clean -fd  # Löscht alle Dateien, die nicht in Git sind

# 2. Docker-Container neu bauen
echo "🏗️ 2. Baue und starte Docker-Container..."
# --build erzwingt das Neuerstellen der Images
# -d startet im Hintergrund (detach)
docker compose up -d --build

# 3. Aufräumen (optional, spart Platz)
echo "🧹 3. Entferne ungenutzte Docker-Images (Cleanup)..."
docker image prune -f

echo "=========================================="
echo "✅ UPDATE ERFOLGREICH ABGESCHLOSSEN!"
echo "Status der Container:"
docker compose ps
echo "=========================================="