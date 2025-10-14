package objetos;

import entorno.Zona;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class NaveExploradora extends Vehiculo {
    private int limiteProf = 500;
    private boolean moduloActivo = false;

    private final Map<Class<? extends Zona>, Integer> anclajes = new HashMap<>();
    private final EnumMap<ItemTipo,Integer> almacen = new EnumMap<>(ItemTipo.class);

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

    public EnumMap<ItemTipo,Integer> verAlmacen(){
        return new EnumMap<>(almacen);
    }

    public int cantidad(ItemTipo t){
        return almacen.getOrDefault(t, 0);
    }

    public void depositar(ItemTipo t, int c){
        if (c <= 0) return;
        almacen.merge(t, c, Integer::sum);
    }

    public void depositarTodoDesdeJugador(player.Jugador j){
        var inv = j.verInventario();
        for (var e : inv.entrySet()){
            depositar(e.getKey(), e.getValue());
        }
        j.vaciarInventario();
    }
}




