package objetos;

public class Vehiculo implements AccesoProfundidad {
    /**
     * mueve objetos entre jugador y nave
     * @param j jugador
     * @param nave nave
     * @param tipo tipo
     * @param cant cantidad
     * @param haciaNave true para jugador->nave, false para nave->jugador
     */
    public void transferirObjetos(player.Jugador j, objetos.NaveExploradora nave,
                                  objetos.ItemTipo tipo, int cant, boolean haciaNave){
        if (cant <= 0) return;
        if (haciaNave){
            if (j.gastar(tipo, cant)){
                nave.depositar(tipo, cant);
            }
        } else {
            nave.transferirAJugador(j, tipo, cant);
        }
    }

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


