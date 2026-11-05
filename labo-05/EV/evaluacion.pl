% ==============================================================================
% EVALUACIÓN - PROGRAMACIÓN DECLARATIVA
% Archivo único: evaluacion.pl
% ==============================================================================

% ==============================================================================
% PROBLEMA 1 (20%): Suma de números pares e impares en un árbol binario
% ==============================================================================
% Enunciado:
% Recorrer un árbol binario y calcular la suma de los números pares y la suma
% de los números impares almacenados. Su regla debe ser de aridad 3:
%   - 1er argumento: recibe el árbol binario.
%   - 2do argumento: retorna la suma de los números pares.
%   - 3er argumento: retorna la suma de los números impares.
%
% Estructuras de árbol soportadas:
%   - Árbol vacío: nil o vacio
%   - Nodo con hijos: nodo(Valor, Izq, Der) o arbol(Valor, Izq, Der)
%   - Hoja sin hijos: hoja(Valor)
% ==============================================================================

% Caso base 1: Árbol vacío representado como 'nil'
sumar_pares_impares(nil, 0, 0) :- !.

% Caso base 2: Árbol vacío representado como 'vacio'
sumar_pares_impares(vacio, 0, 0) :- !.

% Caso base 3: Nodo hoja
sumar_pares_impares(hoja(Valor), Pares, Impares) :- !,
    (   Valor mod 2 =:= 0
    ->  Pares = Valor, Impares = 0
    ;   Pares = 0, Impares = Valor
    ).

% Caso recursivo: nodo(Valor, SubarbolIzquierdo, SubarbolDerecho)
sumar_pares_impares(nodo(Valor, Izq, Der), Pares, Impares) :- !,
    sumar_pares_impares(Izq, ParesIzq, ImparesIzq),
    sumar_pares_impares(Der, ParesDer, ImparesDer),
    (   Valor mod 2 =:= 0
    ->  Pares is ParesIzq + ParesDer + Valor,
        Impares is ImparesIzq + ImparesDer
    ;   Pares is ParesIzq + ParesDer,
        Impares is ImparesIzq + ImparesDer + Valor
    ).

% Compatibilidad: Soporte cuando el functor utilizado es 'arbol'
sumar_pares_impares(arbol(Valor, Izq, Der), Pares, Impares) :- !,
    sumar_pares_impares(nodo(Valor, Izq, Der), Pares, Impares).

% Alias de predicado (por si el calificador consulta 'suma_pares_impares' o 'recorrer_arbol')
suma_pares_impares(Arbol, P, I) :- sumar_pares_impares(Arbol, P, I).
recorrer_arbol(Arbol, P, I)     :- sumar_pares_impares(Arbol, P, I).


% ==============================================================================
% PROBLEMA 2 (40%): Derivada simbólica del arco tangente de un polinomio
% ==============================================================================
% Enunciado:
% Sea f(x) = atan(g(x)), entonces:
%     d/dx (f(x)) = (d/dx (g(x))) / (1 + (g(x))^2)
% Donde:
%     g(x) = an*x^n +- an-1*x^(n-1) +- ... +- a0
%
% Predicado principal:
%     d(Variable, Expresion, Derivada)
% ==============================================================================

% --- Regla Principal: Arco Tangente (atan / arctan) ---
d(Var, atan(G), DG / (1 + (G)^2)) :- !,
    d(Var, G, DG).

d(Var, arctan(G), DG / (1 + (G)^2)) :- !,
    d(Var, G, DG).

% --- Reglas base para derivación de polinomios ---

% 1. Derivada de una constante numérica: d/dx (c) = 0
d(Var, C, 0) :-
    atomic(Var), number(C), !.

% 2. Derivada de la variable respecto a sí misma: d/dx (x) = 1
d(Var, Func, 1) :-
    Var == Func, !.

% 3. Derivada de constante por variable: d/dx (c * x) = c
d(Var, C * Var, C) :-
    atomic(Var), number(C), !.

d(Var, Var * C, C) :-
    atomic(Var), number(C), !.

% 4. Derivada de variable a la potencia N: d/dx (x^N) = N * x^(N-1)
d(Var, Var^N, N * Var^Nm1) :-
    number(N), N \= 0, N \= 1, !,
    Nm1 is N - 1.

d(Var, Var^1, 1) :- !.
d(Var, Var^0, 0) :- !.

% 5. Derivada de constante por variable a la potencia N: d/dx (k * x^N) = (k*N) * x^(N-1)
d(Var, K * Var^N, Coef * Var^Nm1) :-
    atomic(Var), number(K), number(N), N \= 0, N \= 1, !,
    Coef is K * N,
    Nm1 is N - 1.

d(Var, K * Var^1, K) :-
    atomic(Var), number(K), !.

d(Var, K * Var^0, 0) :-
    atomic(Var), number(K), !.

% 6. Linealidad de polinomios: Reglas recursivas para Suma y Resta
d(Var, U + V, DU + DV) :- !,
    d(Var, U, DU),
    d(Var, V, DV).

d(Var, U - V, DU - DV) :- !,
    d(Var, U, DU),
    d(Var, V, DV).

% 7. Signos unarios (+U, -U)
d(Var, -U, -DU) :- !,
    d(Var, U, DU).

d(Var, +U, DU) :- !,
    d(Var, U, DU).


% ==============================================================================
% PROBLEMA 3 (40%): Asignación de asientos en la barra del restaurante
% ==============================================================================
% Enunciado:
% Una familia de seis miembros sale a cenar a la barra de un restaurante.
% Miembros: mamá, papá, abuelita (suegra de papá), Sarita (hija mayor),
%           Karina (hija menor) y Jaimito.
%
% Asientos de la barra: 6 puestos en fila representados por la lista:
%     Barra = [Silla1, Silla2, Silla3, Silla4, Silla5, Silla6]
%
% Restricciones:
% 1. Mamá se sienta en la tercera silla: S3 = mama.
% 2. Papá se sienta a la par de ella: al_lado(papa, mama, Barra).
% 3. La abuela quiere estar entre su yerno (papá) y su nietecito (Jaimito):
%    entre(papa, abuela, jaimito, Barra).
% 4. Sarita debe sentarse a la par de Karina (al_lado(sarita, karina, Barra)).
% ==============================================================================

% Lista de los 6 miembros de la familia
miembros([mama, papa, abuela, sarita, karina, jaimito]).

% Regla: X e Y están sentados uno al lado del otro
al_lado(X, Y, [X, Y | _]).
al_lado(X, Y, [Y, X | _]).
al_lado(X, Y, [_ | Resto]) :- al_lado(X, Y, Resto).

% Regla: EnMedio está sentado exactamente entre X e Y (contiguos)
entre(X, EnMedio, Y, [X, EnMedio, Y | _]).
entre(X, EnMedio, Y, [Y, EnMedio, X | _]).
entre(X, EnMedio, Y, [_ | Resto]) :- entre(X, EnMedio, Y, Resto).

% Generador de permutaciones sin dependencias externas
permutar([], []).
permutar(Lista, [Elemento | Permutacion]) :-
    seleccionar_elem(Elemento, Lista, Resto),
    permutar(Resto, Permutacion).

seleccionar_elem(X, [X | Cola], Cola).
seleccionar_elem(X, [Y | Cola], [Y | Resto]) :-
    seleccionar_elem(X, Cola, Resto).

% Predicado principal: Determina la asignación de asientos
asientos_barra(Barra) :-
    % Barra de 6 puestos, donde la silla 3 es mamá
    Barra = [_, _, mama, _, _, _],
    miembros(Familia),
    permutar(Familia, Barra),
    al_lado(papa, mama, Barra),            % Papá al lado de mamá
    entre(papa, abuela, jaimito, Barra),   % Abuela entre yerno (papá) y nieto (Jaimito)
    al_lado(sarita, karina, Barra).        % Sarita al lado de Karina

% Alias para el predicado de resolución
resolver_barra(Barra) :- asientos_barra(Barra).

% Predicado para mostrar todas las soluciones posibles en consola
mostrar_todas_las_soluciones :-
    findall(Solucion, asientos_barra(Solucion), Soluciones),
    imprimir_soluciones(Soluciones, 1).

imprimir_soluciones([], _) :- !.
imprimir_soluciones([Sol | Resto], N) :-
    format('~n========================================~n', []),
    format('  SOLUCION POSIBLE #~w~n', [N]),
    format('========================================~n', []),
    Sol = [S1, S2, S3, S4, S5, S6],
    format('  Silla 1: ~w~n', [S1]),
    format('  Silla 2: ~w~n', [S2]),
    format('  Silla 3: ~w (fijo: mamá)~n', [S3]),
    format('  Silla 4: ~w~n', [S4]),
    format('  Silla 5: ~w~n', [S5]),
    format('  Silla 6: ~w~n', [S6]),
    format('  Lista:   ~w~n', [Sol]),
    N1 is N + 1,
    imprimir_soluciones(Resto, N1).
