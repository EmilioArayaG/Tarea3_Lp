package objetos;

public class NaveExploradora extends Vehiculo {
    private int limiteProf = 500;
    private boolean moduloActivo = false;

    public static class ModuloProfundidad { }

    @Override
    public boolean puedeAcceder(int requerido){
        return requerido <= limiteProf;
    }

    public void instalarModuloProfundidad(){
        moduloActivo = true;
        limiteProf = 1500;
    }

    public boolean moduloActivo(){ return moduloActivo; }

    public void anclarNave(int profAnclaje){
        // TODO: almacenar pnave por zona (lo centralizaremos en entorno.Zonas)
    }
}

