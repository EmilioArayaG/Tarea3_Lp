package entorno;

import java.util.Random;
import objetos.ItemTipo;
import player.Jugador;

public class ZonaProfunda extends Zona {
    private static final Random RNG = new Random();

    public ZonaProfunda(){ super(200, 999); }

    @Override
    protected int presion(Jugador j, int z){
        if (j.tieneMejoraTanque()) return 0;
        double dd = d(z);
        return (int)Math.ceil(10 + 6*dd);
    }

    @Override
    public void entrar(Jugador jugador){}

    @Override
    public void explorar(Jugador j){
        int z = j.getProfundidad();
        int costo = costoExplorar(j, z);
        j.getOxigeno().consumirO2(costo);
        System.out.println("[Profunda] Exploras a " + z + " m. O2 -" + costo);

        ItemTipo[] pool = { ItemTipo.PLATA, ItemTipo.ORO, ItemTipo.ACERO, ItemTipo.DIAMANTE, ItemTipo.MAGNETITA };
        ItemTipo drop = pool[RNG.nextInt(pool.length)];
        int cant = produccion(z, 1, 3);
        j.agregar(drop, cant);
        System.out.println("[Profunda] Obtienes " + drop + " x" + cant);
    }

    @Override
    public void recolectaTipoRecurso(Jugador j, ItemTipo tipo){
        int z = j.getProfundidad();
        switch (tipo){
            case PLATA, ORO, ACERO, DIAMANTE, MAGNETITA -> {
                int costo = costoRecolectar(j, z);
                j.getOxigeno().consumirO2(costo);
                int cant = produccion(z, 1, 3);
                j.agregar(tipo, cant);
                System.out.println("[Profunda] Recolectaste " + tipo + " x" + cant + " (O2 -" + costo + ")");
            }
            default -> System.out.println("[Profunda] Recurso no disponible aqui: " + tipo);
        }
    }
}


