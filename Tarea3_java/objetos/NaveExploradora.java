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

    /**
     * valida acceso con limite 500/1500
     * @param requerido profundidad requerida
     * @return true si no supera el limite
     */
    @Override
    public boolean puedeAcceder(int requerido){
        return requerido <= limiteProf;
    }

    /**
     * instala modulo y eleva el limite a 1500
     */
    public void instalarModuloProfundidad(){
        moduloActivo = true;
        limiteProf = 1500;
    }

    /** @return true si el modulo esta activo */
    public boolean moduloActivo(){ return moduloActivo; }

    /** @return limite de profundidad actual */
    public int limiteProfundidad(){ return limiteProf; }

    /**
     * define el anclaje por zona
     * @param zona zona destino
     * @param prof profundidad de anclaje
     */
    public void setAnclaje(Zona zona, int prof){
        if (zona == null) return;
        anclajes.put(zona.getClass(), prof);
    }

    /**
     * atajo legible para ajustar anclaje
     * @param zona zona destino
     * @param prof profundidad
     */
    public void anclarNave(Zona zona, int prof){
        setAnclaje(zona, prof);
    }

    /**
     * obtiene anclaje para una zona (zmin por defecto)
     * @param zona zona consultada
     * @return profundidad de anclaje
     */
    public int getAnclaje(Zona zona){
        return anclajes.getOrDefault(zona.getClass(), zona.zMin());
    }

    /** @return copia del almacen de la nave */
    public EnumMap<ItemTipo,Integer> verAlmacen(){
        return new EnumMap<>(almacen);
    }

    /** @return cantidad en almacen para un tipo */
    public int cantidad(ItemTipo t){
        return almacen.getOrDefault(t, 0);
    }

    /**
     * deposita al almacen (permite negativos para consumir)
     * @param t tipo de item
     * @param c cantidad
     */
    public void depositar(ItemTipo t, int c){
        if (c == 0) return;
        int cur = almacen.getOrDefault(t, 0) + c;
        if (cur <= 0) almacen.remove(t);
        else almacen.put(t, cur);
    }

    /**
     * vacia inventario del jugador en el almacen
     * @param j jugador
     */
    public void depositarTodoDesdeJugador(player.Jugador j){
        var inv = j.verInventario();
        for (var e : inv.entrySet()){
            depositar(e.getKey(), e.getValue());
        }
        j.vaciarInventario();
    }
}





