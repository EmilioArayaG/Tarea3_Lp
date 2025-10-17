package entorno;

import java.util.Random;
import objetos.ItemTipo;
import player.Jugador;

public class ZonaProfunda extends Zona {
    private int presionBase = 10;
    private static final Random RNG = new Random();

    /** crea profunda [200..999] */
    public ZonaProfunda(){ super(200, 999); }

    /** fija presion base (default 10) */
    public void setPresionBase(int p){ this.presionBase = p; }
    /** @return presion base */
    public int getPresionBase(){ return presionBase; }

    /**
     * presion efectiva: 0 si mejora de tanque, si no ceil(presionBase + 6d)
     */
    @Override
    protected int presion(Jugador j, int z){
        if (j.tieneMejoraTanque()) return 0;
        return (int)Math.ceil(presionBase + 6*d(z));
    }

    /** no-op al entrar */
    @Override
    public void entrar(Jugador jugador){}

    /**
     * explora y entrega un metal con n(d) 2..6
     * @param j jugador
     */
    @Override
    public void explorar(Jugador j){
        int z = j.getProfundidad();
        int costo = costoExplorar(j, z);
        j.getOxigeno().consumirO2(costo);
        System.out.println("[profunda] exploras a " + z + " m. o2 -" + costo);

        ItemTipo[] pool = { ItemTipo.PLATA, ItemTipo.ORO, ItemTipo.ACERO, ItemTipo.DIAMANTE, ItemTipo.MAGNETITA };
        ItemTipo drop = pool[RNG.nextInt(pool.length)];
        int cant = produccion(z, 2, 6);
        j.agregar(drop, cant);
        System.out.println("[profunda] obtienes " + drop + " x" + cant);
    }

    /**
     * recolecta metales con n(d) 2..6
     * @param j jugador
     * @param tipo tipo de recurso
     */
    @Override
    public void recolectaTipoRecurso(Jugador j, ItemTipo tipo){
        int z = j.getProfundidad();
        switch (tipo){
            case PLATA, ORO, ACERO, DIAMANTE, MAGNETITA -> {
                int costo = costoRecolectar(j, z);
                j.getOxigeno().consumirO2(costo);
                int cant = produccion(z, 2, 6);
                j.agregar(tipo, cant);
                System.out.println("[profunda] recolectaste " + tipo + " x" + cant + " (o2 -" + costo + ")");
            }
            default -> System.out.println("[profunda] recurso no disponible: " + tipo);
        }
    }
}



