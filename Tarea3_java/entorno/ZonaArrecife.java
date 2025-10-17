package entorno;

import java.util.Random;
import objetos.ItemTipo;
import player.Jugador;

public class ZonaArrecife extends Zona {
    private static final Random RNG = new Random();
    private static int stockPiezaTanque = 3;

    /*
     * crea la zona de arrecife, con un rango de profundidad entre 0 y 199 metros.
     * @param ninguno
     * @return
     */
    public ZonaArrecife(){ super(0, 199); }

    /*
     * define la accion que ocurre al entrar en la zona (en este caso, ninguna).
     * @param jugador: jugador - el jugador que entra a la zona.
     * @return void
     */
    @Override
    public void entrar(Jugador jugador){}

    /*
     * ejecuta la accion de explorar, con probabilidad de encontrar una pieza de tanque o recursos basicos.
     * @param j: jugador - el jugador que realiza la exploracion.
     * @return void
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

    /*
     * permite al jugador recolectar recursos basicos como cuarzo, silicio o cobre.
     * @param j: jugador - el jugador que recolecta.
     * @param tipo: itemtipo - el recurso que se intenta recolectar.
     * @return void
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




