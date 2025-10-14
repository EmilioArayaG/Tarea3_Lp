package player;

public class Oxigeno {
    private int base = 60;
    private int bonus = 0;
    private int actual = base;

    public int capacidadTotal(){ return base + bonus; }
    public int restante(){ return actual; }

    public void recargarCompleto(){
        actual = capacidadTotal();
    }

    public void consumirO2(int c){
        if (c < 0) return;
        actual -= c;
        if (actual < 0) actual = 0;
    }

    public void duplicarCapacidadBase(){
        base *= 2;
        if (actual > capacidadTotal()) actual = capacidadTotal();
    }

    public void mejorarOxigenoMas30(){
        bonus += 30;
        if (actual > capacidadTotal()) actual = capacidadTotal();
    }
}

