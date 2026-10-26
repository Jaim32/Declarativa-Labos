% ====================================================================
% operaciones.pl - Motor de operaciones aritméticas en tuProlog (Labo-05.1)
% Las 4 operaciones básicas: Suma, Resta, Multiplicación y División
% ====================================================================

% 1. Reglas directas para las 4 operaciones con 2 operandos:
% Suma: suma(A, B, R)
suma(A, B, R) :-
    number(A), number(B), !,
    R is A + B.

% Resta: resta(A, B, R)
resta(A, B, R) :-
    number(A), number(B), !,
    R is A - B.

% Multiplicación: multiplicacion(A, B, R)
multiplicacion(A, B, R) :-
    number(A), number(B), !,
    R is A * B.

% División: division(A, B, R) - con guardia para evitar división entre cero
division(A, B, R) :-
    number(A), number(B),
    B =\= 0, !,
    R is A / B.

% 2. Predicado unificado operar(Operador, A, B, R)
operar('+', A, B, R) :- suma(A, B, R).
operar('-', A, B, R) :- resta(A, B, R).
operar('*', A, B, R) :- multiplicacion(A, B, R).
operar('/', A, B, R) :- division(A, B, R).

% 3. Evaluación declarativa de expresiones aritméticas compuestas:
% calcular(Expresion, R)
calcular(N, N) :-
    number(N), !.

calcular(A + B, R) :-
    calcular(A, RA),
    calcular(B, RB), !,
    R is RA + RB.

calcular(A - B, R) :-
    calcular(A, RA),
    calcular(B, RB), !,
    R is RA - RB.

calcular(A * B, R) :-
    calcular(A, RA),
    calcular(B, RB), !,
    R is RA * RB.

calcular(A / B, R) :-
    calcular(A, RA),
    calcular(B, RB),
    RB =\= 0, !,
    R is RA / RB.
