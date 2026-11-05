# Evaluación - Programación Declarativa (Carpeta EV)

Este directorio contiene la solución completa de los tres problemas en el archivo único exigido: [`evaluacion.pl`](file:///Users/jaime/Repo%20DECLA/Declarativa-Labos/Labo-05/EV/evaluacion.pl).

---

## 📄 Archivo para subir a Moodle
El archivo solicitado para entregar es:
👉 **`evaluacion.pl`**

---

## 🔍 Resumen y Pruebas de Cada Problema

### Problema 1 (20%): Recorrido de Árbol Binario (Suma de Pares e Impares)
* **Predicado:** `sumar_pares_impares(Arbol, SumaPares, SumaImpares)` (aridad 3).
* **Estructura del árbol:** Admite tanto `nodo(Valor, Izq, Der)` como `arbol(Valor, Izq, Der)`, hojas como `hoja(Valor)` y árbol vacío como `nil` o `vacio`.
* **Ejemplo de consulta:**
  ```prolog
  ?- Arbol = nodo(10, nodo(5, nodo(2, nil, nil), nodo(7, nil, nil)), nodo(20, nodo(15, nil, nil), nodo(30, nil, nil))),
     sumar_pares_impares(Arbol, Pares, Impares).
  % Pares = 62 (10 + 2 + 20 + 30)
  % Impares = 27 (5 + 7 + 15)
  ```

---

### Problema 2 (40%): Derivada Simbólica del Arco Tangente
* **Fórmula:**
  $$\frac{d}{dx}(\text{atan}(g(x))) = \frac{\frac{d}{dx}(g(x))}{1 + (g(x))^2}$$
  donde $g(x) = a_n x^n \pm a_{n-1} x^{n-1} \pm \dots \pm a_0$.
* **Predicado:** `d(Variable, Expresion, Derivada)`
* **Ejemplo de consulta:**
  ```prolog
  ?- d(x, atan(4*x^3 - 5*x^2 + 2*x - 7), Derivada).
  % Derivada = (12*x^2 - 10*x^1 + 2 - 0) / (1 + (4*x^3 - 5*x^2 + 2*x - 7)^2)
  ```

---

### Problema 3 (40%): Asignación de Asientos en la Barra
* **Miembros:** mamá, papá, abuelita (suegra de papá), Sarita (hija mayor), Karina (hija menor) y Jaimito.
* **Restricciones modeladas:**
  1. Mamá en la 3ª silla fija: `S3 = mama`.
  2. Papá al lado de mamá: `al_lado(papa, mama, Barra)` $\implies$ papá debe estar en la silla 4 (la 2 haría imposible que la abuela quede entre papá y su nieto).
  3. La abuela entre su yerno (papá) y su nietecito (Jaimito): `abuela` en silla 5 y `jaimito` en silla 6.
  4. Sarita al lado de Karina: ocupan las sillas 1 y 2.
* **Consultas:**
  ```prolog
  % Obtener todas las soluciones una por una:
  ?- asientos_barra(Barra).
  % Barra = [sarita, karina, mama, papa, abuela, jaimito] ;
  % Barra = [karina, sarita, mama, papa, abuela, jaimito].

  % O imprimir todas en pantalla con formato:
  ?- mostrar_todas_las_soluciones.
  ```

---

## ⚡ Ejecutar el script de pruebas automáticas
En la terminal dentro de esta carpeta:
```bash
./test.sh
```
