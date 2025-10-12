package objetos;

import entorno.Zona;
import java.util.HashMap;
import java.util.Map;

public class NaveExploradora extends Vehiculo {
    private int limiteProf = 500;
    private boolean moduloActivo = false;

    private final Map<Class<? extends Zona>, Integer> anclajes = new HashMap<>();

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
    public int limiteProfundidad(){ return limiteProf; }

    public void setAnclaje(Zona zona, int prof){
        anclajes.put(zona.getClass(), prof);
    }

    public int getAnclaje(Zona zona){
        return anclajes.getOrDefault(zona.getClass(), zona.zMin());
    }
}



