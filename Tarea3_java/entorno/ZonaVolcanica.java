package entorno;

import java.util.Random;
import objetos.ItemTipo;
import player.Jugador;

public class ZonaVolcanica extends Zona {
    private static final Random RNG = new Random();
    private static boolean planoEntregado = false;

    /** crea volcanica [1000..1500] */
    public ZonaVolcanica(){ super(1000, 1500); }

    /** no-op al entrar */
    @Override
    public void entrar(Jugador j){}

    /**
     * 15% plano_nave (unico) y si no, titanio/sulfuro/uranio con n(d) 3..8; 20% desmayo
     * @param j jugador
     */
    @Override
    public void explorar(Jugador j){
        int z = j.getProfundidad();
        int costo = costoExplorar(j, z);
        j.getOxigeno().consumirO2(costo);
        System.out.println("[volcanica] exploras a " + z + " m. o2 -" + costo);

        if (!planoEntregado && RNG.nextDouble() < 0.15){
            planoEntregado = true;
            j.agregar(ItemTipo.PLANO_NAVE, 1);
            j.setTienePlanos(true);
            System.out.println("[volcanica] hallaste plano_nave (unico)");
        } else {
            ItemTipo[] rec = { ItemTipo.TITANIO, ItemTipo.SULFURO, ItemTipo.URANIO };
            ItemTipo drop = rec[RNG.nextInt(rec.length)];
            int cant = produccion(z, 3, 8);
            j.agregar(drop, cant);
            System.out.println("[volcanica] obtienes " + drop + " x" + cant);
        }

        if (RNG.nextDouble() < 0.20){
            j.vaciarInventario();
            System.out.println("[volcanica] te desmayaste, pierdes inventario");
        }
    }

    /**
     * recolecta titanio/sulfuro/uranio con n(d) 3..8 y 20% desmayo
     * @param j jugador
     * @param tipo tipo de recurso
     */
    @Override
    public void recolectaTipoRecurso(Jugador j, ItemTipo tipo){
        int z = j.getProfundidad();
        switch (tipo){
            case TITANIO, SULFURO, URANIO -> {
                int costo = costoRecolectar(j, z);
                j.getOxigeno().consumirO2(costo);
                int cant = produccion(z, 3, 8);
                j.agregar(tipo, cant);
                System.out.println("[volcanica] recolectaste " + tipo + " x" + cant + " (o2 -" + costo + ")");
                if (RNG.nextDouble() < 0.20){
                    j.vaciarInventario();
                    System.out.println("[volcanica] te desmayaste, pierdes inventario");
                }
            }
            default -> System.out.println("[volcanica] recurso no disponible: " + tipo);
        }
    }
}



