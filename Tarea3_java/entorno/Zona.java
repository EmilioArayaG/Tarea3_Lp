package entorno;
import player.Jugador;

public abstract class Zona {
    protected final int zMin;
    protected final int zMax;

    /**
     * crea zona en rango [zmin, zmax]
     * @param zMin minimo
     * @param zMax maximo
     */
    public Zona(int zMin, int zMax){
        this.zMin = zMin;
        this.zMax = zMax;
    }

    /** @return z minima */
    public int zMin(){ return zMin; }
    /** @return z maxima */
    public int zMax(){ return zMax; }

    /**
     * profundidad normalizada en [0,1]
     * @param z profundidad
     * @return valor normalizado
     */
    protected double d(int z){
        int span = Math.max(1, zMax - zMin);
        double val = (z - zMin) / (double)span;
        if (val < 0) return 0;
        if (val > 1) return 1;
        return val;
    }

    /**
     * presion efectiva; por defecto 0
     * @param j jugador
     * @param z profundidad
     * @return presion
     */
    protected int presion(Jugador j, int z){ return 0; }

    /**
     * costo de explorar: ceil(12 + 10d + presion)
     * @param j jugador
     * @param z profundidad
     * @return costo en o2
     */
    protected int costoExplorar(Jugador j, int z){
        return (int)Math.ceil(12 + 10*d(z) + presion(j, z));
    }

    /**
     * costo de recolectar: ceil(10 + 6d + presion)
     * @param j jugador
     * @param z profundidad
     * @return costo en o2
     */
    protected int costoRecolectar(Jugador j, int z){
        return (int)Math.ceil(10 + 6*d(z) + presion(j, z));
    }

    /**
     * costo de mover: ceil((3+3d)*|dz|/50)
     * @param zDesde origen
     * @param zHasta destino
     * @return costo en o2
     */
    protected int costoMover(int zDesde, int zHasta){
        int dz = Math.abs(zHasta - zDesde);
        return (int)Math.ceil(((3 + 3*d(zHasta)) * dz) / 50.0);
    }

    /**
     * produccion: max(1, floor(nmin + (nmax-nmin)*d))
     * @param z profundidad
     * @param nmin minimo
     * @param nmax maximo
     * @return cantidad producida
     */
    protected int produccion(int z, int nmin, int nmax){
        int val = (int)Math.floor(nmin + (nmax - nmin) * d(z));
        return Math.max(1, val);
    }

    /** expuesto para robot */
    public int nProduccion(int z, int nmin, int nmax){
        return produccion(z, nmin, nmax);
    }

    /**
     * costo publico para el turno: delega a la formula interna de mover
     * @param j jugador (no usado en la formula actual)
     * @param zDesde profundidad origen
     * @param zHasta profundidad destino
     * @return costo en o2
     */
    public int costoMoverTurno(Jugador j, int zDesde, int zHasta){
        return costoMover(zDesde, zHasta);
    }

    /** hook al entrar a la zona */
    public abstract void entrar(Jugador jugador);
    /** explorar la zona */
    public abstract void explorar(Jugador jugador);
    /** recolectar un tipo de recurso permitido en la zona */
    public abstract void recolectaTipoRecurso(Jugador jugador, objetos.ItemTipo tipo);
}





