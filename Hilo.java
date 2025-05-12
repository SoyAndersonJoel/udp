package udp;

import java.net.*;
import java.io.*;
import java.util.List;

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
            String recibido = new String(paquete.getData(), 0, paquete.getLength()).trim();
            int id = Integer.parseInt(recibido);

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
    }

    private Estudiante buscarEstudiante(int id) {
        for (Estudiante e : estudiantes) {
            if (e.getId() == id) return e;
        }
        return null;
    }
}
