package objetos;

import entorno.NaveEstrellada;
import entorno.Zona;
import entorno.ZonaArrecife;
import entorno.ZonaProfunda;
import entorno.ZonaVolcanica;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class RobotExcavador {
    private final Random rng = new Random();

    private int capacidad = 1000;
    private boolean danado = false;

    private final EnumMap<ItemTipo,Integer> carga = new EnumMap<>(ItemTipo.class);

    /*
     * devuelve la capacidad maxima de carga del robot.
     * @param ninguno
     * @return int: la capacidad maxima.
     */
    public int capacidadCarga(){ return capacidad; }

    /*
     * establece una nueva capacidad de carga para el robot.
     * @param nueva: int - el nuevo valor de la capacidad.
     * @return void
     */
    public void setCapacidadCarga(int nueva){
        if (nueva < 1) return;
        capacidad = nueva;
    }

    /*
     * calcula el peso total de todos los recursos que el robot lleva actualmente.
     * @param ninguno
     * @return int: la suma de las cantidades de todos los items.
     */
    public int pesoActual(){
        int s = 0;
        for (int v : carga.values()) s += v;
        return s;
    }

    /*
     * devuelve una copia del inventario de carga del robot.
     * @param ninguno
     * @return map<itemtipo,integer>: un mapa con la carga actual del robot.
     */
    public Map<ItemTipo,Integer> verCarga(){
        return new EnumMap<>(carga);
    }

    /*
     * indica si el robot se encuentra danado por sobrecarga.
     * @param ninguno
     * @return boolean: 'true' si el robot esta danado.
     */
    public boolean danado(){ return danado; }

    /*
     * realiza una extraccion de recursos en la zona y profundidad indicadas.
     * @param zona: zona - la zona donde se realiza la extraccion.
     * @param profundidad: int - la profundidad actual para calcular la cantidad.
     * @return void
     */
    public void extraerEn(Zona zona, int profundidad){
        if (danado){ System.out.println("[robot] danado, repara antes de extraer."); return; }

        ItemTipo tipo = elegirTipoPorZona(zona);
        int n = rangoPorZona(zona, profundidad);

        int nuevoPeso = pesoActual() + n;
        if (nuevoPeso > capacidad){
            danado = true;
            System.out.println("[robot] sobrecarga, queda danado (carga=" + nuevoPeso + "/" + capacidad + ")");
        }

        if (tipo != null){
            carga.merge(tipo, n, Integer::sum);
            System.out.println("[robot] extrae " + tipo + " x" + n + " (carga=" + pesoActual() + "/" + capacidad + ")");
        }
    }

    /*
     * transfiere toda la carga del robot al almacen de la nave exploradora.
     * @param nave: naveexploradora - la nave que recibira los recursos.
     * @return void
     */
    public void descargarEnNave(NaveExploradora nave){
        if (carga.isEmpty()){
            System.out.println("[robot] sin carga.");
            return;
        }
        for (var e : carga.entrySet()){
            nave.depositar(e.getKey(), e.getValue());
        }
        System.out.println("[robot] descarga completa en almacen de la nave");
        carga.clear();
    }

    /*
     * repara el robot, permitiendole volver a extraer recursos.
     * @param ninguno
     * @return void
     */
    public void reparar(){
        danado = false;
        System.out.println("[robot] reparado");
    }

    /*
     * aumenta la capacidad de carga del robot en un 25%.
     * @param ninguno
     * @return void
     */
    public void mejorarCapacidad(){
        capacidad = (int)Math.ceil(capacidad * 1.25);
        System.out.println("[robot] capacidad mejorada a " + capacidad);
    }

    /*
     * elige aleatoriamente un tipo de recurso valido para la zona actual.
     * @param z: zona - la zona actual.
     * @return itemtipo: el tipo de item a recolectar, o null si no corresponde.
     */
    private ItemTipo elegirTipoPorZona(Zona z){
        if (z instanceof ZonaArrecife){
            ItemTipo[] a = { ItemTipo.CUARZO, ItemTipo.SILICIO, ItemTipo.COBRE };
            return a[rng.nextInt(a.length)];
        } else if (z instanceof ZonaProfunda){
            ItemTipo[] a = { ItemTipo.PLATA, ItemTipo.ORO, ItemTipo.ACERO, ItemTipo.DIAMANTE, ItemTipo.MAGNETITA };
            return a[rng.nextInt(a.length)];
        } else if (z instanceof ZonaVolcanica){
            ItemTipo[] a = { ItemTipo.TITANIO, ItemTipo.SULFURO, ItemTipo.URANIO };
            return a[rng.nextInt(a.length)];
        } else if (z instanceof NaveEstrellada){
            ItemTipo[] a = { ItemTipo.CABLES, ItemTipo.PIEZAS_METAL };
            return a[rng.nextInt(a.length)];
        }
        return null;
    }

    /*
     * calcula la cantidad de recursos a obtener segun la formula n(d) y los rangos de la zona.
     * @param z: zona - la zona actual.
     * @param profundidad: int - la profundidad para el calculo.
     * @return int: la cantidad de recursos a obtener.
     */
    private int rangoPorZona(Zona z, int profundidad){
        if (z instanceof ZonaArrecife) return z.nProduccion(profundidad, 1, 3);
        if (z instanceof ZonaProfunda)  return z.nProduccion(profundidad, 2, 6);
        if (z instanceof ZonaVolcanica) return z.nProduccion(profundidad, 3, 8);
        if (z instanceof NaveEstrellada) return 1 + rng.nextInt(2);
        return 1;
    }
}




