package entorno;

public final class Zonas {
    public enum Tipo { ARRECIFE, PROFUNDA, VOLCANICA, NAVE_ESTRELLADA }

    private final ZonaArrecife arrecife = new ZonaArrecife();
    private final ZonaProfunda profunda = new ZonaProfunda();
    private final ZonaVolcanica volcanica = new ZonaVolcanica();
    private final NaveEstrellada naveEstrellada = new NaveEstrellada();

    public ZonaArrecife arrecife(){ return arrecife; }
    public ZonaProfunda profunda(){ return profunda; }
    public ZonaVolcanica volcanica(){ return volcanica; }
    public NaveEstrellada naveEstrellada(){ return naveEstrellada; }

    public Zona porTipo(Tipo t){
        return switch (t){
            case ARRECIFE -> arrecife;
            case PROFUNDA -> profunda;
            case VOLCANICA -> volcanica;
            case NAVE_ESTRELLADA -> naveEstrellada;
        };
    }

    public static String nombre(Zona z){
        if (z instanceof ZonaArrecife) return "Arrecife (0..199 m)";
        if (z instanceof ZonaProfunda) return "Profunda (200..999 m)";
        if (z instanceof ZonaVolcanica) return "Volcanica (1000..1500 m)";
        return "Nave Estrellada (0 m)";
    }
}


