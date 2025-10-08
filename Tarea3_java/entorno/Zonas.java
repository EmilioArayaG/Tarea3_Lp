package entorno;

/** Propósito: Mantener únicas instancias de cada zona y estados globales. */
public final class Zonas {
    public final ZonaArrecife arrecife = new ZonaArrecife();
    public final ZonaProfunda profunda = new ZonaProfunda();
    public final ZonaVolcanica volcanica = new ZonaVolcanica();
    public final NaveEstrellada naveEstrellada = new NaveEstrellada();

    // Ejemplos de estados globales a agregar:
    // public int stockPiezasTanque = 3;
    // public boolean moduloEntregado = false;
    // public Map<Class<? extends Zona>, Integer> anclajes = new HashMap<>();
}
