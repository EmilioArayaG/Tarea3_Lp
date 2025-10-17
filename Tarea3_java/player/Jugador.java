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

    /*
     * obtiene el objeto oxigeno asociado al jugador, que gestiona su capacidad y consumo.
     * @param ninguno
     * @return oxigeno: el componente de oxigeno del jugador.
     */
    public Oxigeno getOxigeno(){ return oxigeno; }

    /*
     * anade una cantidad especifica de un item al inventario del jugador.
     * @param t: itemtipo - el tipo de item que se va a agregar.
     * @param c: int - la cantidad de items a agregar.
     * @return void
     */
    public void agregar(ItemTipo t, int c){
        if (c <= 0) return;
        inventario.merge(t, c, Integer::sum);
    }

    /*
     * consulta la cantidad disponible de un tipo de item en el inventario.
     * @param t: itemtipo - el tipo de item a consultar.
     * @return int: la cantidad actual de ese item.
     */
    public int cantidad(ItemTipo t){ return inventario.getOrDefault(t, 0); }

    /*
     * consume una cantidad de un item del inventario si hay suficientes existencias.
     * @param t: itemtipo - el tipo de item a gastar.
     * @param c: int - la cantidad a consumir.
     * @return boolean: 'true' si el item se pudo gastar, 'false' en caso contrario.
     */
    public boolean gastar(ItemTipo t, int c){
        int act = cantidad(t);
        if (c <= 0 || act < c) return false;
        int rest = act - c;
        if (rest == 0) inventario.remove(t);
        else inventario.put(t, rest);
        return true;
    }

    /*
     * alias del metodo gastar, pero sin devolver un valor booleano.
     * @param t: itemtipo - el tipo de item a quitar.
     * @param c: int - la cantidad a quitar.
     * @return void
     */
    public void quitar(ItemTipo t, int c){
        gastar(t, c);
    }

    /*
     * devuelve una copia del inventario actual del jugador.
     * @param ninguno
     * @return enummap<itemtipo,integer>: un mapa que representa el inventario.
     */
    public EnumMap<ItemTipo,Integer> verInventario(){
        return new EnumMap<>(inventario);
    }

    /*
     * elimina todos los items del inventario del jugador.
     * @param ninguno
     * @return void
     */
    public void vaciarInventario(){ inventario.clear(); }

    /*
     * verifica si el jugador ha encontrado los planos de la nave.
     * @param ninguno
     * @return boolean: 'true' si posee los planos.
     */
    public boolean tienePlanos(){ return tienePlanos; }

    /*
     * establece si el jugador posee o no los planos de la nave.
     * @param v: boolean - el nuevo estado de posesion de los planos.
     * @return void
     */
    public void setTienePlanos(boolean v){ tienePlanos = v; }

    /*
     * verifica si el jugador tiene activa la mejora del tanque de oxigeno.
     * @param ninguno
     * @return boolean: 'true' si la mejora esta activa.
     */
    public boolean tieneMejoraTanque(){ return mejoraTanque; }

    /*
     * activa la mejora del tanque, duplicando la capacidad base de oxigeno.
     * @param ninguno
     * @return void
     */
    public void activarMejoraTanque(){
        if (!mejoraTanque){
            mejoraTanque = true;
            oxigeno.duplicarCapacidadBase();
        }
    }

    /*
     * verifica si el jugador tiene equipado el traje termico.
     * @param ninguno
     * @return boolean: 'true' si posee el traje.
     */
    public boolean tieneTrajeTermico(){ return trajeTermico; }

    /*
     * activa la posesion del traje termico para el jugador.
     * @param ninguno
     * @return void
     */
    public void activarTrajeTermico(){ trajeTermico = true; }

    /*
     * obtiene la profundidad actual a la que se encuentra el jugador.
     * @param ninguno
     * @return int: la profundidad en metros.
     */
    public int getProfundidad(){ return profundidad; }

    /*
     * establece la profundidad actual del jugador.
     * @param p: int - la nueva profundidad en metros.
     * @return void
     */
    public void setProfundidad(int p){
        if (p < 0) p = 0;
        this.profundidad = p;
    }

    /*
     * muestra por consola el estado completo del jugador, incluyendo o2, profundidad y progreso.
     * @param ninguno
     * @return void
     */
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

    /*
     * indica si el jugador, nadando, puede acceder a una profundidad (siempre puede intentarlo).
     * @param requerido: int - la profundidad a la que se intenta acceder.
     * @return boolean: siempre devuelve 'true'.
     */
    @Override
    public boolean puedeAcceder(int requerido) { return true; }

    /*
     * ajusta la profundidad del jugador, asegurando que se mantenga dentro de los limites de una zona.
     * @param zona: entorno.zona - la zona actual que define los limites.
     * @param nuevaprof: int - la profundidad deseada.
     * @return void
     */
    public void profundidadAjustar(entorno.Zona zona, int nuevaProf){
        if (zona == null){ setProfundidad(Math.max(0, nuevaProf)); return; }
        int p = Math.max(zona.zMin(), Math.min(zona.zMax(), nuevaProf));
        setProfundidad(p);
    }
}






