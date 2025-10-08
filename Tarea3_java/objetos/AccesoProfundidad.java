package objetos;

/** Propósito: Contrato para validar acceso por profundidad mínima. */
public interface AccesoProfundidad {
    /**
     * @param zMin profundidad mínima requerida
     * @return true si puede acceder
     */
    boolean puedeAcceder(int zMin);
}
