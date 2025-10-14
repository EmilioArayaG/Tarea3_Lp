package objetos;

public class Vehiculo implements AccesoProfundidad {
    @Override
    public boolean puedeAcceder(int requerido) {
        return true;
    }
}

