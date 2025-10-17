package entorno;

import java.util.Random;
import objetos.ItemTipo;
import player.Jugador;

public class ZonaProfunda extends Zona {
    private int presionBase = 10;
    private static final Random RNG = new Random();

    /*
     * crea la zona profunda, con un rango de profundidad entre 200 y 999 metros.
     * @param ninguno
     * @return
     */
    public ZonaProfunda(){ super(200, 999); }

    /*
     * establece el valor de la presion base que afecta al jugador en esta zona.
     * @param p: int - el nuevo valor de la presion base.
     * @return void
     */
    public void setPresionBase(int p){ this.presionBase = p; }

    /*
     * obtiene el valor de la presion base de la zona.
     * @param ninguno
     * @return int: el valor de la presion base.
     */
    public int getPresionBase(){ return presionBase; }

    /*
     * calcula la penalizacion de oxigeno por presion, que se anula si el jugador tiene la mejora de tanque.
     * @param j: jugador - el jugador, para verificar sus mejoras.
     * @param z: int - la profundidad actual.
     * @return int: el costo de oxigeno adicional por la presion.
     */
    @Override
    protected int presion(Jugador j, int z){
        if (j.tieneMejoraTanque()) return 0;
        return (int)Math.ceil(presionBase + 6*d(z));
    }

    /*
     * define la accion que ocurre al entrar en la zona (en este caso, ninguna).
     * @param jugador: jugador - el jugador que entra a la zona.
     * @return void
     */
    @Override
    public void entrar(Jugador jugador){}

    /*
     * ejecuta la accion de explorar, entregando al jugador un recurso metalico aleatorio.
     * @param j: jugador - el jugador que realiza la exploracion.
     * @return void
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

    /*
     * permite al jugador recolectar un tipo de metal especifico de la zona.
     * @param j: jugador - el jugador que recolecta.
     * @param tipo: itemtipo - el recurso que se intenta recolectar.
     * @return void
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



