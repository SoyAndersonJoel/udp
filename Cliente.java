package udp;

import javax.swing.*;
import java.awt.*;
import java.net.*;

public class Cliente extends JFrame {

    private JTextField idField;
    private JButton consultarButton;
    private JTextArea resultadoArea;

    private final String servidorIP = "192.168.204.1"; // Asegúrate de que esta IP sea correcta
    private final int puertoServidor = 5000;

    public Cliente() {
        setTitle("Consulta de Estudiante UDP");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana
        setLayout(new BorderLayout(10, 10));

        // COLORES Y FUENTES
        Color fondo = new Color(245, 250, 255);
        Color azulBoton = new Color(70, 130, 180);
        Font fuenteBase = new Font("Segoe UI", Font.PLAIN, 14);

        // Panel principal de entrada
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(fondo);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        JLabel titulo = new JLabel("Buscar Estudiante");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panelFormulario = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelFormulario.setBackground(fondo);

        JLabel idLabel = new JLabel("ID del estudiante:");
        idLabel.setFont(fuenteBase);
        idField = new JTextField(10);
        idField.setFont(fuenteBase);

        consultarButton = new JButton("Consultar");
        consultarButton.setFont(fuenteBase);
        consultarButton.setBackground(azulBoton);
        consultarButton.setForeground(Color.WHITE);

        panelFormulario.add(idLabel);
        panelFormulario.add(idField);
        panelFormulario.add(consultarButton);

        panelSuperior.add(titulo);
        panelSuperior.add(Box.createVerticalStrut(10));
        panelSuperior.add(panelFormulario);

        // Área de resultado
        resultadoArea = new JTextArea();
        resultadoArea.setFont(fuenteBase);
        resultadoArea.setEditable(false);
        resultadoArea.setBackground(new Color(255, 255, 255));
        resultadoArea.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        scrollPane.getViewport().setBackground(new Color(255, 255, 255));

        // Ventana
        getContentPane().setBackground(fondo);
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Acción del botón
        consultarButton.addActionListener(e -> consultarEstudiante());

        setVisible(true);
    }

    private void consultarEstudiante() {
        // Crear un nuevo hilo para realizar la consulta sin bloquear la interfaz gráfica
        new Thread(() -> {
            try {
                String idTexto = idField.getText().trim();
                if (idTexto.isEmpty()) {
                    resultadoArea.setText("Por favor ingrese un ID.");
                    return;
                }

                int id = Integer.parseInt(idTexto);
                DatagramSocket socket = new DatagramSocket();
                InetAddress direccion = InetAddress.getByName(servidorIP);

                byte[] mensaje = idTexto.getBytes();
                DatagramPacket paquete = new DatagramPacket(mensaje, mensaje.length, direccion, puertoServidor);
                socket.send(paquete);

                byte[] buffer = new byte[1024];
                DatagramPacket respuestaPaquete = new DatagramPacket(buffer, buffer.length);
                socket.receive(respuestaPaquete);

                String respuesta = new String(respuestaPaquete.getData(), 0, respuestaPaquete.getLength());
                // Actualizar la interfaz gráfica en el hilo principal
                SwingUtilities.invokeLater(() -> resultadoArea.setText(respuesta));

                socket.close();
            } catch (Exception ex) {
                // Actualizar la interfaz gráfica en el hilo principal
                SwingUtilities.invokeLater(() -> resultadoArea.setText("Error: " + ex.getMessage()));
            }
        }).start(); // Iniciar el hilo
    }

    public static void main(String[] args) {
        new Cliente();
    }
}
