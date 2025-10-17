package entorno;

import java.util.Random;
import objetos.ItemTipo;
import player.Jugador;

public class NaveEstrellada extends Zona {
    private static final Random RNG = new Random();
    private static boolean moduloEntregado = false;
    private boolean accionUsadaSinTraje = false;

    /** crea nave estrellada (z fija 0) */
    public NaveEstrellada(){ super(0, 0); }

    /** resetea la restriccion de 1 accion sin traje por visita */
    public void resetAccionVisita(){ accionUsadaSinTraje = false; }

    /** no-op al entrar */
    @Override
    public void entrar(Jugador jugador){}

    /**
     * 25% modulo_profundidad (unico); respeta regla de 1 accion sin traje
     * @param j jugador
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

    /**
     * recolecta cables/piezas_metal; respeta regla de 1 accion sin traje
     * @param j jugador
     * @param tipo tipo de recurso
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



