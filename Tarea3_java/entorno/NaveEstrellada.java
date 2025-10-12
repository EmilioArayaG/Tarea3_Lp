package entorno;

import java.util.Random;
import objetos.ItemTipo;
import player.Jugador;

public class NaveEstrellada extends Zona {
    private static final Random RNG = new Random();

    private static boolean moduloEntregado = false;
    private boolean accionUsadaSinTraje = false;

    public NaveEstrellada(){ super(0, 0); }

    public void resetAccionVisita(){
        accionUsadaSinTraje = false;
    }

    @Override
    public void entrar(Jugador j){
        // sin costo de O2
    }

    @Override
    public void explorar(Jugador j){
        boolean tieneTraje = j.tieneTrajeTermico();

        if (!tieneTraje && accionUsadaSinTraje) {
            System.out.println("[Nave Estrellada] Sin traje térmico solo puedes realizar una acción por visita.");
            return;
        }

        if (!moduloEntregado && RNG.nextDouble() < 0.25){
            moduloEntregado = true;
            j.agregar(ItemTipo.MODULO_PROFUNDIDAD, 1);
            System.out.println("[Nave Estrellada] Hallaste MODULO_PROFUNDIDAD (único).");
            if (!tieneTraje) accionUsadaSinTraje = true;
            return;
        }

        if (tieneTraje) {
            ItemTipo drop = (RNG.nextBoolean()) ? ItemTipo.CABLES : ItemTipo.PIEZAS_METAL;
            j.agregar(drop, 1 + RNG.nextInt(2));
            System.out.println("[Nave Estrellada] Encontraste " + drop + ".");
        } else {
            System.out.println("[Nave Estrellada] Sin traje, recorres un pasillo pero no encuentras nada útil.");
            accionUsadaSinTraje = true;
        }
    }

    @Override
    public void recolectaTipoRecurso(Jugador jugador, ItemTipo tipo) {
        explorar(jugador);
    }
}

