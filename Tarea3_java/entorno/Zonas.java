package entorno;

public final class Zonas {
    public enum Tipo { ARRECIFE, PROFUNDA, VOLCANICA, NAVE_ESTRELLADA }

    private final ZonaArrecife arrecife = new ZonaArrecife();
    private final ZonaProfunda profunda = new ZonaProfunda();
    private final ZonaVolcanica volcanica = new ZonaVolcanica();
    private final NaveEstrellada naveEstrellada = new NaveEstrellada();

    /*
     * obtiene la instancia unica de la zona arrecife.
     * @param ninguno
     * @return zonaarrecife: la instancia de la zona.
     */
    public ZonaArrecife arrecife(){ return arrecife; }

    /*
     * obtiene la instancia unica de la zona profunda.
     * @param ninguno
     * @return zonaprofunda: la instancia de la zona.
     */
    public ZonaProfunda profunda(){ return profunda; }

    /*
     * obtiene la instancia unica de la zona volcanica.
     * @param ninguno
     * @return zonavolcanica: la instancia de la zona.
     */
    public ZonaVolcanica volcanica(){ return volcanica; }

    /*
     * obtiene la instancia unica de la nave estrellada.
     * @param ninguno
     * @return naveestrellada: la instancia de la zona.
     */
    public NaveEstrellada naveEstrellada(){ return naveEstrellada; }

    /*
     * devuelve la instancia de una zona a partir de su tipo enumerado.
     * @param t: tipo - el tipo de zona solicitado.
     * @return zona: la instancia unica correspondiente a ese tipo.
     */
    public Zona porTipo(Tipo t){
        return switch (t){
            case ARRECIFE -> arrecife;
            case PROFUNDA -> profunda;
            case VOLCANICA -> volcanica;
            case NAVE_ESTRELLADA -> naveEstrellada;
        };
    }

    /*
     * genera un nombre descriptivo para una zona, incluyendo su rango de profundidad.
     * @param z: zona - la zona de la que se quiere obtener el nombre.
     * @return string: el texto con el nombre y el rango.
     */
    public static String nombre(Zona z){
        if (z instanceof ZonaArrecife) return "arrecife (0..199 m)";
        if (z instanceof ZonaProfunda) return "profunda (200..999 m)";
        if (z instanceof ZonaVolcanica) return "volcanica (1000..1500 m)";
        return "nave estrellada (0 m)";
    }
}



