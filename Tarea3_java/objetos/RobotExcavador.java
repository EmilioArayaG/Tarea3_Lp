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

    /** @return capacidad maxima de carga */
    public int capacidadCarga(){ return capacidad; }

    /**
     * fija capacidad de carga
     * @param nueva nueva capacidad
     */
    public void setCapacidadCarga(int nueva){
        if (nueva < 1) return;
        capacidad = nueva;
    }

    /** @return peso actual sumando todas las entradas */
    public int pesoActual(){
        int s = 0;
        for (int v : carga.values()) s += v;
        return s;
    }

    /** @return mapa copia de la carga por tipo */
    public Map<ItemTipo,Integer> verCarga(){
        return new EnumMap<>(carga);
    }

    /** @return true si el robot esta danado */
    public boolean danado(){ return danado; }

    /**
     * extrae lote segun la zona y la profundidad
     * @param zona zona actual
     * @param profundidad profundidad usada para n(d)
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

    /**
     * descargar toda la carga al almacen de la nave
     * @param nave nave exploradora
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

    /**
     * reparar sin consumir (tu main ya descuenta recursos del jugador)
     */
    public void reparar(){
        danado = false;
        System.out.println("[robot] reparado");
    }

    /**
     * mejora capacidad +25% redondeando hacia arriba
     */
    public void mejorarCapacidad(){
        capacidad = (int)Math.ceil(capacidad * 1.25);
        System.out.println("[robot] capacidad mejorada a " + capacidad);
    }


    /**
     * elige un tipo acorde a la zona
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

    /**
     * decide n(d) acorde a la zona usando sus rangos
     */
    private int rangoPorZona(Zona z, int profundidad){
        if (z instanceof ZonaArrecife) return z.nProduccion(profundidad, 1, 3);
        if (z instanceof ZonaProfunda)  return z.nProduccion(profundidad, 2, 6);
        if (z instanceof ZonaVolcanica) return z.nProduccion(profundidad, 3, 8);
        if (z instanceof NaveEstrellada) return 1 + rng.nextInt(2);
        return 1;
    }
}




