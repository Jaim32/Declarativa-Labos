#lang racket

;; ====================================================================
;; Laboratorio 06 - Programación Declarativa (Paradigma Funcional)
;; Lenguaje: Racket
;; Archivo: actividad.rkt
;; ====================================================================
;;
;; Enunciado:
;; Suponga que el banco paga 3.5% para depósitos menores o iguales a $40,000,
;; 4% para depósitos menores o iguales a $260,000 y 4.5% para depósitos
;; mayores a $260,000. Escriba una función que reciba un depósito y
;; devuelva el interés asignado al depósito.
;; ====================================================================

;; --------------------------------------------------------------------
;; Función: tasa-interes
;; Entrada: deposito (número real positivo)
;; Salida:  tasa decimal correspondiente (0.035, 0.040 o 0.045)
;; --------------------------------------------------------------------
(define (tasa-interes deposito)
  (cond
    [(< deposito 0) (error "El depósito no puede ser negativo")]
    [(<= deposito 40000) 0.035]
    [(<= deposito 260000) 0.040]
    [else 0.045]))

;; --------------------------------------------------------------------
;; Función: calcular-interes
;; Entrada: deposito (número real positivo)
;; Salida:  monto monetario del interés generado (deposito * tasa)
;; --------------------------------------------------------------------
(define (calcular-interes deposito)
  (cond
    [(< deposito 0) (error "El depósito no puede ser negativo")]
    [(<= deposito 40000) (* deposito 0.035)]
    [(<= deposito 260000) (* deposito 0.040)]
    [else (* deposito 0.045)]))

;; Alias corto para invocar la función con el nombre directo:
(define (interes deposito)
  (calcular-interes deposito))

;; --------------------------------------------------------------------
;; Pruebas de verificación en consola
;; --------------------------------------------------------------------
(displayln "==========================================================")
(displayln "  LABORATORIO 06: CÁLCULO DE INTERÉS DE DEPÓSITOS")
(displayln "==========================================================")

(define (imprimir-caso deposito)
  (define tasa (* (tasa-interes deposito) 100))
  (define int-calculado (calcular-interes deposito))
  (printf "Depósito: $~a  -->  Tasa: ~a%  -->  Interés: $~a\n"
          deposito tasa int-calculado))

;; Casos de prueba que cubren todos los tramos y valores frontera:
(imprimir-caso 20000)    ; Tramo 1: menor a $40,000 (3.5%)
(imprimir-caso 40000)    ; Límite exacto del Tramo 1 (3.5%)
(imprimir-caso 100000)   ; Tramo 2: entre $40,000 y $260,000 (4.0%)
(imprimir-caso 260000)   ; Límite exacto del Tramo 2 (4.0%)
(imprimir-caso 300000)   ; Tramo 3: mayor a $260,000 (4.5%)
(imprimir-caso 500000)   ; Tramo 3: mayor a $260,000 (4.5%)

(displayln "==========================================================")
