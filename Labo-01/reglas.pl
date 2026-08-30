% REGLAS LÓGICAS E INFERENCIAS

% Carga el archivo de hechos
:- consult('hechos.pl').

% Regla 1: Determina si una zona es de alto riesgo en un momento específico.
% Propósito: Saber qué lugares evitar. Usa el conector OR (;) para evaluar 
% si el peligro es alto en ese momento específico o si la zona siempre es de peligro alto.
zona_peligrosa(Zona, Momento) :- 
    peligro(Zona, Momento, alto) ; 
    peligro(Zona, siempre, alto).

% Regla 2: Determina si un personaje es útil para establecer un campamento.
% Propósito: Filtrar quién puede ayudar a Eric a sobrevivir. Usa el conector AND (,)
% para asegurar que el personaje sea un aliado y además tenga la habilidad de construir.
util_para_campamento(Personaje) :- 
    aliado(Personaje), 
    habilidad(Personaje, construir).

% Regla 3: Identifica si una zona tiene una amenaza combinada (multiples tipos de enemigos).
% Propósito: Conocer las zonas mas complejas para el combate. Usa el conector AND (,) 
% y el operador de comparación de desigualdad (\=) para verificar que existan 
% al menos dos enemigos distintos que aparezcan en el mismo lugar.
zona_con_multiples_enemigos(Zona) :- 
    aparece_en(Enemigo1, Zona), 
    aparece_en(Enemigo2, Zona), 
    Enemigo1 \= Enemigo2.