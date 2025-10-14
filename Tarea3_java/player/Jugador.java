package player;

import java.util.EnumMap;
import objetos.AccesoProfundidad;
import objetos.ItemTipo;

public class Jugador implements AccesoProfundidad {
    private final Oxigeno oxigeno = new Oxigeno();
    private final EnumMap<ItemTipo,Integer> inventario = new EnumMap<>(ItemTipo.class);

    private int profundidad = 0;

    private boolean tienePlanos = false;
    private boolean mejoraTanque = false;
    private boolean trajeTermico = false;

    public Oxigeno getOxigeno(){ return oxigeno; }

    public void agregar(ItemTipo t, int c){
        if (c <= 0) return;
        inventario.merge(t, c, Integer::sum);
    }

    public boolean quitar(ItemTipo t, int c){
        if (c <= 0) return false;
        int cur = inventario.getOrDefault(t, 0);
        if (cur < c) return false;
        if (cur == c) inventario.remove(t);
        else inventario.put(t, cur - c);
        return true;
    }

    public int cantidad(ItemTipo t){
        return inventario.getOrDefault(t, 0);
    }

    public EnumMap<ItemTipo,Integer> verInventario(){
        return new EnumMap<>(inventario);
    }

    public void vaciarInventario(){ inventario.clear(); }

    public boolean tienePlanos(){ return tienePlanos; }
    public void setTienePlanos(boolean v){ tienePlanos = v; }

    public boolean tieneMejoraTanque(){ return mejoraTanque; }
    public void activarMejoraTanque(){
        if (!mejoraTanque){
            mejoraTanque = true;
            oxigeno.duplicarCapacidadBase();
        }
    }

    public boolean tieneTrajeTermico(){ return trajeTermico; }
    public void activarTrajeTermico(){ trajeTermico = true; }

    public int getProfundidad(){ return profundidad; }
    public void setProfundidad(int p){
        if (p < 0) p = 0;
        this.profundidad = p;
    }

    public void verEstadoJugador(){
        System.out.println("---- ESTADO JUGADOR ----");
        System.out.println("O2: " + oxigeno.restante() + " / " + oxigeno.capacidadTotal());
        System.out.println("Profundidad: " + profundidad + " m");
        System.out.println("Progreso: tanque=" + (mejoraTanque ? "OK" : "NO")
                + ", traje=" + (trajeTermico ? "OK" : "NO")
                + ", planos=" + (tienePlanos ? "OK" : "NO"));
        if (inventario.isEmpty()){
            System.out.println("Inventario: (vacio)");
        } else {
            System.out.println("Inventario:");
            inventario.forEach((t,c) -> System.out.println(" - " + t + ": " + c));
        }
        System.out.println("------------------------");
    }

    @Override
    public boolean puedeAcceder(int requerido) {
        return true;
    }
}




