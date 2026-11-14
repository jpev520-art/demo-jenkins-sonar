package com.ejemplo;

import java.util.HashMap;
import java.util.Map;

/** Inventario simple de productos: nombre -> cantidad en stock. */
public class Inventario {

    private final Map<String, Integer> stock = new HashMap<>();

    public void agregar(String producto, int cantidad) {
        validarCantidad(cantidad);
        stock.merge(producto, cantidad, Integer::sum);
    }

    public void retirar(String producto, int cantidad) {
        validarCantidad(cantidad);
        int disponible = cantidad(producto);
        if (cantidad > disponible) {
            throw new IllegalStateException("Stock insuficiente de " + producto
                    + ": hay " + disponible + ", se pidieron " + cantidad);
        }
        stock.put(producto, disponible - cantidad);
    }

    public int cantidad(String producto) {
        return stock.getOrDefault(producto, 0);
    }

    public boolean necesitaReposicion(String producto, int minimo) {
        return cantidad(producto) < minimo;
    }

    private void validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    }
}
