package entorno;

import java.util.Random;
import objetos.ItemTipo;
import player.Jugador;

public class ZonaVolcanica extends Zona {
    private static final Random RNG = new Random();
    private static boolean planoEntregado = false;

    public ZonaVolcanica(){ super(1000, 1500); }

    @Override
    public void entrar(Jugador j){
        // acceso se valida al salir de la nave (Main)
    }

    @Override
    public void explorar(Jugador j){
        int z = j.getProfundidad();

        if (!planoEntregado && RNG.nextDouble() < 0.15){
            planoEntregado = true;
            j.agregar(ItemTipo.PLANO_NAVE, 1);
            j.setTienePlanos(true);
            System.out.println("[Volcánica] Hallaste PLANO_NAVE (único).");
        } else {
            ItemTipo[] rec = { ItemTipo.TITANIO, ItemTipo.SULFURO, ItemTipo.URANIO };
            ItemTipo drop = rec[RNG.nextInt(rec.length)];
            int cant = produccion(z, 1, 3);
            j.agregar(drop, cant);
            System.out.println("[Volcánica] Obtienes " + drop + " x" + cant);
        }

        if (RNG.nextDouble() < 0.20){
            j.vaciarInventario();
            System.out.println("[Volcánica] ¡Te desmayaste por el calor/presión! Pierdes el inventario.");
        }
    }

    @Override
    public void recolectaTipoRecurso(Jugador j, ItemTipo tipo){
        int z = j.getProfundidad();
        switch (tipo){
            case TITANIO, SULFURO, URANIO -> {
                int cant = produccion(z, 1, 3);
                j.agregar(tipo, cant);
                System.out.println("[Volcánica] Recolectaste " + tipo + " x" + cant);
                if (RNG.nextDouble() < 0.20){
                    j.vaciarInventario();
                    System.out.println("[Volcánica] ¡Te desmayaste! Pierdes el inventario.");
                }
            }
            default -> System.out.println("[Volcánica] Recurso no disponible aquí: " + tipo);
        }
    }
}

