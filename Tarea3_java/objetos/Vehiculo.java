package objetos;

import java.util.EnumMap;

/** Propósito: Base de vehículos con inventario y transferencia. */
public abstract class Vehiculo implements AccesoProfundidad {
    protected final EnumMap<ItemTipo,Integer> inventario = new EnumMap<>(ItemTipo.class);

    protected void add(ItemTipo t, int c){ inventario.merge(t, c, Integer::sum); }
    protected boolean take(ItemTipo t, int c){
        int cur = inventario.getOrDefault(t, 0);
        if (cur < c) return false;
        if (cur == c) inventario.remove(t);
        else inventario.put(t, cur - c);
        return true;
    }

    // Firma libre según enunciado (la definiremos al conectar Jugador/Nave):
    // public void transferirObjetos(Jugador j, ItemTipo t, int c, Direccion dir) { ... }
}
