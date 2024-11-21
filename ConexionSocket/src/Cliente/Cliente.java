package Cliente;

import java.io.*;
import java.net.*;

public class Cliente {

    public static void main(String[] args) {
        try (Socket socket = new Socket("172.16.40.172", 5555);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consola = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado al servidor.");

            while (true) {
                // Leer mensaje del servidor
                String mensajeServidor = reader.readLine();
                System.out.println(mensajeServidor);

                // Si el servidor espera entrada, enviarla
                if (mensajeServidor.contains("escribe un mensaje")) {
                    String mensajeCliente = consola.readLine();
                    writer.println(mensajeCliente);
                }
            }

        } catch (IOException e) {
            System.out.println("Error en el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
