package com.compi.backend.riscv;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Buffer de salida que acumula el texto del programa y, además, avisa de cada
 * fragmento nuevo. Así el usuario ve los mensajes del programa mientras corre,
 * lo cual es imprescindible para responder a una lectura interactiva: la
 * interfaz debe mostrar la petición de datos antes de que el usuario escriba.
 */
final class StreamingOutputStream extends ByteArrayOutputStream {

    private final Consumer<String> listener;

    StreamingOutputStream(Consumer<String> listener) {
        this.listener = listener;
    }

    @Override
    public synchronized void write(int value) {
        super.write(value);
        emit(new byte[]{(byte) value}, 0, 1);
    }

    @Override
    public synchronized void write(byte[] bytes, int offset, int length) {
        super.write(bytes, offset, length);
        emit(bytes, offset, length);
    }

    private void emit(byte[] bytes, int offset, int length) {
        if (listener != null && length > 0) {
            listener.accept(new String(bytes, offset, length, StandardCharsets.UTF_8));
        }
    }

    String text() {
        return toString(StandardCharsets.UTF_8);
    }
}
