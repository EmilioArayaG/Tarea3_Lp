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
    public void entrar(Jugador j){}

    @Override
    public void explorar(Jugador j){
        boolean tieneTraje = j.tieneTrajeTermico();

        if (!tieneTraje && accionUsadaSinTraje) {
            System.out.println("[Nave Estrellada] Sin traje termico solo puedes realizar una accion por visita.");
            return;
        }

        if (!moduloEntregado && RNG.nextDouble() < 0.25){
            moduloEntregado = true;
            j.agregar(ItemTipo.MODULO_PROFUNDIDAD, 1);
            System.out.println("[Nave Estrellada] Hallaste MODULO_PROFUNDIDAD (unico).");
            if (!tieneTraje) accionUsadaSinTraje = true;
            return;
        }

        ItemTipo drop = (RNG.nextBoolean()) ? ItemTipo.CABLES : ItemTipo.PIEZAS_METAL;
        j.agregar(drop, 1 + RNG.nextInt(2));
        System.out.println("[Nave Estrellada] Encontraste " + drop + ".");
        if (!tieneTraje) accionUsadaSinTraje = true;
    }

    @Override
    public void recolectaTipoRecurso(Jugador j, ItemTipo tipo) {
        boolean tieneTraje = j.tieneTrajeTermico();
        if (!tieneTraje && accionUsadaSinTraje) {
            System.out.println("[Nave Estrellada] Sin traje termico solo puedes realizar una accion por visita.");
            return;
        }
        if (tipo == ItemTipo.CABLES || tipo == ItemTipo.PIEZAS_METAL) {
            j.agregar(tipo, 1 + RNG.nextInt(2));
            System.out.println("[Nave Estrellada] Recolectas " + tipo + ".");
            if (!tieneTraje) accionUsadaSinTraje = true;
        } else {
            System.out.println("[Nave Estrellada] Aqui solo puedes obtener CABLES o PIEZAS_METAL.");
        }
    }
}


