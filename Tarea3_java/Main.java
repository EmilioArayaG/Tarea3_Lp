import entorno.Zona;
import entorno.ZonaArrecife;
import entorno.ZonaProfunda;
import java.util.Scanner;
import objetos.ItemTipo;
import player.Jugador;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Exploración Sub-acuática — prototipo ===");

        Jugador jugador = new Jugador();
        ZonaArrecife arrecife = new ZonaArrecife();
        ZonaProfunda profunda = new ZonaProfunda();
        Zona zonaActual = arrecife;

        try (Scanner sc = new Scanner(System.in)) {
            boolean seguir = true;
            while (seguir) {
                System.out.println("\nZona actual: " + (zonaActual instanceof ZonaArrecife ? "Arrecife (0..199 m)" : "Profunda (200..999 m)"));
                System.out.println("Menu:");
                System.out.println("1) Ver estado");
                System.out.println("2) Mover en zona actual [cobra O2]");
                System.out.println("3) Explorar zona actual [cobra O2]");
                System.out.println("4) Recolectar recurso de zona actual [cobra O2]");
                System.out.println("5) Ir a Arrecife");
                System.out.println("6) Ir a Profunda");
                System.out.println("0) Salir");
                System.out.print("Opcion: ");
                String op = sc.nextLine().trim();

                switch (op) {
                    case "1" -> jugador.verEstadoJugador();

                    case "2" -> {
                        int zMin = (zonaActual instanceof ZonaArrecife) ? 0 : 200;
                        int zMax = (zonaActual instanceof ZonaArrecife) ? 199 : 999;
                        System.out.print("Nueva profundidad (m " + zMin + ".." + zMax + "): ");
                        String in = sc.nextLine().trim();
                        try {
                            int zNueva = Integer.parseInt(in);
                            if (zNueva < zMin || zNueva > zMax) {
                                System.out.println("Fuera de rango de la zona actual.");
                            } else {
                                int zVieja = jugador.getProfundidad();
                                int costo = zonaActual.costoMoverTurno(jugador, zVieja, zNueva);
                                jugador.getOxigeno().consumirO2(costo);
                                jugador.setProfundidad(zNueva);
                                System.out.println("Te mueves de " + zVieja + " a " + zNueva + " m (O2 -" + costo + ")");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada inválida.");
                        }
                    }

                    case "3" -> zonaActual.explorar(jugador);

                    case "4" -> {
                        if (zonaActual instanceof ZonaArrecife) {
                            zonaActual.recolectaTipoRecurso(jugador, ItemTipo.CUARZO);
                        } else {
                            zonaActual.recolectaTipoRecurso(jugador, ItemTipo.PLATA);
                        }
                    }

                    case "5" -> {
                        zonaActual = arrecife;
                        if (jugador.getProfundidad() > 199) jugador.setProfundidad(0);
                        System.out.println("Cambiado a Arrecife.");
                    }

                    case "6" -> {
                        zonaActual = profunda;
                        if (jugador.getProfundidad() < 200) jugador.setProfundidad(200);
                        System.out.println("Cambiado a Profunda.");
                    }

                    case "0" -> seguir = false;

                    default -> System.out.println("Opción inválida.");
                }

                if (jugador.getOxigeno().restante() == 0) {
                    System.out.println("¡Te quedaste sin O2! (fin de partida provisional)");
                    seguir = false;
                }
            }
        }

        System.out.println("Fin del juego.");
    }
}

