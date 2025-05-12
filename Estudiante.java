package udp;

public class Estudiante {
    private int id;
    private String nombre;
    private String telefono;
    private String carrera;
    private int semestre;
    private boolean gratuidad;

    public Estudiante(int id, String nombre, String telefono, String carrera, int semestre, boolean gratuidad) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.carrera = carrera;
        this.semestre = semestre;
        this.gratuidad = gratuidad;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ID: " + id +
               ", Nombre: " + nombre +
               ", Teléfono: " + telefono +
               ", Carrera: " + carrera +
               ", Semestre: " + semestre +
               ", Gratuidad: " + (gratuidad ? "Sí" : "No");
    }
}
