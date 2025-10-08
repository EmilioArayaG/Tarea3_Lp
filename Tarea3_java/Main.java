import player.Jugador;
import entorno.ZonaArrecife;
import objetos.ItemTipo;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Exploración Sub-acuática — prototipo ===");

        Jugador jugador = new Jugador();
        ZonaArrecife arrecife = new ZonaArrecife();

        try (Scanner sc = new Scanner(System.in)) {
            boolean seguir = true;
            while (seguir) {
                System.out.println("\nMenu:");
                System.out.println("1) Ver estado");
                System.out.println("2) Mover en Arrecife (0..199) [cobra O2]");
                System.out.println("3) Explorar Arrecife [cobra O2]");
                System.out.println("4) Recolectar CUARZO [cobra O2]");
                System.out.println("0) Salir");
                System.out.print("Opcion: ");
                String op = sc.nextLine().trim();

                switch (op) {
                    case "1" -> jugador.verEstadoJugador();

                    case "2" -> {
                        System.out.print("Nueva profundidad (m 0..199): ");
                        String in = sc.nextLine().trim();
                        try {
                            int zNueva = Integer.parseInt(in);
                            if (zNueva < 0 || zNueva > 199) {
                                System.out.println("Fuera de rango para Arrecife (0..199).");
                            } else {
                                int zVieja = jugador.getProfundidad();
                                int costo = arrecife.costoMoverTurno(jugador, zVieja, zNueva);
                                jugador.getOxigeno().consumirO2(costo);
                                jugador.setProfundidad(zNueva);
                                System.out.println("Te mueves de " + zVieja + " a " + zNueva + " m (O2 -" + costo + ")");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada inválida.");
                        }
                    }

                    case "3" -> arrecife.explorar(jugador);
                    case "4" -> arrecife.recolectaTipoRecurso(jugador, ItemTipo.CUARZO);
                    case "0" -> seguir = false;
                    default -> System.out.println("Opción inválida.");
                }

                // Derrota provisional: si O2 llega a 0, termina
                if (jugador.getOxigeno().restante() == 0) {
                    System.out.println("¡Te quedaste sin O2! (fin de partida provisional)");
                    seguir = false;
                }
            }
        }

        System.out.println("Fin del juego.");
    }
}
