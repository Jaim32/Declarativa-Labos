# Laboratorio 02 - Programacion declarativa

Encuentre el sujeto y predicado de las siguientes oraciones y conviértalas a su versión en lógica simbólica.

---

## 1) "Si la información no es completa para oferentes y demandantes, hay una falla de mercado."

Es una condicional con dos cláusulas:

| | Sujeto | Predicado |
|---|---|---|
| Antecedente | la información | no es completa para oferentes y demandantes |
| Consecuente | una falla de mercado | hay (existe) |

```
P = La información es completa para los oferentes
Q = La información es completa para los demandantes
R = Hay una falla de mercado
```

**¬(P ∧ Q) → R**

En Prolog:

```prolog
falla_de_mercado :- \+ (informacion_completa(oferentes), informacion_completa(demandantes)).
```

---

## 2) "He pasado todas mis vacaciones en Grecia y Marruecos."

Sujeto tácito: *yo*. Predicado: *he pasado todas mis vacaciones en Grecia y Marruecos*.

Es una conjunción (el predicado se reparte en dos destinos):

```
P = He pasado mis vacaciones en Grecia
Q = He pasado mis vacaciones en Marruecos
```

**P ∧ Q**

En Prolog:

```prolog
vacaciones(yo, grecia).
vacaciones(yo, marruecos).

todas_las_vacaciones :- vacaciones(yo, grecia), vacaciones(yo, marruecos).
```

---

## 3) "La realidad supera a la ficción y él no puede creerlo."

Dos oraciones unidas por conjunción:

| | Sujeto | Predicado |
|---|---|---|
| 1ª | la realidad | supera a la ficción |
| 2ª | él | no puede creerlo |

```
P = La realidad supera a la ficción
Q = Él puede creerlo
```

**P ∧ ¬Q**

En Prolog:

```prolog
supera(realidad, ficcion).

incredulo(X) :- supera(realidad, ficcion), \+ puede_creer(X, realidad).
```
