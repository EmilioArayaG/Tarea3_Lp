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

    /*
     * verifica si la nave puede operar a la profundidad requerida, segun su limite actual.
     * @param requerido: int - la profundidad a verificar.
     * @return boolean: 'true' si la profundidad esta dentro del limite de la nave.
     */
    @Override
    public boolean puedeAcceder(int requerido){
        return requerido <= limiteProf;
    }

    /*
     * instala el modulo de profundidad, aumentando el limite de la nave a 1500 metros.
     * @param ninguno
     * @return void
     */
    public void instalarModuloProfundidad(){
        moduloActivo = true;
        limiteProf = 1500;
    }

    /*
     * comprueba si el modulo de profundidad ya ha sido instalado en la nave.
     * @param ninguno
     * @return boolean: 'true' si el modulo esta activo.
     */
    public boolean moduloActivo(){ return moduloActivo; }

    /*
     * devuelve el limite de profundidad maximo que la nave puede alcanzar.
     * @param ninguno
     * @return int: el limite de profundidad en metros.
     */
    public int limiteProfundidad(){ return limiteProf; }

    /*
     * fija la profundidad de anclaje de la nave para una zona especifica.
     * @param zona: zona - la zona en la cual se fijara el anclaje.
     * @param prof: int - la profundidad de anclaje en metros.
     * @return void
     */
    public void setAnclaje(Zona zona, int prof){
        if (zona == null) return;
        anclajes.put(zona.getClass(), prof);
    }

    /*
     * alias del metodo setanclaje para un uso mas intuitivo.
     * @param zona: zona - la zona en la cual se fijara el anclaje.
     * @param prof: int - la profundidad de anclaje en metros.
     * @return void
     */
    public void anclarNave(Zona zona, int prof){
        setAnclaje(zona, prof);
    }

    /*
     * obtiene la profundidad de anclaje guardada para una zona. si no hay, usa la zmin de la zona.
     * @param zona: zona - la zona de la que se quiere saber el anclaje.
     * @return int: la profundidad de anclaje en metros.
     */
    public int getAnclaje(Zona zona){
        return anclajes.getOrDefault(zona.getClass(), zona.zMin());
    }

    /*
     * devuelve una copia del mapa de items almacenados en la nave.
     * @param ninguno
     * @return enummap<itemtipo,integer>: un mapa con los items y sus cantidades.
     */
    public EnumMap<ItemTipo,Integer> verAlmacen(){
        return new EnumMap<>(almacen);
    }

    /*
     * consulta la cantidad de un tipo de item especifico en el almacen de la nave.
     * @param t: itemtipo - el tipo de item a consultar.
     * @return int: la cantidad de items de ese tipo.
     */
    public int cantidad(ItemTipo t){
        return almacen.getOrDefault(t, 0);
    }

    /*
     * anade o retira una cantidad de un item del almacen de la nave.
     * @param t: itemtipo - el tipo de item a modificar.
     * @param c: int - la cantidad a anadir (si es negativa, se retira).
     * @return void
     */
    public void depositar(ItemTipo t, int c){
        if (c == 0) return;
        int cur = almacen.getOrDefault(t, 0) + c;
        if (cur <= 0) almacen.remove(t);
        else almacen.put(t, cur);
    }

    /*
     * retira una cantidad de un item del almacen, solo si hay suficiente stock.
     * @param t: itemtipo - el tipo de item a retirar.
     * @param c: int - la cantidad a retirar.
     * @return boolean: 'true' si la operacion fue exitosa.
     */
    public boolean retirar(ItemTipo t, int c){
        if (c <= 0) return false;
        int cur = almacen.getOrDefault(t, 0);
        if (cur < c) return false;
        int rest = cur - c;
        if (rest == 0) almacen.remove(t);
        else almacen.put(t, rest);
        return true;
    }

    /*
     * mueve items desde el almacen de la nave al inventario del jugador.
     * @param j: player.jugador - el jugador que recibira los items.
     * @param t: itemtipo - el tipo de item a transferir.
     * @param c: int - la cantidad a transferir.
     * @return boolean: 'true' si la transferencia se completo.
     */
    public boolean transferirAJugador(player.Jugador j, ItemTipo t, int c){
        if (retirar(t, c)){
            j.agregar(t, c);
            return true;
        }
        return false;
    }

    /*
     * transfiere todos los items del inventario del jugador al almacen de la nave.
     * @param j: player.jugador - el jugador cuyo inventario se vaciara.
     * @return void
     */
    public void depositarTodoDesdeJugador(player.Jugador j){
        var inv = j.verInventario();
        for (var e : inv.entrySet()){
            depositar(e.getKey(), e.getValue());
        }
        j.vaciarInventario();
    }
}





