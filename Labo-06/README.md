# Laboratorio 06 - Programación Declarativa (Racket)

Implementación de la actividad evaluada correspondiente a la introducción al paradigma funcional con **Racket**.

---

## 📋 Enunciado de la Actividad

> *Suponga que el banco paga 3.5% para depósitos menores o iguales a $40,000, 4% para depósitos menores o iguales a $260,000 y 4.5% para depósitos mayores a $260,000. Escriba una función que reciba un depósito y devuelva el interés asignado al depósito.*

---

## 🧮 Lógica de Solución

La tasa de interés se asigna en tres tramos escalonados:

| Condición del Depósito ($D$) | Tasa de Interés | Interés Generado |
| :--- | :---: | :--- |
| $D \le \$40,000$ | **3.5%** ($0.035$) | $D \times 0.035$ |
| $\$40,000 < D \le \$260,000$ | **4.0%** ($0.040$) | $D \times 0.040$ |
| $D > \$260,000$ | **4.5%** ($0.045$) | $D \times 0.045$ |

### Implementación en Racket:
En Racket, la estructura idiomática para condicionales encadenados es `cond`:

```racket
(define (calcular-interes deposito)
  (cond
    [(< deposito 0) (error "El depósito no puede ser negativo")]
    [(<= deposito 40000) (* deposito 0.035)]
    [(<= deposito 260000) (* deposito 0.040)]
    [else (* deposito 0.045)]))
```

Debido a que `cond` evalúa sus ramas en orden secuencial:
1. Si `deposito <= 40000`, toma el primer caso ($3.5\%$).
2. Si la primera falla, entra a `deposito <= 260000` garantizando que el valor es estrictamente superior a $\$40,000$ e inferior o igual a $\$260,000$ ($4.0\%$).
3. Si ambas fallan, entra a `else` garantizando que es estrictamente superior a $\$260,000$ ($4.5\%$).

---

## 📁 Archivos del Proyecto

```text
Labo-06/
├── actividad.rkt    # Código fuente en Racket (#lang racket)
└── README.md        # Documentación y guía de ejecución
```

---

## 🚀 Cómo Ejecutar

### Opción 1: En DrRacket (Recomendado según la guía)
1. Abre **DrRacket**.
2. Ve a **File > Open...** y abre el archivo `actividad.rkt`.
3. Haz clic en el botón superior derecho **Run** (o presiona `Ctrl + R` / `Cmd + R`).
4. Verás las pruebas ejecutadas en la ventana de interacciones (panel inferior).
5. Puedes probar cualquier valor directamente en la consola interactiva:
   ```racket
   > (calcular-interes 50000)
   2000.0
   > (interes 30000)
   1050.0
   ```

### Opción 2: En Visual Studio Code
1. Abre la carpeta `Labo-06` en VS Code.
2. Asegúrate de tener instalada la extensión **Magic Racket** (como se sugiere en la página 2 de la guía).
3. Abre el archivo `actividad.rkt` y ejecuta el script con el botón de Run o mediante la terminal integrada:
   ```bash
   racket actividad.rkt
   ```

---

## 🧪 Casos de Prueba Verificados

| Caso | Depósito | Tasa Aplicada | Interés Calculado | Justificación |
| :---: | :---: | :---: | :---: | :--- |
| **1** | $\$20,000$ | $3.5\%$ | **$\$700.0$** | Dentro del tramo $\le 40,000$ |
| **2** | $\$40,000$ | $3.5\%$ | **$\$1,400.0$** | Límite exacto del tramo 1 |
| **3** | $\$100,000$ | $4.0\%$ | **$\$4,000.0$** | Dentro del tramo $(40,000, 260,000]$ |
| **4** | $\$260,000$ | $4.0\%$ | **$\$10,400.0$** | Límite exacto del tramo 2 |
| **5** | $\$300,000$ | $4.5\%$ | **$\$13,500.0$** | Superior a $\$260,000$ |
