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

    /** @return componente de oxigeno */
    public Oxigeno getOxigeno(){ return oxigeno; }

    /**
     * agrega items al inventario
     * @param t tipo
     * @param c cantidad
     */
    public void agregar(ItemTipo t, int c){
        if (c <= 0) return;
        inventario.merge(t, c, Integer::sum);
    }

    /** @return cantidad de un tipo en inventario */
    public int cantidad(ItemTipo t){ return inventario.getOrDefault(t, 0); }

    /**
     * gasta items si alcanza
     * @param t tipo
     * @param c cantidad
     * @return true si pudo gastar
     */
    public boolean gastar(ItemTipo t, int c){
        int act = cantidad(t);
        if (c <= 0 || act < c) return false;
        int rest = act - c;
        if (rest == 0) inventario.remove(t);
        else inventario.put(t, rest);
        return true;
    }

    /**
     * quitar: alias sin retorno usado por main
     * @param t tipo
     * @param c cantidad
     */
    public void quitar(ItemTipo t, int c){
        gastar(t, c);
    }

    /** @return copia del inventario */
    public EnumMap<ItemTipo,Integer> verInventario(){
        return new EnumMap<>(inventario);
    }

    /** vacia el inventario del jugador */
    public void vaciarInventario(){ inventario.clear(); }

    /** @return true si posee planos */
    public boolean tienePlanos(){ return tienePlanos; }
    /** fija bandera de planos */
    public void setTienePlanos(boolean v){ tienePlanos = v; }

    /** @return true si tiene mejora de tanque */
    public boolean tieneMejoraTanque(){ return mejoraTanque; }

    /** activa mejora de tanque y duplica o2 base */
    public void activarMejoraTanque(){
        if (!mejoraTanque){
            mejoraTanque = true;
            oxigeno.duplicarCapacidadBase();
        }
    }

    /** @return true si tiene traje termico */
    public boolean tieneTrajeTermico(){ return trajeTermico; }

    /** activa traje termico */
    public void activarTrajeTermico(){ trajeTermico = true; }

    /** @return profundidad actual */
    public int getProfundidad(){ return profundidad; }

    /**
     * fija profundidad sin clamp (uso interno)
     * @param p nueva profundidad
     */
    public void setProfundidad(int p){
        if (p < 0) p = 0;
        this.profundidad = p;
    }

    /** imprime estado breve del jugador */
    public void verEstadoJugador(){
        System.out.println("---- estado jugador ----");
        System.out.println("o2: " + oxigeno.restante() + " / " + oxigeno.capacidadTotal());
        System.out.println("profundidad: " + profundidad + " m");
        System.out.println("progreso: tanque=" + (mejoraTanque ? "ok" : "no")
                + ", traje=" + (trajeTermico ? "ok" : "no")
                + ", planos=" + (tienePlanos ? "ok" : "no"));
        if (inventario.isEmpty()){
            System.out.println("(inventario vacio)");
        } else {
            inventario.forEach((t,c) -> System.out.println(" - " + t + ": " + c));
        }
        System.out.println("------------------------");
    }

    /** el vehiculo valida limites; aqui no se restringe */
    @Override
    public boolean puedeAcceder(int requerido) { return true; }

    /**
     * ajusta profundidad con clamp a los limites de la zona
     * @param zona zona para limites
     * @param nuevaProf profundidad solicitada
     */
    public void profundidadAjustar(entorno.Zona zona, int nuevaProf){
        if (zona == null){ setProfundidad(Math.max(0, nuevaProf)); return; }
        int p = Math.max(zona.zMin(), Math.min(zona.zMax(), nuevaProf));
        setProfundidad(p);
    }
}






