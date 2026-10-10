% Caso base: Si el número es menor a 10, la lista contiene solo ese dígito.
almacenar(N, [N]) :- 
    N < 10.

% Caso recursivo: Para números de dos o mas dígitos.
almacenar(N, [Digito|Resto]) :-
    N >= 10,
    Digito is N mod 10,       % Extrae el último dígito
    Siguiente is N // 10,     % Elimina el último dígito
    almacenar(Siguiente, Resto).
