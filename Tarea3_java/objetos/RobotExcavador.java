package objetos;

import entorno.NaveEstrellada;
import entorno.Zona;
import entorno.ZonaArrecife;
import entorno.ZonaProfunda;
import entorno.ZonaVolcanica;
import java.util.EnumMap;
import java.util.Random;

public class RobotExcavador {
    private static final Random RNG = new Random();

    private int capacidadCarga = 1000;
    private boolean danado = false;
    private final EnumMap<ItemTipo,Integer> carga = new EnumMap<>(ItemTipo.class);

    public int capacidadCarga(){ return capacidadCarga; }
    public boolean danado(){ return danado; }
    public EnumMap<ItemTipo,Integer> verCarga(){ return new EnumMap<>(carga); }
    public int pesoActual(){ return carga.values().stream().mapToInt(Integer::intValue).sum(); }

    public void setCapacidadCarga(int nueva){
        if (nueva > 0) capacidadCarga = nueva;
    }

    public void extraerEn(Zona zona, int z){
        if (zona instanceof NaveEstrellada){
            System.out.println("[Robot] No opera dentro de la Nave Estrellada.");
            return;
        }
        if (danado){
            System.out.println("[Robot] Esta danado. Repara antes de operar.");
            return;
        }

        ItemTipo[] pool;
        if (zona instanceof ZonaArrecife){
            pool = new ItemTipo[]{ ItemTipo.CUARZO, ItemTipo.SILICIO, ItemTipo.COBRE };
        } else if (zona instanceof ZonaProfunda){
            pool = new ItemTipo[]{ ItemTipo.PLATA, ItemTipo.ORO, ItemTipo.ACERO, ItemTipo.DIAMANTE, ItemTipo.MAGNETITA };
        } else if (zona instanceof ZonaVolcanica){
            pool = new ItemTipo[]{ ItemTipo.TITANIO, ItemTipo.SULFURO, ItemTipo.URANIO };
        } else {
            pool = new ItemTipo[]{};
        }

        int base = zona.nProduccion(z, 2, 5);
        int multiplicador = 3 + RNG.nextInt(4);
        int cantidad = Math.max(1, base * multiplicador);

        ItemTipo drop = pool.length == 0 ? null : pool[RNG.nextInt(pool.length)];
        if (drop == null) {
            System.out.println("[Robot] No hay recursos validos en esta zona.");
            return;
        }

        int nuevoPeso = pesoActual() + cantidad;
        if (nuevoPeso > capacidadCarga){
            danado = true;
            int espacio = Math.max(0, capacidadCarga - pesoActual());
            if (espacio > 0){
                carga.merge(drop, espacio, Integer::sum);
                System.out.println("[Robot] Sobrecarga. Se dano y solo almaceno " + espacio + " de " + cantidad + " " + drop + ".");
            } else {
                System.out.println("[Robot] Sobrecarga. Se dano sin poder almacenar mas material.");
            }
        } else {
            carga.merge(drop, cantidad, Integer::sum);
            System.out.println("[Robot] Extrajo " + drop + " x" + cantidad + " (carga: " + pesoActual() + "/" + capacidadCarga + ").");
        }
    }

    public void descargarEnNave(NaveExploradora nave){
        if (carga.isEmpty()){
            System.out.println("[Robot] No hay carga que descargar.");
            return;
        }
        for (var e : carga.entrySet()){
            nave.depositar(e.getKey(), e.getValue());
        }
        carga.clear();
        System.out.println("[Robot] Descarga completa en almacen de la nave.");
    }

    public void reparar(){
        danado = false;
        System.out.println("[Robot] Reparado.");
    }
}


