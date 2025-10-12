import entorno.NaveEstrellada;
import entorno.Zona;
import entorno.ZonaArrecife;
import entorno.ZonaProfunda;
import entorno.ZonaVolcanica;
import java.util.Scanner;
import objetos.ItemTipo;
import objetos.NaveExploradora;
import player.Jugador;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Exploración Sub-acuática — prototipo ===");

        Jugador jugador = new Jugador();
        ZonaArrecife arrecife = new ZonaArrecife();
        ZonaProfunda profunda = new ZonaProfunda();
        ZonaVolcanica volcanica = new ZonaVolcanica();
        NaveEstrellada naveE = new NaveEstrellada();
        Zona zonaActual = arrecife;

        NaveExploradora nave = new NaveExploradora();
        nave.setAnclaje(arrecife, 0);
        nave.setAnclaje(profunda, 200);
        // No seteamos anclaje inicial de Volcánica si no hay módulo (1000 > límite 500)
        nave.setAnclaje(naveE, 0);

        boolean enNave = true;
        boolean victoria = false;

        try (Scanner sc = new Scanner(System.in)) {
            boolean seguir = true;
            while (seguir) {
                if (victoria) { System.out.println("¡Has reparado la Nave Estrellada! ¡Victoria!"); break; }

                if (enNave) {
                    String nombre = nombreZona(zonaActual);
                    String anclajeTexto = nave.puedeAcceder(zonaActual.zMin()) ? (nave.getAnclaje(zonaActual) + " m") : "— (fuera de límite)";
                    System.out.println("\n[EN NAVE] Zona anclada: " + nombre
                            + " | Anclaje: " + anclajeTexto + " | Límite nave: " + nave.limiteProfundidad() + " m");
                    System.out.println("Menu (Nave):");
                    System.out.println("1) Ver estado");
                    System.out.println("2) Recargar O2");
                    System.out.println("3) Elegir zona destino (mover nave sin costo)");
                    System.out.println("4) Ajustar anclaje en zona actual");
                    System.out.println("5) Salir al agua (bajar al anclaje)");
                    System.out.println("6) Crafteos");
                    System.out.println("7) Reparar Nave Estrellada (si tienes plano + recursos)");
                    System.out.println("0) Salir del juego");
                    System.out.print("Opcion: ");
                    String op = sc.nextLine().trim();

                    switch (op) {
                        case "1" -> jugador.verEstadoJugador();

                        case "2" -> {
                            jugador.getOxigeno().recargarCompleto();
                            System.out.println("O2 recargado.");
                        }

                        case "3" -> {
                            System.out.println("Destino: 1) Arrecife  2) Profunda  3) Volcánica  4) Nave Estrellada");
                            String d = sc.nextLine().trim();
                            Zona candidata = switch (d) {
                                case "1" -> arrecife;
                                case "2" -> profunda;
                                case "3" -> volcanica;
                                case "4" -> naveE;
                                default -> null;
                            };
                            if (candidata == null) {
                                System.out.println("Opción inválida.");
                                break;
                            }
                            // Validación de alcance por límite de la nave
                            if (!(candidata instanceof NaveEstrellada) && !nave.puedeAcceder(candidata.zMin())) {
                                System.out.println("No puedes mover la nave a " + nombreZona(candidata)
                                        + ": requiere módulo de profundidad (límite actual " + nave.limiteProfundidad() + " m).");
                                break;
                            }
                            zonaActual = candidata;
                            System.out.println("Nave movida a " + nombreZona(zonaActual) + ".");
                        }

                        case "4" -> {
                            if (!nave.puedeAcceder(zonaActual.zMin())) {
                                System.out.println("No puedes anclar en " + nombreZona(zonaActual)
                                        + ": requiere módulo de profundidad.");
                                break;
                            }
                            int zMin = zonaActual.zMin();
                            int zMax = Math.min(zonaActual.zMax(), nave.limiteProfundidad());
                            System.out.print("Nuevo anclaje (" + zMin + ".." + zMax + " m): ");
                            String in = sc.nextLine().trim();
                            try {
                                int nuevo = Integer.parseInt(in);
                                if (nuevo < zMin || nuevo > zMax) {
                                    System.out.println("Fuera de rango o sobre el límite de la nave.");
                                } else {
                                    nave.setAnclaje(zonaActual, nuevo);
                                    System.out.println("Anclaje actualizado a " + nuevo + " m.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Entrada inválida.");
                            }
                        }

                        case "5" -> {
                            // Salir al agua: además de límite de nave, validamos requisitos de cada zona
                            if (zonaActual instanceof ZonaVolcanica) {
                                if (!(jugador.tieneMejoraTanque() && jugador.tieneTrajeTermico())) {
                                    System.out.println("No puedes salir a Volcánica sin mejora de tanque y traje térmico.");
                                    break;
                                }
                            }
                            if (!(zonaActual instanceof NaveEstrellada) && !nave.puedeAcceder(zonaActual.zMin())) {
                                System.out.println("No puedes salir al agua aquí: la nave no puede alcanzar esta zona (instala el módulo).");
                                break;
                            }
                            int anclaje = nave.getAnclaje(zonaActual);
                            jugador.setProfundidad(anclaje);
                            enNave = false;
                            if (zonaActual instanceof NaveEstrellada) {
                                naveE.resetAccionVisita();
                            }
                            System.out.println("Bajas al agua a " + anclaje + " m.");
                        }

                        case "6" -> crafteos(sc, jugador, nave);

                        case "7" -> {
                            if (!jugador.tienePlanos()){
                                System.out.println("No tienes el PLANO_NAVE.");
                                break;
                            }
                            boolean ok = true;
                            ok &= jugador.cantidad(ItemTipo.TITANIO) >= 50;
                            ok &= jugador.cantidad(ItemTipo.ACERO) >= 30;
                            ok &= jugador.cantidad(ItemTipo.URANIO) >= 15;
                            ok &= jugador.cantidad(ItemTipo.SULFURO) >= 20;
                            if (!ok){
                                System.out.println("Faltan recursos: requiere 50 TITANIO, 30 ACERO, 15 URANIO, 20 SULFURO.");
                            } else {
                                jugador.quitar(ItemTipo.TITANIO, 50);
                                jugador.quitar(ItemTipo.ACERO, 30);
                                jugador.quitar(ItemTipo.URANIO, 15);
                                jugador.quitar(ItemTipo.SULFURO, 20);
                                System.out.println("¡Has reparado la Nave Estrellada! ¡Victoria!");
                                return;
                            }
                        }

                        case "0" -> seguir = false;

                        default -> System.out.println("Opción inválida.");
                    }

                } else {
                    System.out.println("\n[EN AGUA] " + nombreZona(zonaActual)
                            + " | Prof: " + jugador.getProfundidad() + " m | O2: " + jugador.getOxigeno().restante());
                    System.out.println("Menu (Agua):");
                    System.out.println("1) Ver estado");
                    System.out.println("2) Mover (cobra O2, excepto en Nave Estrellada)");
                    System.out.println("3) Explorar");
                    System.out.println("4) Recolectar");
                    System.out.println("5) Volver a la nave");
                    System.out.println("0) Salir del juego");
                    System.out.print("Opcion: ");
                    String op = sc.nextLine().trim();

                    switch (op) {
                        case "1" -> jugador.verEstadoJugador();

                        case "2" -> {
                            if (zonaActual instanceof NaveEstrellada) {
                                System.out.println("Dentro de la Nave Estrellada no se navega con O2.");
                                break;
                            }
                            int zMin = zonaActual.zMin();
                            int zMax = zonaActual.zMax();
                            System.out.print("Nueva profundidad (" + zMin + ".." + zMax + " m): ");
                            String in = sc.nextLine().trim();
                            try {
                                int zNueva = Integer.parseInt(in);
                                if (zNueva < zMin || zNueva > zMax) {
                                    System.out.println("Fuera de rango de la zona.");
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
                            } else if (zonaActual instanceof ZonaProfunda) {
                                zonaActual.recolectaTipoRecurso(jugador, ItemTipo.PLATA);
                            } else if (zonaActual instanceof ZonaVolcanica) {
                                zonaActual.recolectaTipoRecurso(jugador, ItemTipo.TITANIO);
                            } else {
                                zonaActual.recolectaTipoRecurso(jugador, ItemTipo.CABLES);
                            }
                        }

                        case "5" -> {
                            enNave = true;
                            System.out.println("Vuelves a la nave.");
                        }

                        case "0" -> seguir = false;

                        default -> System.out.println("Opción inválida.");
                    }

                    if (!enNave && !(zonaActual instanceof NaveEstrellada) && jugador.getOxigeno().restante() == 0) {
                        System.out.println("¡Te quedaste sin O2! (fin de partida provisional)");
                        seguir = false;
                    }
                }
            }
        }

        System.out.println("Fin del juego.");
    }

    private static String nombreZona(Zona z){
        if (z instanceof ZonaArrecife) return "Arrecife (0..199 m)";
        if (z instanceof ZonaProfunda) return "Profunda (200..999 m)";
        if (z instanceof ZonaVolcanica) return "Volcánica (1000..1500 m)";
        return "Nave Estrellada (0 m)";
    }

    private static void crafteos(Scanner sc, Jugador j, NaveExploradora nave){
        System.out.println("Crafteos:");
        System.out.println("  a) Instalar módulo de profundidad (1x MODULO_PROFUNDIDAD)");
        System.out.println("  b) Mejorar tanque (3x PIEZA_TANQUE)");
        System.out.println("  c) Mejora O2 +30 (10x PLATA + 15x CUARZO)");
        System.out.println("  d) Traje térmico (10x SILICIO + 3x ORO + 5x CUARZO)");
        System.out.print("Elige (a/b/c/d): ");
        String cc = sc.nextLine().trim().toLowerCase();
        switch (cc){
            case "a" -> {
                if (j.cantidad(ItemTipo.MODULO_PROFUNDIDAD) >= 1) {
                    if (!nave.moduloActivo()) {
                        j.quitar(ItemTipo.MODULO_PROFUNDIDAD, 1);
                        nave.instalarModuloProfundidad();
                        System.out.println("Módulo instalado. Límite ahora: " + nave.limiteProfundidad() + " m.");
                    } else {
                        System.out.println("La nave ya tiene el módulo instalado.");
                    }
                } else System.out.println("No tienes MODULO_PROFUNDIDAD.");
            }
            case "b" -> {
                if (j.cantidad(ItemTipo.PIEZA_TANQUE) >= 3) {
                    j.quitar(ItemTipo.PIEZA_TANQUE, 3);
                    if (!j.tieneMejoraTanque()){
                        j.activarMejoraTanque();
                        System.out.println("Mejora de tanque aplicada (capacidad duplicada).");
                    } else System.out.println("Ya posees la mejora de tanque.");
                } else System.out.println("Faltan PIEZA_TANQUE (3).");
            }
            case "c" -> {
                if (j.cantidad(ItemTipo.PLATA) >= 10 && j.cantidad(ItemTipo.CUARZO) >= 15){
                    j.quitar(ItemTipo.PLATA, 10);
                    j.quitar(ItemTipo.CUARZO, 15);
                    j.getOxigeno().mejorarOxigenoMas30();
                    System.out.println("Capacidad de O2 +30 aplicada.");
                } else System.out.println("Faltan recursos: 10 PLATA y 15 CUARZO.");
            }
            case "d" -> {
                if (j.cantidad(ItemTipo.SILICIO) >= 10 && j.cantidad(ItemTipo.ORO) >= 3 && j.cantidad(ItemTipo.CUARZO) >= 5){
                    j.quitar(ItemTipo.SILICIO, 10);
                    j.quitar(ItemTipo.ORO, 3);
                    j.quitar(ItemTipo.CUARZO, 5);
                    if (!j.tieneTrajeTermico()){
                        j.activarTrajeTermico();
                        System.out.println("Traje térmico equipado.");
                    } else System.out.println("Ya tienes traje térmico.");
                } else System.out.println("Faltan recursos: 10 SILICIO, 3 ORO, 5 CUARZO.");
            }
            default -> {}
        }
    }
}




