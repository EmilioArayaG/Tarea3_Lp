package objetos;

/** Propósito: Robot para extracciones por lotes (capacidad y mejoras). */
public class RobotExcavador {
    private int capacidad = 1000; // base
    private boolean operativo = true;

    public int capacidad(){ return capacidad; }
    public boolean operativo(){ return operativo; }

    public void mejorar25(){ capacidad = (int)Math.round(capacidad * 1.25); }
    public void reparar(){ operativo = true; }
    public void danar(){ operativo = false; }
}
