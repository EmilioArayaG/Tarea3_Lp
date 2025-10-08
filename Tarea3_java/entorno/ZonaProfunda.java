package entorno;

import player.Jugador;

public class ZonaProfunda extends Zona {
    public ZonaProfunda(){ super(200, 999); }

    @Override
    public void entrar(Jugador j){
        // TODO: validar acceso (a nado vs nave) y aplicar restricciones
    }

    @Override
    public void explorar(Jugador j){
        // TODO: Cexplorar(d) + presion(d) si no hay mejora de tanque; loot según reglas
    }

    @Override
    public void recolectaTipoRecurso(Jugador j, objetos.ItemTipo tipo){
        // TODO: Crecolectar(d) + presion(d) si no hay mejora de tanque; n(d) zona
    }
}
