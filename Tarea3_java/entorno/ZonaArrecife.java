package entorno;

import java.util.Random;
import objetos.ItemTipo;
import player.Jugador;

public class ZonaArrecife extends Zona {
    private static final Random RNG = new Random();
    private static int stockPiezasTanque = 3;

    public ZonaArrecife(){ super(0, 199); }

    @Override
    public void entrar(Jugador j){}

    @Override
    public void explorar(Jugador j){
        int z = j.getProfundidad();
        int costo = costoExplorar(j, z);
        j.getOxigeno().consumirO2(costo);
        System.out.println("[Arrecife] Exploras a " + z + " m. O2 -" + costo);

        boolean caePieza = stockPiezasTanque > 0 && RNG.nextDouble() < 0.30;
        if (caePieza){
            stockPiezasTanque--;
            j.agregar(ItemTipo.PIEZA_TANQUE, 1);
            System.out.println("¡Encontraste PIEZA_TANQUE! (stock restante: " + stockPiezasTanque + ")");
            return;
        }

        ItemTipo[] basicos = { ItemTipo.CUARZO, ItemTipo.SILICIO, ItemTipo.COBRE };
        ItemTipo drop = basicos[RNG.nextInt(basicos.length)];
        int cantidad = produccion(z, 1, 3);
        j.agregar(drop, cantidad);
        System.out.println("Obtienes: " + drop + " x" + cantidad);
    }

    @Override
    public void recolectaTipoRecurso(Jugador j, ItemTipo tipo){
        switch (tipo){
            case CUARZO, SILICIO, COBRE -> {
                int z = j.getProfundidad();
                int costo = costoRecolectar(j, z);
                j.getOxigeno().consumirO2(costo);
                int cantidad = produccion(z, 1, 3);
                j.agregar(tipo, cantidad);
                System.out.println("[Arrecife] Recolectaste " + tipo + " x" + cantidad + " (O2 -" + costo + ")");
            }
            default -> System.out.println("[Arrecife] Ese recurso no está disponible aquí: " + tipo);
        }
    }
}


