package objetos;

/** Propósito: Nave con límite de profundidad y anclaje por zona. */
public class NaveExploradora extends Vehiculo {
    private int limiteProf = 500;
    private boolean moduloActivo = false;

    /** Clase anidada (si tu profe la exige explícita). */
    public static class ModuloProfundidad { /* puede quedar vacía si solo es “marcador” */ }

    @Override
    public boolean puedeAcceder(int zMin){ return zMin <= limiteProf; }

    /** Instala módulo y aumenta límite a 1500 m. */
    public void instalarModuloProfundidad(){
        moduloActivo = true;
        limiteProf = 1500;
    }

    public boolean moduloActivo(){ return moduloActivo; }

    /** Sugerido: anclar en una profundidad por zona (lo haremos al crear Zonas). */
    public void anclarNave(int profAnclaje){
        // TODO: almacenar pnave por zona (usaremos entorno.Zonas para centralizarlo)
    }
}
