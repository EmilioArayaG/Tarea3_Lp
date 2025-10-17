package entorno;

import java.util.Random;
import objetos.ItemTipo;
import player.Jugador;

public class ZonaArrecife extends Zona {
    private static final Random RNG = new Random();
    private static int stockPiezaTanque = 3;

    /** crea arrecife [0..199] */
    public ZonaArrecife(){ super(0, 199); }

    /** no-op al entrar */
    @Override
    public void entrar(Jugador jugador){}

    /**
     * 30% pieza_tanque (stock 3); si no cae, recurso basico con n(d) 1..3
     * @param j jugador
     */
    @Override
    public void explorar(Jugador j){
        int z = j.getProfundidad();
        int c = costoExplorar(j, z);
        j.getOxigeno().consumirO2(c);
        System.out.println("[arrecife] exploras a " + z + " m. o2 -" + c);

        boolean cayoPieza = false;
        if (stockPiezaTanque > 0 && RNG.nextDouble() < 0.30){
            stockPiezaTanque--;
            j.agregar(ItemTipo.PIEZA_TANQUE, 1);
            cayoPieza = true;
            System.out.println("[arrecife] hallaste pieza_tanque. quedan " + stockPiezaTanque);
        }
        if (!cayoPieza){
            ItemTipo[] basicos = { ItemTipo.CUARZO, ItemTipo.SILICIO, ItemTipo.COBRE };
            ItemTipo drop = basicos[RNG.nextInt(basicos.length)];
            int cant = produccion(z, 1, 3);
            j.agregar(drop, cant);
            System.out.println("[arrecife] obtienes " + drop + " x" + cant);
        }
    }

    /**
     * recolecta cuarzo/silicio/cobre con n(d) 1..3
     * @param j jugador
     * @param tipo tipo de recurso
     */
    @Override
    public void recolectaTipoRecurso(Jugador j, ItemTipo tipo){
        int z = j.getProfundidad();
        switch (tipo){
            case CUARZO, SILICIO, COBRE -> {
                int costo = costoRecolectar(j, z);
                j.getOxigeno().consumirO2(costo);
                int cant = produccion(z, 1, 3);
                j.agregar(tipo, cant);
                System.out.println("[arrecife] recolectaste " + tipo + " x" + cant + " (o2 -" + costo + ")");
            }
            default -> System.out.println("[arrecife] recurso no disponible: " + tipo);
        }
    }
}




