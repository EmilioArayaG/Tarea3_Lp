package entorno;

import java.util.Random;
import objetos.ItemTipo;
import player.Jugador;

public class NaveEstrellada extends Zona {
    private static final Random RNG = new Random();
    private static boolean moduloEntregado = false;
    private boolean accionUsadaSinTraje = false;

    /*
     * crea la zona de la nave estrellada, con una profundidad fija en 0 metros.
     * @param ninguno
     * @return
     */
    public NaveEstrellada(){ super(0, 0); }

    /*
     * reinicia el contador de acciones para un jugador sin traje termico al entrar en la zona.
     * @param ninguno
     * @return void
     */
    public void resetAccionVisita(){ accionUsadaSinTraje = false; }

    /*
     * define la accion que ocurre al entrar en la zona (en este caso, ninguna).
     * @param jugador: jugador - el jugador que entra a la zona.
     * @return void
     */
    @Override
    public void entrar(Jugador jugador){}

    /*
     * ejecuta la accion de explorar dentro de la nave, con probabilidad de encontrar el modulo de profundidad.
     * @param j: jugador - el jugador que realiza la exploracion.
     * @return void
     */
    @Override
    public void explorar(Jugador j){
        boolean tieneTraje = j.tieneTrajeTermico();
        if (!tieneTraje && accionUsadaSinTraje){
            System.out.println("[estrellada] sin traje solo 1 accion por visita");
            return;
        }
        if (!moduloEntregado && RNG.nextDouble() < 0.25){
            moduloEntregado = true;
            j.agregar(ItemTipo.MODULO_PROFUNDIDAD, 1);
            System.out.println("[estrellada] hallaste modulo_profundidad (unico)");
            if (!tieneTraje) accionUsadaSinTraje = true;
            return;
        }
        ItemTipo drop = RNG.nextBoolean() ? ItemTipo.CABLES : ItemTipo.PIEZAS_METAL;
        j.agregar(drop, 1 + RNG.nextInt(2));
        System.out.println("[estrellada] encontraste " + drop);
        if (!tieneTraje) accionUsadaSinTraje = true;
    }

    /*
     * permite al jugador recolectar cables o piezas metalicas dentro de la nave.
     * @param j: jugador - el jugador que recolecta.
     * @param tipo: itemtipo - el recurso que se intenta recolectar.
     * @return void
     */
    @Override
    public void recolectaTipoRecurso(Jugador j, ItemTipo tipo) {
        boolean tieneTraje = j.tieneTrajeTermico();
        if (!tieneTraje && accionUsadaSinTraje) {
            System.out.println("[estrellada] sin traje solo 1 accion por visita");
            return;
        }
        if (tipo == ItemTipo.CABLES || tipo == ItemTipo.PIEZAS_METAL) {
            j.agregar(tipo, 1 + new Random().nextInt(2));
            System.out.println("[estrellada] recolectas " + tipo);
            if (!tieneTraje) accionUsadaSinTraje = true;
        } else {
            System.out.println("[estrellada] aqui solo cables o piezas_metal");
        }
    }
}



