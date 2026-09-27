% Regla 1 (Caso base)
suma(1, 1).

% Regla 2 (Caso recursivo)
suma(N, S) :- 
    N > 1, 
    N1 is N - 1, 
    suma(N1, S1), 
    S is N + S1.

/*
==========================================================
SALIDA ESPERADA EN CONSOLA CON TRACE
==========================================================

?- [suma].
true.

?- trace.
true.

[trace] ?- suma(3, R).
   Call: (11) suma(3, _17282) ? creep
   Fail: (11) suma(1, 1) [Regla 1 falla porque 3 \= 1]
   Call: (11) 3 > 1 ? creep
   Exit: (11) 3 > 1 ? creep
   Call: (11) _20670 is 3 - 1 ? creep
   Exit: (11) 2 is 3 - 1 ? creep
   Call: (11) suma(2, _20672) ? creep
   Fail: (11) suma(1, 1) [Regla 1 falla porque 2 \= 1]
   Call: (11) 2 > 1 ? creep
   Exit: (11) 2 > 1 ? creep
   Call: (11) _21890 is 2 - 1 ? creep
   Exit: (11) 1 is 2 - 1 ? creep
   Call: (11) suma(1, _21892) ? creep
   Exit: (11) suma(1, 1) [Regla 1 tiene exito] ? creep
   Call: (11) _20672 is 2 + 1 ? creep
   Exit: (11) 3 is 2 + 1 ? creep
   Exit: (11) suma(2, 3) ? creep
   Call: (11) _17282 is 3 + 3 ? creep
   Exit: (11) 6 is 3 + 3 ? creep
   Exit: (11) suma(3, 6) ? creep
R = 6.
*/