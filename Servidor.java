package udp;

import java.net.*;
import java.util.*;

public class Servidor {
    private static final int PUERTO = 5000;
    private static List<Estudiante> estudiantes;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PUERTO)) {
            System.out.println("Servidor UDP iniciado en puerto " + PUERTO);

            // Lista de estudiantes predefinidos
            estudiantes = new ArrayList<>();
            estudiantes.add(new Estudiante(1, "Anderson Vilatuña", "123456789", "Ingeniería", 3, true));
            estudiantes.add(new Estudiante(2, "Wilmer Vargas", "987654321", "Medicina", 5, false));
            estudiantes.add(new Estudiante(3, "Carlos López", "111222333", "Derecho", 2, true));
            estudiantes.add(new Estudiante(4, "Laura Ruiz", "444555666", "Arquitectura", 6, false));
            estudiantes.add(new Estudiante(5, "Pedro Martínez", "777888999", "Economía", 4, true));

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                socket.receive(paquete);  // Recibe la solicitud del cliente

                // Lanza un hilo para manejar la solicitud de este cliente
                Hilo hilo = new Hilo(socket, paquete, estudiantes);
                hilo.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
