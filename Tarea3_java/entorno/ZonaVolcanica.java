package entorno;

import java.util.Random;
import objetos.ItemTipo;
import player.Jugador;

public class ZonaVolcanica extends Zona {
    private static final Random RNG = new Random();
    private static boolean planoEntregado = false;

    public ZonaVolcanica(){ super(1000, 1500); }

    @Override
    public void entrar(Jugador j){}

    @Override
    public void explorar(Jugador j){
        int z = j.getProfundidad();
        int costo = costoExplorar(j, z);
        j.getOxigeno().consumirO2(costo);
        System.out.println("[Volcanica] Exploras a " + z + " m. O2 -" + costo);

        if (!planoEntregado && RNG.nextDouble() < 0.15){
            planoEntregado = true;
            j.agregar(ItemTipo.PLANO_NAVE, 1);
            j.setTienePlanos(true);
            System.out.println("[Volcanica] Hallaste PLANO_NAVE (unico).");
        } else {
            ItemTipo[] rec = { ItemTipo.TITANIO, ItemTipo.SULFURO, ItemTipo.URANIO };
            ItemTipo drop = rec[RNG.nextInt(rec.length)];
            int cant = produccion(z, 1, 3);
            j.agregar(drop, cant);
            System.out.println("[Volcanica] Obtienes " + drop + " x" + cant);
        }

        if (RNG.nextDouble() < 0.20){
            j.vaciarInventario();
            System.out.println("[Volcanica] Te desmayaste. Pierdes el inventario.");
        }
    }

    @Override
    public void recolectaTipoRecurso(Jugador j, ItemTipo tipo){
        int z = j.getProfundidad();
        switch (tipo){
            case TITANIO, SULFURO, URANIO -> {
                int costo = costoRecolectar(j, z);
                j.getOxigeno().consumirO2(costo);
                int cant = produccion(z, 1, 3);
                j.agregar(tipo, cant);
                System.out.println("[Volcanica] Recolectaste " + tipo + " x" + cant + " (O2 -" + costo + ")");
                if (RNG.nextDouble() < 0.20){
                    j.vaciarInventario();
                    System.out.println("[Volcanica] Te desmayaste. Pierdes el inventario.");
                }
            }
            default -> System.out.println("[Volcanica] Recurso no disponible aqui: " + tipo);
        }
    }
}


