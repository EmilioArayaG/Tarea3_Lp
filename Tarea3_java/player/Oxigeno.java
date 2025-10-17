package player;

public class Oxigeno {
    private int base = 60;
    private int bonus = 0;
    private int actual = base;

    /*
     * calcula la capacidad maxima de oxigeno, sumando la base y las mejoras.
     * @param ninguno
     * @return int: la capacidad total de oxigeno.
     */
    public int capacidadTotal(){ return base + bonus; }

    /*
     * devuelve la cantidad de oxigeno que le queda al jugador actualmente.
     * @param ninguno
     * @return int: las unidades de oxigeno restantes.
     */
    public int restante(){ return actual; }

    /*
     * rellena el tanque de oxigeno del jugador a su maxima capacidad.
     * @param ninguno
     * @return void
     */
    public void recargarCompleto(){ actual = capacidadTotal(); }

    /*
     * reduce el oxigeno del jugador en una cantidad determinada, sin bajar de cero.
     * @param c: int - las unidades de oxigeno a consumir.
     * @return void
     */
    public void consumirO2(int c){
        if (c < 0) return;
        actual -= c;
        if (actual < 0) actual = 0;
    }

    /*
     * aplica la mejora de tanque, duplicando la capacidad base de oxigeno.
     * @param ninguno
     * @return void
     */
    public void duplicarCapacidadBase(){
        base *= 2;
        if (actual > capacidadTotal()) actual = capacidadTotal();
    }

    /*
     * anade un bonus de 30 unidades a la capacidad maxima de oxigeno.
     * @param ninguno
     * @return void
     */
    public void mejorarOxigenoMas30(){
        bonus += 30;
        if (actual > capacidadTotal()) actual = capacidadTotal();
    }
}


