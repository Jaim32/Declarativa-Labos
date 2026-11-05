#!/bin/bash
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$DIR"

echo "=========================================================="
echo " Ejecutando pruebas de evaluacion.pl en SWI-Prolog"
echo "=========================================================="

/opt/homebrew/bin/swipl -s evaluacion.pl -g "
    writeln(''),
    writeln('>>> TEST PROBLEMA 1: Arbol Binario (Pares e Impares)'),
    Arbol = nodo(10, nodo(5, nodo(2, nil, nil), nodo(7, nil, nil)), nodo(20, nodo(15, nil, nil), nodo(30, nil, nil))),
    sumar_pares_impares(Arbol, Pares, Impares),
    format('Arbol con [10, 5, 2, 7, 20, 15, 30]~n'),
    format('  Suma Pares:   ~w (esperado: 10 + 2 + 20 + 30 = 62)~n', [Pares]),
    format('  Suma Impares: ~w (esperado: 5 + 7 + 15 = 27)~n~n', [Impares]),

    writeln('>>> TEST PROBLEMA 2: Derivada Simbolica de atan(g(x))'),
    Polinomio = 4*x^3 - 5*x^2 + 2*x - 7,
    d(x, atan(Polinomio), Derivada),
    format('f(x)  = atan(~w)~n', [Polinomio]),
    format('df/dx = ~w~n~n', [Derivada]),

    writeln('>>> TEST PROBLEMA 3: Asientos en la Barra'),
    mostrar_todas_las_soluciones,
    halt.
"
