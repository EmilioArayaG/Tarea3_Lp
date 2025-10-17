package entorno;
import player.Jugador;

public abstract class Zona {
    protected final int zMin;
    protected final int zMax;

    /*
     * constructor para una zona, definiendo su rango de profundidades.
     * @param zmin: int - la profundidad minima de la zona.
     * @param zmax: int - la profundidad maxima de la zona.
     * @return
     */
    public Zona(int zMin, int zMax){
        this.zMin = zMin;
        this.zMax = zMax;
    }

    /*
     * devuelve la profundidad minima de la zona.
     * @param ninguno
     * @return int: la profundidad minima en metros.
     */
    public int zMin(){ return zMin; }

    /*
     * devuelve la profundidad maxima de la zona.
     * @param ninguno
     * @return int: la profundidad maxima en metros.
     */
    public int zMax(){ return zMax; }

    /*
     * calcula la profundidad normalizada (un valor entre 0 y 1) para una profundidad dada.
     * @param z: int - la profundidad actual en metros.
     * @return double: el valor normalizado.
     */
    protected double d(int z){
        int span = Math.max(1, zMax - zMin);
        double val = (z - zMin) / (double)span;
        if (val < 0) return 0;
        if (val > 1) return 1;
        return val;
    }

    /*
     * calcula el costo adicional de oxigeno debido a la presion en la zona.
     * @param j: jugador - el jugador, para verificar si tiene mejoras.
     * @param z: int - la profundidad actual.
     * @return int: el costo de oxigeno adicional por presion.
     */
    protected int presion(Jugador j, int z){ return 0; }

    /*
     * calcula el costo de oxigeno para la accion de explorar en una profundidad dada.
     * @param j: jugador - el jugador que explora.
     * @param z: int - la profundidad a la que se explora.
     * @return int: el costo total en unidades de oxigeno.
     */
    protected int costoExplorar(Jugador j, int z){
        return (int)Math.ceil(12 + 10*d(z) + presion(j, z));
    }

    /*
     * calcula el costo de oxigeno para la accion de recolectar en una profundidad dada.
     * @param j: jugador - el jugador que recolecta.
     * @param z: int - la profundidad a la que se recolecta.
     * @return int: el costo total en unidades de oxigeno.
     */
    protected int costoRecolectar(Jugador j, int z){
        return (int)Math.ceil(10 + 6*d(z) + presion(j, z));
    }

    /*
     * calcula el costo de oxigeno para moverse entre dos profundidades.
     * @param zdesde: int - la profundidad de origen.
     * @param zhasta: int - la profundidad de destino.
     * @return int: el costo total en unidades de oxigeno.
     */
    protected int costoMover(int zDesde, int zHasta){
        int dz = Math.abs(zHasta - zDesde);
        return (int)Math.ceil(((3 + 3*d(zHasta)) * dz) / 50.0);
    }

    /*
     * calcula la cantidad de recursos obtenidos en una recoleccion, basado en la profundidad.
     * @param z: int - la profundidad actual.
     * @param nmin: int - la cantidad minima de produccion de la zona.
     * @param nmax: int - la cantidad maxima de produccion de la zona.
     * @return int: la cantidad de recursos a obtener.
     */
    protected int produccion(int z, int nmin, int nmax){
        int val = (int)Math.floor(nmin + (nmax - nmin) * d(z));
        return Math.max(1, val);
    }

    /*
     * expone el metodo de produccion para que sea accesible externamente (ej: por el robot).
     * @param z: int - la profundidad actual.
     * @param nmin: int - la cantidad minima de produccion.
     * @param nmax: int - la cantidad maxima de produccion.
     * @return int: la cantidad de recursos a obtener.
     */
    public int nProduccion(int z, int nmin, int nmax){
        return produccion(z, nmin, nmax);
    }

    /*
     * delega el calculo del costo de movimiento para ser usado en el turno del jugador.
     * @param j: jugador - el jugador que se mueve.
     * @param zdesde: int - la profundidad de origen.
     * @param zhasta: int - la profundidad de destino.
     * @return int: el costo en unidades de oxigeno.
     */
    public int costoMoverTurno(Jugador j, int zDesde, int zHasta){
        return costoMover(zDesde, zHasta);
    }

    /*
     * define la accion que ocurre al entrar en la zona.
     * @param jugador: jugador - el jugador que entra.
     * @return void
     */
    public abstract void entrar(Jugador jugador);

    /*
     * define la logica de la accion de explorar en la zona.
     * @param jugador: jugador - el jugador que explora.
     * @return void
     */
    public abstract void explorar(Jugador jugador);

    /*
     * define la logica para recolectar un tipo de recurso especifico en la zona.
     * @param jugador: jugador - el jugador que recolecta.
     * @param tipo: objetos.itemtipo - el recurso a recolectar.
     * @return void
     */
    public abstract void recolectaTipoRecurso(Jugador jugador, objetos.ItemTipo tipo);
}





