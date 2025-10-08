package entorno;

import player.Jugador;

public class ZonaVolcanica extends Zona {
    public ZonaVolcanica(){ super(1000, 1500); }

    @Override
    public void entrar(Jugador j){
        // TODO: requerir mejora de tanque + traje; validar si es en nave o a nado
    }

    @Override
    public void explorar(Jugador j){
        // TODO: acciones con riesgo (p.ej., 20% desmayo si sale sin robot), 15% PLANO_NAVE
    }

    @Override
    public void recolectaTipoRecurso(Jugador j, objetos.ItemTipo tipo){
        // TODO: n(d) con recursos volcánicos; costos y restricciones
    }
}
