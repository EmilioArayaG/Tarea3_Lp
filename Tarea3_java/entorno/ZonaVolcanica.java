package entorno;

import java.util.Random;
import objetos.ItemTipo;
import player.Jugador;

public class ZonaVolcanica extends Zona {
    private static final Random RNG = new Random();
    private static boolean planoEntregado = false;

    /*
     * crea la zona volcanica, con un rango de profundidad entre 1000 y 1500 metros.
     * @param ninguno
     * @return
     */
    public ZonaVolcanica(){ super(1000, 1500); }

    /*
     * define la accion que ocurre al entrar en la zona (en este caso, ninguna).
     * @param j: jugador - el jugador que entra a la zona.
     * @return void
     */
    @Override
    public void entrar(Jugador j){}

    /*
     * ejecuta la accion de explorar, con probabilidad de encontrar los planos de la nave o sufrir un desmayo.
     * @param j: jugador - el jugador que realiza la exploracion.
     * @return void
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

    /*
     * permite al jugador recolectar recursos exoticos como titanio, sulfuro o uranio.
     * @param j: jugador - el jugador que recolecta.
     * @param tipo: itemtipo - el recurso que se intenta recolectar.
     * @return void
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




