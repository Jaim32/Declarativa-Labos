#!/bin/bash
set -e
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$DIR"

mkdir -p target/classes

echo "[1/3] Compilando clases Java del Proyecto Final..."
javac -cp "lib/tuprolog-3.3.0.jar" -d target/classes $(find src/main/java -name "*.java")

echo "[2/3] Copiando base de conocimiento SWI-Prolog..."
cp -r src/main/resources/* target/classes/ 2>/dev/null || true

echo "[3/3] Iniciando Sistema Experto de Integrales..."
java -cp "lib/tuprolog-3.3.0.jar:target/classes" org.example.Main
