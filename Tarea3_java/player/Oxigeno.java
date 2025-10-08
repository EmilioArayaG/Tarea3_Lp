package player;

/** Propósito: Gestiona capacidad, mejoras y consumo de O2. */
public class Oxigeno {
    private int capacidadBase = 60; // O0 inicial
    private int bonus = 0;          // +30 por cada mejora de oxígeno
    private int actual = capacidadTotal();

    /** Capacidad total actual = base + bonus. */
    public int capacidadTotal(){ return capacidadBase + bonus; }

    /** Recarga al máximo. */
    public void recargarCompleto(){ this.actual = capacidadTotal(); }

    /** Consume unidades de O2 (satura en 0). */
    public void consumirO2(int unidades){
        if (unidades < 0) throw new IllegalArgumentException("unidades < 0");
        this.actual = Math.max(0, this.actual - unidades);
    }

    /** Agrega +30 a la capacidad total (mejora acumulable). */
    public void mejorarOxigenoMas30(){
        this.bonus += 30;
        // Si queremos que el jugador "gane" el aumento al instante:
        this.actual = Math.min(capacidadTotal(), this.actual + 30);
    }

    /** Duplica la capacidad base (mejora de tanque). */
    public void duplicarCapacidadBase(){
        this.capacidadBase *= 2;
        // Ajusta actual si quieres que se rellene automáticamente (opcional):
        this.actual = Math.min(capacidadTotal(), this.actual + capacidadBase/2);
    }

    public int restante(){ return actual; }
}
