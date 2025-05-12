package udp;

import java.net.*;
import java.util.*;

public class Hilo extends Thread {
    private DatagramSocket socket;
    private DatagramPacket paquete;
    private List<Estudiante> estudiantes;

    public Hilo(DatagramSocket socket, DatagramPacket paquete, List<Estudiante> estudiantes) {
        this.socket = socket;
        this.paquete = paquete;
        this.estudiantes = estudiantes;
    }

    @Override
    public void run() {
        try {
            // Convertir el mensaje recibido en ID de estudiante
            String mensaje = new String(paquete.getData(), 0, paquete.getLength()).trim();
            int idBuscado = Integer.parseInt(mensaje);

            // Buscar el estudiante en la lista
            Estudiante estudiante = estudiantes.stream()
                    .filter(e -> e.getId() == idBuscado)
                    .findFirst()
                    .orElse(null);

            // Generar la respuesta
            String respuesta = (estudiante != null) ? estudiante.toString() : "Estudiante no encontrado";

            // Enviar la respuesta al cliente
            byte[] bufferRespuesta = respuesta.getBytes();
            InetAddress ipCliente = paquete.getAddress();
            int puertoCliente = paquete.getPort();
            DatagramPacket paqueteRespuesta = new DatagramPacket(bufferRespuesta, bufferRespuesta.length, ipCliente, puertoCliente);
            socket.send(paqueteRespuesta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
