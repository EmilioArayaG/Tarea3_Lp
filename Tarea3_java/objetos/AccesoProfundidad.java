package objetos;

public interface AccesoProfundidad {
    /**
     * indica si se puede acceder dada una profundidad requerida
     * @param requerido profundidad requerida
     * @return true si se puede acceder
     */
    boolean puedeAcceder(int requerido);
}


