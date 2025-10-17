package entorno;

public final class Zonas {
    public enum Tipo { ARRECIFE, PROFUNDA, VOLCANICA, NAVE_ESTRELLADA }

    private final ZonaArrecife arrecife = new ZonaArrecife();
    private final ZonaProfunda profunda = new ZonaProfunda();
    private final ZonaVolcanica volcanica = new ZonaVolcanica();
    private final NaveEstrellada naveEstrellada = new NaveEstrellada();

    /** @return arrecife */
    public ZonaArrecife arrecife(){ return arrecife; }
    /** @return profunda */
    public ZonaProfunda profunda(){ return profunda; }
    /** @return volcanica */
    public ZonaVolcanica volcanica(){ return volcanica; }
    /** @return nave estrellada */
    public NaveEstrellada naveEstrellada(){ return naveEstrellada; }

    /**
     * retorna zona por tipo
     * @param t tipo
     * @return instancia unica
     */
    public Zona porTipo(Tipo t){
        return switch (t){
            case ARRECIFE -> arrecife;
            case PROFUNDA -> profunda;
            case VOLCANICA -> volcanica;
            case NAVE_ESTRELLADA -> naveEstrellada;
        };
    }

    /**
     * nombre legible con rango
     * @param z zona
     * @return texto
     */
    public static String nombre(Zona z){
        if (z instanceof ZonaArrecife) return "arrecife (0..199 m)";
        if (z instanceof ZonaProfunda) return "profunda (200..999 m)";
        if (z instanceof ZonaVolcanica) return "volcanica (1000..1500 m)";
        return "nave estrellada (0 m)";
    }
}



