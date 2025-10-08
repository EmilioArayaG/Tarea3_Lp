package entorno;

import player.Jugador;

public class NaveEstrellada extends Zona {
    public NaveEstrellada(){ super(0, 0); }

    @Override
    public void entrar(Jugador j){
        // TODO: sin consumo de O2; restricciones de acciones sin traje
    }

    @Override
    public void explorar(Jugador j){
        // TODO: sin O2; 25% MODULO_PROFUNDIDAD (único); con traje permite más loot (cables/piezas)
    }

    @Override
    public void recolectaTipoRecurso(Jugador j, objetos.ItemTipo tipo){
        // Normalmente se usa explorar aquí; este método puede quedar no-op o guiado a explorar.
    }
}
