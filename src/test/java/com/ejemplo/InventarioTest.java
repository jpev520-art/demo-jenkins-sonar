package com.ejemplo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventarioTest {

    private Inventario inventario;

    @BeforeEach
    void setUp() {
        inventario = new Inventario();
    }

    @Test
    void productoNuevoTieneStockCero() {
        assertEquals(0, inventario.cantidad("vacuna"));
    }

    @Test
    void agregarAcumulaCantidades() {
        inventario.agregar("vacuna", 10);
        inventario.agregar("vacuna", 5);
        assertEquals(15, inventario.cantidad("vacuna"));
    }

    @Test
    void retirarDescuentaDelStock() {
        inventario.agregar("jeringa", 20);
        inventario.retirar("jeringa", 8);
        assertEquals(12, inventario.cantidad("jeringa"));
    }

    @Test
    void retirarMasDeLoDisponibleFalla() {
        inventario.agregar("jeringa", 3);
        assertThrows(IllegalStateException.class, () -> inventario.retirar("jeringa", 5));
    }

    @Test
    void cantidadNegativaOCeroNoSePermite() {
        assertThrows(IllegalArgumentException.class, () -> inventario.agregar("guantes", 0));
        assertThrows(IllegalArgumentException.class, () -> inventario.retirar("guantes", -1));
    }

    @Test
    void detectaCuandoHayQueReponer() {
        inventario.agregar("guantes", 4);
        assertTrue(inventario.necesitaReposicion("guantes", 5));
        assertFalse(inventario.necesitaReposicion("guantes", 4));
    }
}
