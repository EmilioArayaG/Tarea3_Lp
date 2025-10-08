package entorno;

import player.Jugador;
import objetos.ItemTipo;
import java.util.Random;

public class ZonaArrecife extends Zona {
    private static final Random RNG = new Random();

    // Stock global de piezas de tanque (persistente en el proceso)
    private static int stockPiezasTanque = 3;

    public ZonaArrecife(){ super(0, 199); }

    @Override
    public void entrar(Jugador j){
        // Arrecife (0..199 m) no requiere validación especial por ahora.
    }

    @Override
    public void explorar(Jugador j){
        int z = j.getProfundidad();
        int costo = costoExplorar(j, z);
        j.getOxigeno().consumirO2(costo);
        System.out.println("[Arrecife] Exploras a " + z + " m. O2 -" + costo);

        // 30% PIEZA_TANQUE si queda stock
        boolean caePieza = stockPiezasTanque > 0 && RNG.nextDouble() < 0.30;
        if (caePieza){
            stockPiezasTanque--;
            j.agregar(ItemTipo.PIEZA_TANQUE, 1);
            System.out.println("¡Encontraste PIEZA_TANQUE! (stock restante: " + stockPiezasTanque + ")");
            return;
        }

        // Recurso básico aleatorio: CUARZO, SILICIO o COBRE
        ItemTipo[] basicos = { ItemTipo.CUARZO, ItemTipo.SILICIO, ItemTipo.COBRE };
        ItemTipo drop = basicos[RNG.nextInt(basicos.length)];

        // Producción simple para arrancar: 1..3 según d (luego la afinamos a la fórmula exacta)
        int cantidad = 1 + (int)Math.floor(2 * d(z));
        j.agregar(drop, cantidad);
        System.out.println("Obtienes: " + drop + " x" + cantidad);
    }

    @Override
    public void recolectaTipoRecurso(Jugador j, ItemTipo tipo){
        // Solo recursos del Arrecife:
        switch (tipo){
            case CUARZO, SILICIO, COBRE -> {
                int z = j.getProfundidad();
                int costo = costoRecolectar(j, z);
                j.getOxigeno().consumirO2(costo);
                int cantidad = 1 + (int)Math.floor(2 * d(z));
                j.agregar(tipo, cantidad);
                System.out.println("[Arrecife] Recolectaste " + tipo + " x" + cantidad + " (O2 -" + costo + ")");
            }
            default -> System.out.println("[Arrecife] Ese recurso no está disponible aquí: " + tipo);
        }
    }
}

