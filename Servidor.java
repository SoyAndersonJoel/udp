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
            estudiantes.add(new Estudiante(2, "María Gómez", "987654321", "Medicina", 5, false));
            estudiantes.add(new Estudiante(3, "Carlos López", "111222333", "Derecho", 2, true));
            estudiantes.add(new Estudiante(4, "Laura Ruiz", "444555666", "Arquitectura", 6, false));
            estudiantes.add(new Estudiante(5, "Pedro Martínez", "777888999", "Economía", 4, true));

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                socket.receive(paquete);

                String recibido = new String(paquete.getData(), 0, paquete.getLength()).trim();
                int id = Integer.parseInt(recibido);

                // Crear un hilo para manejar la solicitud
                new Thread(() -> {
                    try {
                        String respuesta;
                        Estudiante estudiante = buscarEstudiante(id);
                        if (estudiante != null) {
                            respuesta = estudiante.toString();
                        } else {
                            respuesta = "Estudiante no encontrado";
                        }

                        byte[] respuestaBytes = respuesta.getBytes();
                        DatagramPacket respuestaPaquete = new DatagramPacket(
                            respuestaBytes, respuestaBytes.length, 
                            paquete.getAddress(), paquete.getPort());
                        socket.send(respuestaPaquete);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Estudiante buscarEstudiante(int id) {
        for (Estudiante e : estudiantes) {
            if (e.getId() == id) return e;
        }
        return null;
    }
}
