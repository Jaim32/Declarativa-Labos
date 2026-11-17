# Proyecto Final - Programación Declarativa (Ciclo 02/2026)
### Departamento de Electrónica e Informática (DEI) — Universidad Centroamericana José Simeón Cañas (UCA)

---

## 👥 Integrantes del Equipo

| Nombre Completo | Carnet |
| :--- | :---: |
| **Jaime Alejandro Hernández Sandoval** | `00071322` |
| **Elías Alejandro Milián Rivera** | `00092822` |
| **José Alejandro Chávez Guardado** | `00149320` |

---

## 📖 Resumen y Explicación del Proyecto

Este proyecto consiste en un **Sistema Experto para la Solución de Integrales Simbólicas y Cálculo del Área Bajo la Curva**, integrando una **interfaz gráfica moderna en Java (Swing)** con un motor de inferencia lógica desarrollado en **SWI-Prolog**.

### 🧩 Arquitectura del Sistema
El sistema opera bajo una arquitectura desacoplada de dos capas:

```
┌──────────────────────────────────────────────────────────┐
│                   INTERFAZ JAVA SWING                    │
│  - Captura f(x) y límites [a, b]                         │
│  - Selector rápido con los 22 casos de la guía           │
│  - Renderizado 2D de la curva y sombreado del área       │
└────────────────────────────┬─────────────────────────────┘
                             │  Invoca subproceso nativo swipl
                             ▼  Query: resolver(x, f(x), a, b)
┌──────────────────────────────────────────────────────────┐
│             BASE DE CONOCIMIENTO SWI-PROLOG              │
│  (integrales.pl)                                         │
│  - Deducción simbólica de la antiderivada F(x)           │
│  - Regla de Barrow: Área = F(b) - F(a)                   │
│  - Manejo de dominios y simplificación analítica         │
└──────────────────────────────────────────────────────────┘
```

1. **Frontend (Java Swing - Modo Claro / Clean Design):**
   - Permite al usuario ingresar cualquier función $f(x)$ y los límites de integración $a$ y $b$, o seleccionar cualquiera de los casos base preconfigurados.
   - Cuenta con un **lienzo cartesiano 2D interactivo** (`PanelGrafico.java`) que dibuja la función, sitúa los ejes, traza los límites con líneas discontinuas y **sombrea el área bajo la curva en verde translúcido**.
   - Muestra el resultado desglosado: la primitiva simbólica $F(x) + C$, la evaluación $F(a)$, la evaluación $F(b)$, el valor final del área y el desglose de la Regla de Barrow:
     $$\int_a^b f(x) \, dx = F(b) - F(a)$$

2. **Base de Conocimiento (SWI-Prolog - `integrales.pl`):**
   - Desarrollada exclusivamente bajo el estándar de **SWI-Prolog** (módulo `integrales`).
   - Implementa de forma exhaustiva las **22 integrales especificadas en el proyecto (incisos a hasta v)**, superando el mínimo requerido de 12:
     - Constantes numéricas, potencias enteras ($x^n$) y fraccionarias.
     - Caso singular de exponente $-1$: $\int \frac{1}{x} \, dx = \ln|x|$.
     - Linealidad completa (suma, resta, constante por función).
     - Exponenciales de cualquier base constante ($a^x$, $e^x$, $a^{kx}$).
     - Funciones trigonométricas ($\sin, \cos, \tan$).
     - Funciones trigonométricas inversas ($\arcsin, \arccos, \arctan$).
     - Funciones hiperbólicas ($\sinh, \cosh, \tanh$).
     - Funciones hiperbólicas inversas ($\text{arcsinh}, \text{arccosh}, \text{arctanh}$).
   - Dispone de un evaluador numérico seguro que captura discontinuidades y valores fuera del dominio real, evitando excepciones no controladas.

---

## 📋 Catálogo de Integrales Soportadas (Casos a – v)

| Inciso | Expresión $f(x)$ | Primitiva $F(x)$ en Prolog | Límites Sugeridos $[a, b]$ |
| :---: | :--- | :--- | :---: |
| **a** | `k` (ej. `5`) | `k*x` | `[0, 2]` |
| **b** | `x` | `1/2*x^2` | `[0, 2]` |
| **c** | `k*x` (ej. `3*x`) | `(k/2)*x^2` | `[1, 4]` |
| **d** | `x^n` (ej. `x^3`) | `(1/(n+1))*x^(n+1)` | `[0, 2]` |
| **d\*** | `1/x` o `x^(-1)` | `log(abs(x))` | `[1, 2.718]` |
| **e** | `u(x)` (ej. `exp(x)`) | `exp(x)` | `[0, 1]` |
| **f** | `k*u(x)` (ej. `4*sin(x)`) | `k * int(u)` | `[0, 3.14159]` |
| **g** | `u + v` (ej. `3*x^2 + 2*x`) | `int(u) + int(v)` | `[0, 2]` |
| **h** | `u - v` (ej. `x^2 - cos(x)`) | `int(u) - int(v)` | `[0, 1.57079]` |
| **i** | `a^x` (ej. `2^x`) | `(a^x) / log(a)` | `[0, 3]` |
| **j** | `a^(k*x)` (ej. `3^(2*x)`) | `(a^(k*x)) / (k*log(a))` | `[0, 1]` |
| **k** | `sin(x)` | `-cos(x)` | `[0, 3.14159]` |
| **l** | `cos(x)` | `sin(x)` | `[0, 1.57079]` |
| **m** | `tan(x)` | `-log(abs(cos(x)))` | `[0, 0.78539]` |
| **n** | `asin(x)` | `x*asin(x) + sqrt(1 - x^2)` | `[0, 0.8]` |
| **o** | `acos(x)` | `x*acos(x) - sqrt(1 - x^2)` | `[0, 0.8]` |
| **p** | `atan(x)` | `x*atan(x) - (1/2)*log(1 + x^2)` | `[0, 1]` |
| **q** | `sinh(x)` o `sh(x)` | `cosh(x)` | `[0, 1]` |
| **r** | `cosh(x)` | `sinh(x)` | `[0, 1]` |
| **s** | `tanh(x)` | `log(cosh(x))` | `[0, 1]` |
| **t** | `asinh(x)` o `arcsinh(x)` | `x*asinh(x) - sqrt(x^2 + 1)` | `[0, 1]` |
| **u** | `acosh(x)` o `arccosh(x)` | `x*acosh(x) - sqrt(x^2 - 1)` | `[1.5, 3]` |
| **v** | `atanh(x)` o `arctanh(x)` | `x*atanh(x) + (1/2)*log(abs(1 - x^2))` | `[0, 0.5]` |

---

## 🛠️ Requisitos Previos

1. **Java JDK 21** o superior instalado.
2. **SWI-Prolog (`swipl`)** instalado y accesible en el sistema:
   * **macOS (Homebrew):** `brew install swi-prolog`
   * **Linux (Ubuntu/Debian):** `sudo apt install swi-prolog`
   * **Windows:** Descargar desde [swi-prolog.org](https://www.swi-prolog.org/) y agregar al PATH.

---

## 🚀 Cómo Ejecutar el Proyecto

### Opción 1: Desde la Terminal (Recomendado)
El proyecto incluye un script automatizado `run.sh` que compila el código Java, sincroniza la base de conocimiento y levanta la aplicación:

1. Abre tu terminal y navega hasta la carpeta del proyecto:
   ```bash
   cd "/Users/jaime/Repo DECLA/Declarativa-Labos/Tarea Final"
   ```
2. Otorga permisos de ejecución (si no los tiene) y corre el script:
   ```bash
   chmod +x run.sh
   ./run.sh
   ```

---

### Opción 2: Desde IntelliJ IDEA

1. Abre **IntelliJ IDEA**.
2. Ve a **File > Open...** (o pulsa **Open** en la pantalla de bienvenida).
3. Selecciona la carpeta:
   ```text
   /Users/jaime/Repo DECLA/Declarativa-Labos/Tarea Final
   ```
4. IntelliJ detectará automáticamente el archivo `pom.xml` como un proyecto Maven:
   * Verifica en **File > Project Structure > Project** que el SDK seleccionado sea **Java 21**.
5. En el panel de navegación de la izquierda (*Project Explorer*), dirígete a:
   ```text
   src/main/java/org/example/Main.java
   ```
6. Haz clic derecho sobre el archivo `Main.java` y selecciona **Run 'Main.main()'** (o pulsa el botón verde de *Play* al lado de `public static void main`).

---

### Opción 3: Pruebas Unitarias de Prolog en Terminal
Para verificar de forma aislada las 22 integrales y el cálculo de áreas directamente en el motor de SWI-Prolog:

```bash
cd "/Users/jaime/Repo DECLA/Declarativa-Labos/Tarea Final"
swipl -s src/main/resources/integrales.pl -g "test, halt."
```
Este comando ejecutará la suite completa y mostrará el estado de cada una de las 22 integrales en consola.
