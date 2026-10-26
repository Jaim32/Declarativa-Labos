#!/bin/bash
set -e
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$DIR"

mkdir -p target/classes
javac -cp "lib/tuprolog-3.3.0.jar" -d target/classes $(find src/main/java -name "*.java")
cp -r src/main/resources/* target/classes/ 2>/dev/null || true

echo "Iniciando Calculadora de Operaciones Básicas (Labo-05.1)..."
java -cp "lib/tuprolog-3.3.0.jar:target/classes" org.example.Main
