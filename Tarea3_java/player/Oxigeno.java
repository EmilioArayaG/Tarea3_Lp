package player;

public class Oxigeno {
    private int base = 60;
    private int bonus = 0;
    private int actual = base;

    /** @return capacidad total (base + bonus) */
    public int capacidadTotal(){ return base + bonus; }

    /** @return oxigeno restante */
    public int restante(){ return actual; }

    /** recarga al maximo */
    public void recargarCompleto(){ actual = capacidadTotal(); }

    /**
     * consume oxigeno sin bajar de 0
     * @param c unidades a consumir
     */
    public void consumirO2(int c){
        if (c < 0) return;
        actual -= c;
        if (actual < 0) actual = 0;
    }

    /** duplica la capacidad base (mejora de tanque) */
    public void duplicarCapacidadBase(){
        base *= 2;
        if (actual > capacidadTotal()) actual = capacidadTotal();
    }

    /** suma +30 a la capacidad (mejora incremental) */
    public void mejorarOxigenoMas30(){
        bonus += 30;
        if (actual > capacidadTotal()) actual = capacidadTotal();
    }
}


