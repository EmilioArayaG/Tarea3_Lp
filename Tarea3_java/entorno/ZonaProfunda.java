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
    public void entrar(Jugador j){}

    @Override
    public void explorar(Jugador j){
        int z = j.getProfundidad();
        int costo = costoExplorar(j, z);
        j.getOxigeno().consumirO2(costo);
        System.out.println("[Profunda] Exploras a " + z + " m. O2 -" + costo + (j.tieneMejoraTanque() ? " (sin presión)" : " (con presión)"));

        ItemTipo[] recursos = { ItemTipo.PLATA, ItemTipo.ORO, ItemTipo.ACERO, ItemTipo.DIAMANTE, ItemTipo.MAGNETITA };
        ItemTipo drop = recursos[RNG.nextInt(recursos.length)];
        int cantidad = produccion(z, 1, 3);
        j.agregar(drop, cantidad);
        System.out.println("Obtienes: " + drop + " x" + cantidad);
    }

    @Override
    public void recolectaTipoRecurso(Jugador j, ItemTipo tipo){
        switch (tipo){
            case PLATA, ORO, ACERO, DIAMANTE, MAGNETITA -> {
                int z = j.getProfundidad();
                int costo = costoRecolectar(j, z);
                j.getOxigeno().consumirO2(costo);
                int cantidad = produccion(z, 1, 3);
                j.agregar(tipo, cantidad);
                System.out.println("[Profunda] Recolectaste " + tipo + " x" + cantidad + " (O2 -" + costo + (j.tieneMejoraTanque() ? ", sin presión)" : ", con presión)"));
            }
            default -> System.out.println("[Profunda] Ese recurso no está disponible aquí: " + tipo);
        }
    }
}

