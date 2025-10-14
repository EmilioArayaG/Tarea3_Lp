package entorno;

import java.util.Random;
import objetos.ItemTipo;
import player.Jugador;

public class ZonaArrecife extends Zona {
    private static final Random RNG = new Random();
    private static int stockPiezaTanque = 3;

    public ZonaArrecife(){ super(0, 199); }

    @Override
    public void entrar(Jugador jugador){}

    @Override
    public void explorar(Jugador j){
        int z = j.getProfundidad();
        int costo = costoExplorar(j, z);
        j.getOxigeno().consumirO2(costo);
        System.out.println("[Arrecife] Exploras a " + z + " m. O2 -" + costo);

        boolean cayoPieza = false;
        if (stockPiezaTanque > 0 && RNG.nextDouble() < 0.30){
            stockPiezaTanque--;
            j.agregar(ItemTipo.PIEZA_TANQUE, 1);
            System.out.println("[Arrecife] Obtienes PIEZA_TANQUE. Stock restante: " + stockPiezaTanque);
            cayoPieza = true;
        }
        if (!cayoPieza){
            ItemTipo[] basicos = { ItemTipo.CUARZO, ItemTipo.SILICIO, ItemTipo.COBRE };
            ItemTipo drop = basicos[RNG.nextInt(basicos.length)];
            int cant = produccion(z, 1, 3);
            j.agregar(drop, cant);
            System.out.println("[Arrecife] Obtienes " + drop + " x" + cant);
        }
    }

    @Override
    public void recolectaTipoRecurso(Jugador j, ItemTipo tipo){
        int z = j.getProfundidad();
        switch (tipo){
            case CUARZO, SILICIO, COBRE -> {
                int costo = costoRecolectar(j, z);
                j.getOxigeno().consumirO2(costo);
                int cant = produccion(z, 1, 3);
                j.agregar(tipo, cant);
                System.out.println("[Arrecife] Recolectaste " + tipo + " x" + cant + " (O2 -" + costo + ")");
            }
            default -> System.out.println("[Arrecife] Recurso no disponible aqui: " + tipo);
        }
    }
}



