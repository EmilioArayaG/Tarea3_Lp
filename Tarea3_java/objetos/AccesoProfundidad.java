package objetos;

public interface AccesoProfundidad {
    /*
     * verifica si una entidad (jugador o vehiculo) puede acceder a una profundidad especifica.
     * @param requerido: int - la profundidad que se desea alcanzar.
     * @return boolean: 'true' si el acceso es permitido, 'false' en caso contrario.
     */
    boolean puedeAcceder(int requerido);
}


