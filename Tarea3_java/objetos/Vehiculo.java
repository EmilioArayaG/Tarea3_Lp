package objetos;

public class Vehiculo implements AccesoProfundidad {
    /**
     * por defecto siempre permite acceso; subclases restringen
     * @param requerido profundidad requerida
     * @return true por defecto
     */
    @Override
    public boolean puedeAcceder(int requerido) {
        return true;
    }
}


