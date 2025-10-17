package objetos;

public class Vehiculo implements AccesoProfundidad {
    /*
     * gestiona la transferencia de items entre el inventario del jugador y el almacen de la nave.
     * @param j: player.jugador - el jugador involucrado en la transferencia.
     * @param nave: objetos.naveexploradora - la nave involucrada.
     * @param tipo: objetos.itemtipo - el tipo de item a transferir.
     * @param cant: int - la cantidad a transferir.
     * @param hacianave: boolean - 'true' si es del jugador a la nave, 'false' si es de la nave al jugador.
     * @return void
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

    /*
     * define la capacidad de un vehiculo para acceder a una profundidad. por defecto, siempre puede.
     * @param requerido: int - la profundidad que se desea alcanzar.
     * @return boolean: siempre devuelve 'true' por defecto.
     */
    @Override
    public boolean puedeAcceder(int requerido) {
        return true;
    }
}


