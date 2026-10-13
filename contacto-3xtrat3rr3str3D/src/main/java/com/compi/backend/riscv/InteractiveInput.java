package com.compi.backend.riscv;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Flujo de entrada del simulador interactivo. El hilo que simula se bloquea en
 * {@link #read()} hasta que la interfaz entrega una línea con {@link #submit};
 * {@link #close()} provoca el fin de la entrada (EOF) y desbloquea cualquier
 * lectura pendiente, de modo que el botón Detener también interrumpe una espera
 * de datos. Se entrega exactamente una línea por lectura, sin leer por delante.
 */
public final class InteractiveInput extends InputStream {

    private static final long POLL_MILLIS = 100;

    private final LinkedBlockingQueue<String> pending = new LinkedBlockingQueue<>();
    private byte[] current = new byte[0];
    private int position;
    private volatile boolean closed;

    /** Encola una línea; el programa la consumirá en su próxima lectura. */
    public void submit(String line) {
        if (!closed) {
            pending.offer(line == null ? "" : line);
        }
    }

    @Override
    public int read() throws IOException {
        while (position >= current.length) {
            String line = nextLine();
            if (line == null) {
                if (closed) {
                    return -1;
                }
                continue;
            }
            current = (line + "\n").getBytes(StandardCharsets.UTF_8);
            position = 0;
        }
        return current[position++] & 0xFF;
    }

    @Override
    public int read(byte[] buffer, int offset, int length) throws IOException {
        if (length == 0) {
            return 0;
        }
        int first = read();
        if (first == -1) {
            return -1;
        }
        buffer[offset] = (byte) first;
        int count = 1;
        // Solo se devuelve el resto de la línea ya disponible: si se esperara a
        // llenar el buffer, BufferedReader no vería el fin de línea hasta que el
        // usuario enviara otra, y la lectura quedaría bloqueada de más.
        while (count < length && position < current.length) {
            buffer[offset + count++] = (byte) (current[position++] & 0xFF);
        }
        return count;
    }

    private String nextLine() {
        try {
            return pending.poll(POLL_MILLIS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /** Señala el fin de la entrada: desbloquea las lecturas pendientes con EOF. */
    @Override
    public void close() {
        closed = true;
    }
}
