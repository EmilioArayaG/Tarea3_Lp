package entorno;

import player.Jugador;

public abstract class Zona {
    protected final int zMin;
    protected final int zMax;

    public Zona(int zMin, int zMax){
        this.zMin = zMin;
        this.zMax = zMax;
    }

    public int zMin(){ return zMin; }
    public int zMax(){ return zMax; }

    protected double d(int z){
        int span = Math.max(1, zMax - zMin);
        double dd = (z - zMin) / (double) span;
        if (dd < 0) return 0.0;
        if (dd > 1) return 1.0;
        return dd;
    }

    protected int presion(Jugador j, int z){
        return 0;
    }

    protected int costoExplorar(Jugador j, int z){
        double dd = d(z);
        int pres = presion(j, z);
        return (int)Math.ceil(12 + 10*dd + pres);
    }

    protected int costoRecolectar(Jugador j, int z){
        double dd = d(z);
        int pres = presion(j, z);
        return (int)Math.ceil(10 + 6*dd + pres);
    }

    public int costoMoverTurno(Jugador j, int zDesde, int zHasta){
        int dz = Math.abs(zHasta - zDesde);
        double dd = d(zHasta);
        return (int)Math.ceil(((3 + 3*dd) * dz) / 50.0);
    }

    protected int produccion(int z, int nmin, int nmax){
        int val = (int)Math.floor(nmin + (nmax - nmin) * d(z));
        return Math.max(1, val);
    }

    public abstract void entrar(Jugador jugador);
    public abstract void explorar(Jugador jugador);
    public abstract void recolectaTipoRecurso(Jugador jugador, objetos.ItemTipo tipo);
}



