% BASE DE CONOCIMIENTOS - HECHOS

% Personajes
personaje(eric).
personaje(timmy).
personaje(kelvin).
personaje(virginia).

% Características de Eric
protagonista(eric).
edad(eric, 30).
tiene(eric, hacha).
tiene(eric, encendedor).
necesita(eric, refugio).
necesita(eric, comida).
necesita(eric, agua).

% Características de Kelvin
aliado(kelvin).
estado(kelvin, capturado).
habla(kelvin, no).
habilidad(kelvin, cargar_troncos).
habilidad(kelvin, construir).

% Características de Virginia
mutante(virginia).
puede_ser_aliada(virginia).

% Zonas
zona(superficie).
zona(cuevas).
zona(bunkeres).

% Enemigos y apariciones
enemigo(canibal).
enemigo(mutante).
aparece_en(canibal, superficie).
aparece_en(mutante, superficie).
aparece_en(mutante, cuevas).

% Propiedades de los búnkeres
tiene_enemigos(bunkeres, no).
requiere(bunkeres, llaves).

% Niveles de peligro (Zona, Momento, Nivel)
peligro(cuevas, siempre, alto).
peligro(superficie, dia, medio).
peligro(superficie, noche, alto).

% Materiales
material(troncos).
material(piedras).
encuentra_en(troncos, superficie).
encuentra_en(piedras, superficie).