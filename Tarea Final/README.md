# Proyecto Final - Programación Declarativa (Ciclo 02/2026)

## Sistema Experto de Solución de Integrales y Cálculo de Área Bajo la Curva

Este sistema integra una **interfaz gráfica interactiva en Java Swing** con una **base de conocimiento experta en SWI-Prolog** para resolver integrales analíticas y calcular el área bajo la curva entre dos límites $[a, b]$ según la Regla de Barrow:

$$\int_a^b f(x) \, dx = F(b) - F(a)$$

---

## 🚀 Características Principales

1. **Resolución Simbólica Analítica**: Encuentra la función primitiva $F(x) + C$ en Prolog.
2. **Cálculo Numérico del Área**: Evalúa $F(b)$ y $F(a)$ para calcular con alta precisión el área bajo la curva.
3. **Las 22 Integrales Exigidas Implementadas** (Supera el mínimo de 12 solicitado en la guía):
   - **a)** $\int k \, dx$ (Constante numérica)
   - **b)** $\int x \, dx$
   - **c)** $\int (k * x) \, dx$
   - **d)** $\int x^n \, dx$ ($n$ entero, $n \neq -1$ y caso $x^{-1} \rightarrow \ln|x|$)
   - **e)** $\int u(x) \, dx$ (Reglas generales de integración)
   - **f)** $\int (k * u) \, dx$ (Constante por función)
   - **g)** $\int (u + v) \, dx$ (Linealidad: Suma)
   - **h)** $\int (u - v) \, dx$ (Linealidad: Resta)
   - **i)** $\int a^x \, dx = \frac{a^x}{\ln(a)}$ ($a > 0, a \neq 1$)
   - **j)** $\int a^u \, dx$ donde $u = k \cdot x$
   - **k)** $\int \sin(x) \, dx = -\cos(x)$
   - **l)** $\int \cos(x) \, dx = \sin(x)$
   - **m)** $\int \tan(x) \, dx = -\ln|\cos(x)|$
   - **n)** $\int \arcsin(x) \, dx = x \arcsin(x) + \sqrt{1 - x^2}$
   - **o)** $\int \arccos(x) \, dx = x \arccos(x) - \sqrt{1 - x^2}$
   - **p)** $\int \arctan(x) \, dx = x \arctan(x) - \frac{1}{2}\ln(1 + x^2)$
   - **q)** $\int \sinh(x) \, dx = \cosh(x)$
   - **r)** $\int \cosh(x) \, dx = \sinh(x)$
   - **s)** $\int \tanh(x) \, dx = \ln(\cosh(x))$
   - **t)** $\int \text{arcsinh}(x) \, dx = x \text{asinh}(x) - \sqrt{x^2 + 1}$
   - **u)** $\int \text{arccosh}(x) \, dx = x \text{acosh}(x) - \sqrt{x^2 - 1}$
   - **v)** $\int \text{arctanh}(x) \, dx = x \text{atanh}(x) + \frac{1}{2}\ln|1 - x^2|$
4. **Gráfico 2D Interactivo**:
   - Visualiza el plano cartesiano con ejes $X$ e $Y$.
   - Dibuja la curva continua $y = f(x)$.
   - **Sombrea el área bajo la curva** entre los límites $a$ y $b$ con líneas guía punteadas.
5. **Comunicación Nativa con SWI-Prolog**:
   - Conexión por `ProcessBuilder` que detecta automáticamente `swipl` en macOS (Apple Silicon e Intel), Linux y Windows.

---

## 📁 Estructura del Proyecto

```text
Tarea Final/
├── pom.xml                                    # Configuración Maven (Java 21)
├── run.sh                                     # Script de ejecución con 1 solo clic
├── README.md                                  # Este documento
├── lib/
│   └── tuprolog-3.3.0.jar                    # Biblioteca jar de respaldo
└── src/
    └── main/
        ├── resources/
        │   └── integrales.pl                  # Base de conocimiento experta en SWI-Prolog
        └── java/org/example/
            ├── Main.java                      # Punto de entrada de la aplicación
            ├── prolog/
            │   ├── PrologEngine.java          # Conector y evaluador con SWI-Prolog
            │   └── ResultadoIntegral.java     # Modelo de datos de la respuesta
            └── ui/
                ├── Theme.java                 # Paleta moderna Dark Mode
                ├── VentanaPrincipal.java      # Ventana principal e interacción
                └── components/
                    ├── Buttons.java           # Botones estilizados con hover
                    ├── TextField.java         # Inputs con placeholder
                    ├── PanelGrafico.java      # Lienzo de dibujo 2D del área y curva
                    └── PanelResultado.java    # Tarjeta de desglose matemático
```

---

## 💻 Cómo Ejecutar el Proyecto

### Opción 1: Desde la Terminal (Recomendado)
Dentro de la carpeta `Tarea Final`:
```bash
./run.sh
```

### Opción 2: Desde IntelliJ IDEA
1. Abre **IntelliJ IDEA**.
2. Ve a **File > Open...** y selecciona la carpeta:
   `Declarativa-Labos/Tarea Final`
3. IntelliJ detectará automáticamente el archivo `pom.xml`.
4. Abre `src/main/java/org/example/Main.java`.
5. Haz clic derecho y selecciona **Run 'Main.main()'**.

---

## 🧪 Pruebas Automatizadas en SWI-Prolog
Para ejecutar la batería de pruebas de las 22 integrales directamente en SWI-Prolog:
```bash
swipl -s src/main/resources/integrales.pl -g "test, halt."
```
