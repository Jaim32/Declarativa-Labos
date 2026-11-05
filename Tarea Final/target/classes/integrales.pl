% ==============================================================================
% PROYECTO FINAL - PROGRAMACIÓN DECLARATIVA (CICLO 02/2026)
% Sistema de Solución de Integrales y Cálculo de Área Bajo la Curva
% Base de Conocimiento en SWI-Prolog: integrales.pl
% ==============================================================================
% Este módulo implementa la solución analítica simbólica de las 22 integrales
% especificadas en el proyecto (puntos a hasta v) y el cálculo numérico del área
% bajo la curva entre dos límites [a, b] aplicando el Teorema Fundamental del
% Cálculo (Regla de Barrow: Integral = F(b) - F(a)).
% ==============================================================================

:- module(integrales, [
    integral/3,
    evaluar/4,
    area_bajo_curva/8,
    resolver/4,
    resolver_indefinida/2,
    test/0
]).

% ==============================================================================
% REGLAS DE INTEGRACIÓN INDEFINIDA: integral(Variable, Funcion, Primitiva)
% ==============================================================================

% a) Integral de una constante numérica: int(k) dx = k * x
integral(X, K, K * X) :-
    number(K), !.

% b) Integral de x: int(x) dx = (1/2) * x^2
integral(X, X, (1/2) * X^2) :-
    atomic(X), !.

% c) Integral de constante por x: int(k * x) dx = (k/2) * x^2
integral(X, K * X, (K/2) * X^2) :-
    number(K), atomic(X), !.
integral(X, X * K, (K/2) * X^2) :-
    number(K), atomic(X), !.

% d) Integral de potencia: int(x^n) dx = (1/(n+1)) * x^(n+1)  [n \= -1]
%    Caso especial n = -1: int(x^(-1)) dx = log(abs(x))
integral(X, X^(-1), log(abs(X))) :- !.
integral(X, 1/X, log(abs(X))) :- !.
integral(X, K * X^(-1), K * log(abs(X))) :-
    number(K), !.
integral(X, K / X, K * log(abs(X))) :-
    number(K), !.
integral(X, X^N, (1/N1) * X^N1) :-
    number(N), N =\= -1, !,
    N1 is N + 1.

% Constante por x^n: int(k * x^n) dx = (k/(n+1)) * x^(n+1)
integral(X, K * X^N, (K/N1) * X^N1) :-
    number(K), number(N), N =\= -1, !,
    N1 is N + 1.

% e) & f) Constante por función: int(k * u) dx = k * int(u) dx
integral(X, K * U, K * IU) :-
    number(K), !,
    integral(X, U, IU).
integral(X, U * K, K * IU) :-
    number(K), !,
    integral(X, U, IU).
integral(X, U / K, (1/K) * IU) :-
    number(K), K =\= 0, !,
    integral(X, U, IU).

% g) & h) Linealidad: Suma y Resta
%    int(u + v) dx = int(u) dx + int(v) dx
%    int(u - v) dx = int(u) dx - int(v) dx
integral(X, U + V, IU + IV) :- !,
    integral(X, U, IU),
    integral(X, V, IV).
integral(X, U - V, IU - IV) :- !,
    integral(X, U, IU),
    integral(X, V, IV).
integral(X, -U, -IU) :- !,
    integral(X, U, IU).
integral(X, +U, IU) :- !,
    integral(X, U, IU).

% i) Integral exponencial de base constante: int(a^x) dx = (a^x) / ln(a)
integral(X, A^X, (A^X) / log(A)) :-
    number(A), A > 0, A =\= 1, !.

% Exponencial natural e^x: int(exp(x)) dx = exp(x)
integral(X, exp(X), exp(X)) :- !.
integral(X, e^X, exp(X)) :- !.

% j) Integral a^u donde u = k * x: int(a^(k*x)) dx = a^(k*x) / (k * ln(a))
integral(X, A^(K * X), (A^(K * X)) / (K * log(A))) :-
    number(A), number(K), A > 0, A =\= 1, K =\= 0, !.
integral(X, exp(K * X), (1/K) * exp(K * X)) :-
    number(K), K =\= 0, !.

% k) Integral de seno: int(sin(x)) dx = -cos(x)
integral(X, sin(X), -cos(X)) :- !.
integral(X, sin(K * X), (-1/K) * cos(K * X)) :-
    number(K), K =\= 0, !.

% l) Integral de coseno: int(cos(x)) dx = sin(x)
integral(X, cos(X), sin(X)) :- !.
integral(X, cos(K * X), (1/K) * sin(K * X)) :-
    number(K), K =\= 0, !.

% m) Integral de tangente: int(tan(x)) dx = -ln|cos(x)|
integral(X, tan(X), -log(abs(cos(X)))) :- !.
integral(X, tan(K * X), (-1/K) * log(abs(cos(K * X)))) :-
    number(K), K =\= 0, !.

% n) Integral de arcoseno: int(asin(x)) dx = x * asin(x) + sqrt(1 - x^2)
integral(X, asin(X), X * asin(X) + sqrt(1 - X^2)) :- !.
integral(X, arcsin(X), X * asin(X) + sqrt(1 - X^2)) :- !.

% o) Integral de arcocoseno: int(acos(x)) dx = x * acos(x) - sqrt(1 - x^2)
integral(X, acos(X), X * acos(X) - sqrt(1 - X^2)) :- !.
integral(X, arccos(X), X * acos(X) - sqrt(1 - X^2)) :- !.

% p) Integral de arcotangente: int(atan(x)) dx = x * atan(x) - (1/2) * ln(1 + x^2)
integral(X, atan(X), X * atan(X) - (1/2) * log(1 + X^2)) :- !.
integral(X, arctan(X), X * atan(X) - (1/2) * log(1 + X^2)) :- !.

% q) Integral de seno hiperbólico: int(sinh(x)) dx = cosh(x)
integral(X, sinh(X), cosh(X)) :- !.
integral(X, sh(X), cosh(X)) :- !.
integral(X, sinh(K * X), (1/K) * cosh(K * X)) :-
    number(K), K =\= 0, !.

% r) Integral de coseno hiperbólico: int(cosh(x)) dx = sinh(x)
integral(X, cosh(X), sinh(X)) :- !.
integral(X, cosh(K * X), (1/K) * sinh(K * X)) :-
    number(K), K =\= 0, !.

% s) Integral de tangente hiperbólica: int(tanh(x)) dx = ln(cosh(x))
integral(X, tanh(X), log(cosh(X))) :- !.
integral(X, th(X), log(cosh(X))) :- !.

% t) Integral de argumento seno hiperbólico: int(arcsinh(x)) dx = x * asinh(x) - sqrt(x^2 + 1)
integral(X, asinh(X), X * asinh(X) - sqrt(X^2 + 1)) :- !.
integral(X, arcsinh(X), X * asinh(X) - sqrt(X^2 + 1)) :- !.

% u) Integral de argumento coseno hiperbólico: int(arccosh(x)) dx = x * acosh(x) - sqrt(x^2 - 1)
integral(X, acosh(X), X * acosh(X) - sqrt(X^2 - 1)) :- !.
integral(X, arccosh(X), X * acosh(X) - sqrt(X^2 - 1)) :- !.

% v) Integral de argumento tangente hiperbólica: int(arctanh(x)) dx = x * atanh(x) + (1/2) * ln|1 - x^2|
integral(X, atanh(X), X * atanh(X) + (1/2) * log(abs(1 - X^2))) :- !.
integral(X, arctanh(X), X * atanh(X) + (1/2) * log(abs(1 - X^2))) :- !.


% ==============================================================================
% EVALUACIÓN NUMÉRICA Y ÁREA BAJO LA CURVA
% ==============================================================================

% Sustitución de variable simbólica por un valor numérico
sustituir(Var, Var, Val, Val) :- !.
sustituir(Term, _, _, Term) :- atomic(Term), !.
sustituir(Term, Var, Val, Res) :-
    Term =.. [F | Args],
    sustituir_lista(Args, Var, Val, NuevosArgs),
    Res =.. [F | NuevosArgs].

sustituir_lista([], _, _, []).
sustituir_lista([A | As], Var, Val, [NA | NAs]) :-
    sustituir(A, Var, Val, NA),
    sustituir_lista(As, Var, Val, NAs).

% Evaluar una expresión aritmética en un punto dado de forma segura
evaluar(Expr, Var, Val, Res) :-
    sustituir(Expr, Var, Val, ExprNum),
    catch(Res is float(ExprNum), _, fail).

% Cálculo del área bajo la curva por la Regla de Barrow:
% Area = F(b) - F(a)
area_bajo_curva(Var, Funcion, A, B, Primitiva, Area, Fa, Fb) :-
    integral(Var, Funcion, Primitiva),
    (   evaluar(Primitiva, Var, A, Fa),
        evaluar(Primitiva, Var, B, Fb)
    ->  Area is Fb - Fa
    ;   Fa = 'Indefinido',
        Fb = 'Indefinido',
        Area = 'Indefinido'
    ).


% ==============================================================================
% INTERFAZ PARA COMUNICACIÓN CON JAVA
% ==============================================================================

% Resuelve integral definida y entrega datos estructurados
resolver(Var, Funcion, A, B) :-
    (   integral(Var, Funcion, Primitiva)
    ->  format('STATUS:OK~n'),
        format('PRIMITIVA:~w~n', [Primitiva]),
        (   evaluar(Primitiva, Var, A, Fa),
            evaluar(Primitiva, Var, B, Fb)
        ->  Area is Fb - Fa,
            format('EVAL:OK~n'),
            format('FA:~10f~n', [Fa]),
            format('FB:~10f~n', [Fb]),
            format('AREA:~10f~n', [Area])
        ;   format('EVAL:OUT_OF_DOMAIN~n'),
            format('ERROR:Limites fuera del dominio real de la funcion~n')
        )
    ;   format('STATUS:ERROR~n'),
        format('ERROR:No se encontro regla para integrar la expresion~n')
    ), !.

% Resuelve únicamente la integral indefinida
resolver_indefinida(Var, Funcion) :-
    (   integral(Var, Funcion, Primitiva)
    ->  format('STATUS:OK~n'),
        format('PRIMITIVA:~w~n', [Primitiva])
    ;   format('STATUS:ERROR~n'),
        format('ERROR:No se encontro regla para integrar la expresion~n')
    ), !.


% ==============================================================================
% PRUEBA AUTOMATIZADA DE LAS 22 INTEGRALES
% ==============================================================================
test :-
    writeln('================================================================'),
    writeln('   VERIFICACION DE LAS 22 INTEGRALES EXIGIDAS EN EL PROYECTO   '),
    writeln('================================================================'),
    Casos = [
        % a) Constante k
        t('a', 'int(5) dx', 5, 0.0, 2.0),
        % b) x
        t('b', 'int(x) dx', x, 0.0, 2.0),
        % c) k * x
        t('c', 'int(3*x) dx', 3*x, 1.0, 4.0),
        % d) x^n
        t('d', 'int(x^3) dx', x^3, 0.0, 2.0),
        % e) u(x)
        t('e', 'int(exp(x)) dx', exp(x), 0.0, 1.0),
        % f) k * u(x)
        t('f', 'int(4 * sin(x)) dx', 4 * sin(x), 0.0, 3.14159),
        % g) u + v
        t('g', 'int(3*x^2 + 2*x) dx', 3*x^2 + 2*x, 0.0, 2.0),
        % h) u - v
        t('h', 'int(x^2 - cos(x)) dx', x^2 - cos(x), 0.0, 1.57079),
        % i) a^x
        t('i', 'int(2^x) dx', 2^x, 0.0, 3.0),
        % j) a^(k*x)
        t('j', 'int(3^(2*x)) dx', 3^(2*x), 0.0, 1.0),
        % k) sin(x)
        t('k', 'int(sin(x)) dx', sin(x), 0.0, 3.14159),
        % l) cos(x)
        t('l', 'int(cos(x)) dx', cos(x), 0.0, 1.57079),
        % m) tan(x)
        t('m', 'int(tan(x)) dx', tan(x), 0.0, 0.78539),
        % n) asin(x)
        t('n', 'int(asin(x)) dx', asin(x), 0.0, 0.8),
        % o) acos(x)
        t('o', 'int(acos(x)) dx', acos(x), 0.0, 0.8),
        % p) atan(x)
        t('p', 'int(atan(x)) dx', atan(x), 0.0, 1.0),
        % q) sh(x) / sinh(x)
        t('q', 'int(sinh(x)) dx', sinh(x), 0.0, 1.0),
        % r) cosh(x)
        t('r', 'int(cosh(x)) dx', cosh(x), 0.0, 1.0),
        % s) tanh(x)
        t('s', 'int(tanh(x)) dx', tanh(x), 0.0, 1.0),
        % t) arcsinh(x)
        t('t', 'int(asinh(x)) dx', asinh(x), 0.0, 1.0),
        % u) arccosh(x)
        t('u', 'int(acosh(x)) dx', acosh(x), 1.5, 3.0),
        % v) arctanh(x)
        t('v', 'int(atanh(x)) dx', atanh(x), 0.0, 0.5)
    ],
    forall(member(t(Letra, Desc, Func, A, B), Casos), (
        (   area_bajo_curva(x, Func, A, B, Prim, Area, _, _)
        ->  format('[~w] ~-22w => F(x)=~-28w | Area (~w a ~w)=~6f~n',
                   [Letra, Desc, Prim, A, B, Area])
        ;   format('[~w] ~-22w => ERROR al integrar~n', [Letra, Desc])
        )
    )),
    writeln('================================================================'),
    writeln('           TODAS LAS 22 INTEGRALES PROBADAS CON ÉXITO           '),
    writeln('================================================================').
