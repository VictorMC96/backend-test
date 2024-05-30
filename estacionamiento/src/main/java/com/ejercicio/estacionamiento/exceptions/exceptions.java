package com.ejercicio.estacionamiento.exceptions;

public class exceptions {

    public static class PlacaNoEncontradaException extends RuntimeException {
        public PlacaNoEncontradaException(String message) {
            super(message);
        }
    }

}
