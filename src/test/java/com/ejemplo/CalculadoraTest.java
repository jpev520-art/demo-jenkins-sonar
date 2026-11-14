package com.ejemplo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculadoraTest {

    private final Calculadora calc = new Calculadora();

    @Test
    void sumaDosNumeros() {
        assertEquals(5, calc.sumar(2, 3));
    }

    @Test
    void restaDosNumeros() {
        assertEquals(1, calc.restar(4, 3));
    }

    @Test
    void multiplicaDosNumeros() {
        assertEquals(12, calc.multiplicar(3, 4));
    }

    @Test
    void divideDosNumeros() {
        assertEquals(2, calc.dividir(8, 4));
    }

    @Test
    void dividirPorCeroLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> calc.dividir(4, 0));
    }
}
