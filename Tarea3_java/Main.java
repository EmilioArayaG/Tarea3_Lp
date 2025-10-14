import entorno.NaveEstrellada;
import entorno.Zona;
import entorno.ZonaArrecife;
import entorno.ZonaProfunda;
import entorno.ZonaVolcanica;
import entorno.Zonas;
import static java.lang.Math.ceil;
import java.util.Objects;
import java.util.Scanner;
import objetos.ItemTipo;
import objetos.NaveExploradora;
import objetos.RobotExcavador;
import player.Jugador;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Exploracion Subacuatica ===");

        Jugador jugador = new Jugador();

        Zonas zonas = new Zonas();
        ZonaArrecife arrecife = zonas.arrecife();
        ZonaProfunda profunda = zonas.profunda();
        ZonaVolcanica volcanica = zonas.volcanica();
        NaveEstrellada naveE = zonas.naveEstrellada();

        Zona zonaActual = arrecife;

        NaveExploradora nave = new NaveExploradora();
        nave.setAnclaje(Objects.requireNonNull(arrecife), 0);
        nave.setAnclaje(Objects.requireNonNull(profunda), 200);
        nave.setAnclaje(Objects.requireNonNull(naveE), 0);

        RobotExcavador robot = null;
        boolean robotConstruido = false;

        boolean enNave = true;

        try (Scanner sc = new Scanner(System.in)) {
            boolean seguir = true;
            while (seguir) {
                if (enNave) {
                    String anclajeTexto = (zonaActual != null && nave.puedeAcceder(zonaActual.zMin()))
                            ? (nave.getAnclaje(zonaActual) + " m")
                            : "-- (fuera de limite)";
                    System.out.println("\n[EN NAVE] Zona: " + Zonas.nombre(zonaActual)
                            + " | Anclaje: " + anclajeTexto
                            + " | Limite nave: " + nave.limiteProfundidad() + " m");
                    System.out.println("Menu (Nave):");
                    System.out.println("1) Ver estado");
                    System.out.println("2) Recargar O2");
                    System.out.println("3) Elegir zona destino");
                    System.out.println("4) Ajustar anclaje");
                    System.out.println("5) Salir al agua");
                    System.out.println("6) Crafteos");
                    System.out.println("7) Reparar Nave Estrellada");
                    System.out.println("8) Almacen de la nave");
                    System.out.println("9) Robot Excavador " + (robotConstruido ? "" : "(bloqueado: fabrica primero)"));
                    System.out.println("0) Salir");
                    System.out.print("Opcion: ");
                    String op = sc.nextLine().trim();

                    switch (op) {
                        case "1" -> jugador.verEstadoJugador();

                        case "2" -> {
                            jugador.getOxigeno().recargarCompleto();
                            System.out.println("O2 recargado.");
                        }

                        case "3" -> {
                            System.out.println("Destino: 1) Arrecife  2) Profunda  3) Volcanica  4) Nave Estrellada");
                            String d = sc.nextLine().trim();
                            Zona candidata = switch (d) {
                                case "1" -> arrecife;
                                case "2" -> profunda;
                                case "3" -> volcanica;
                                case "4" -> naveE;
                                default -> null;
                            };
                            if (candidata == null) {
                                System.out.println("Opcion invalida.");
                                break;
                            }
                            if (!(candidata instanceof NaveEstrellada) && !nave.puedeAcceder(candidata.zMin())) {
                                System.out.println("No puedes mover la nave a " + Zonas.nombre(candidata)
                                        + ": requiere modulo de profundidad (limite actual " + nave.limiteProfundidad() + " m).");
                                break;
                            }
                            zonaActual = candidata;
                            System.out.println("Nave movida a " + Zonas.nombre(zonaActual) + ".");
                        }

                        case "4" -> {
                            if (!nave.puedeAcceder(zonaActual.zMin())) {
                                System.out.println("No puedes anclar en " + Zonas.nombre(zonaActual)
                                        + ": requiere modulo de profundidad.");
                                break;
                            }
                            int zMin = zonaActual.zMin();
                            int zMax = Math.min(zonaActual.zMax(), nave.limiteProfundidad());
                            System.out.print("Nuevo anclaje (" + zMin + ".." + zMax + " m): ");
                            String in = sc.nextLine().trim();
                            try {
                                int nuevo = Integer.parseInt(in);
                                if (nuevo < zMin || nuevo > zMax) {
                                    System.out.println("Fuera de rango o sobre el limite de la nave.");
                                } else {
                                    nave.setAnclaje(zonaActual, nuevo);
                                    System.out.println("Anclaje actualizado a " + nuevo + " m.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Entrada invalida.");
                            }
                        }

                        case "5" -> {
                            if (zonaActual instanceof ZonaVolcanica) {
                                if (!(jugador.tieneMejoraTanque() && jugador.tieneTrajeTermico())) {
                                    System.out.println("No puedes salir a Volcanica sin mejora de tanque y traje termico.");
                                    break;
                                }
                            }
                            if (!(zonaActual instanceof NaveEstrellada) && !nave.puedeAcceder(zonaActual.zMin())) {
                                System.out.println("No puedes salir al agua aqui: instala el modulo de profundidad.");
                                break;
                            }
                            int anclaje = nave.getAnclaje(zonaActual);
                            jugador.setProfundidad(anclaje);
                            enNave = false;
                            if (zonaActual instanceof NaveEstrellada ne) {
                                ne.resetAccionVisita();
                            }
                            System.out.println("Bajas al agua a " + anclaje + " m.");
                        }

                        case "6" -> {
                            System.out.println("Crafteos:");
                            System.out.println("  a) Instalar modulo de profundidad (1x MODULO_PROFUNDIDAD)");
                            System.out.println("  b) Mejorar tanque (3x PIEZA_TANQUE)");
                            System.out.println("  c) Mejora O2 +30 (10x PLATA + 15x CUARZO)");
                            System.out.println("  d) Traje termico (10x SILICIO + 3x ORO + 5x CUARZO)");
                            System.out.println("  e) Fabricar Robot Excavador (15x COBRE + 10x MAGNETITA + 5x DIAMANTE + 20x ACERO)");
                            System.out.print("Elige (a/b/c/d/e): ");
                            String cc = sc.nextLine().trim().toLowerCase();
                            switch (cc){
                                case "a" -> {
                                    if (jugador.cantidad(ItemTipo.MODULO_PROFUNDIDAD) >= 1) {
                                        if (!nave.moduloActivo()) {
                                            jugador.quitar(ItemTipo.MODULO_PROFUNDIDAD, 1);
                                            nave.instalarModuloProfundidad();
                                            System.out.println("Modulo instalado. Limite ahora: " + nave.limiteProfundidad() + " m.");
                                        } else {
                                            System.out.println("La nave ya tiene el modulo instalado.");
                                        }
                                    } else System.out.println("No tienes MODULO_PROFUNDIDAD.");
                                }
                                case "b" -> {
                                    if (jugador.cantidad(ItemTipo.PIEZA_TANQUE) >= 3) {
                                        jugador.quitar(ItemTipo.PIEZA_TANQUE, 3);
                                        if (!jugador.tieneMejoraTanque()){
                                            jugador.activarMejoraTanque();
                                            System.out.println("Mejora de tanque aplicada.");
                                        } else System.out.println("Ya posees la mejora de tanque.");
                                    } else System.out.println("Faltan PIEZA_TANQUE (3).");
                                }
                                case "c" -> {
                                    if (jugador.cantidad(ItemTipo.PLATA) >= 10 && jugador.cantidad(ItemTipo.CUARZO) >= 15){
                                        jugador.quitar(ItemTipo.PLATA, 10);
                                        jugador.quitar(ItemTipo.CUARZO, 15);
                                        jugador.getOxigeno().mejorarOxigenoMas30();
                                        System.out.println("Capacidad de O2 +30 aplicada.");
                                    } else System.out.println("Faltan recursos: 10 PLATA y 15 CUARZO.");
                                }
                                case "d" -> {
                                    if (jugador.cantidad(ItemTipo.SILICIO) >= 10 && jugador.cantidad(ItemTipo.ORO) >= 3 && jugador.cantidad(ItemTipo.CUARZO) >= 5){
                                        jugador.quitar(ItemTipo.SILICIO, 10);
                                        jugador.quitar(ItemTipo.ORO, 3);
                                        jugador.quitar(ItemTipo.CUARZO, 5);
                                        if (!jugador.tieneTrajeTermico()){
                                            jugador.activarTrajeTermico();
                                            System.out.println("Traje termico equipado.");
                                        } else System.out.println("Ya tienes traje termico.");
                                    } else System.out.println("Faltan recursos: 10 SILICIO, 3 ORO, 5 CUARZO.");
                                }
                                case "e" -> {
                                    if (robotConstruido) {
                                        System.out.println("Ya tienes un Robot Excavador.");
                                        break;
                                    }
                                    boolean ok = true;
                                    ok &= jugador.cantidad(ItemTipo.COBRE) >= 15;
                                    ok &= jugador.cantidad(ItemTipo.MAGNETITA) >= 10;
                                    ok &= jugador.cantidad(ItemTipo.DIAMANTE) >= 5;
                                    ok &= jugador.cantidad(ItemTipo.ACERO) >= 20;
                                    if (ok){
                                        jugador.quitar(ItemTipo.COBRE, 15);
                                        jugador.quitar(ItemTipo.MAGNETITA, 10);
                                        jugador.quitar(ItemTipo.DIAMANTE, 5);
                                        jugador.quitar(ItemTipo.ACERO, 20);
                                        robot = new RobotExcavador();
                                        robotConstruido = true;
                                        System.out.println("Robot Excavador fabricado y listo para operar.");
                                    } else {
                                        System.out.println("Faltan recursos: 15 COBRE, 10 MAGNETITA, 5 DIAMANTE, 20 ACERO.");
                                    }
                                }
                                default -> {}
                            }
                        }

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
                                System.out.println("Faltan recursos: 50 TITANIO, 30 ACERO, 15 URANIO, 20 SULFURO.");
                            } else {
                                jugador.quitar(ItemTipo.TITANIO, 50);
                                jugador.quitar(ItemTipo.ACERO, 30);
                                jugador.quitar(ItemTipo.URANIO, 15);
                                jugador.quitar(ItemTipo.SULFURO, 20);
                                System.out.println("Has reparado la Nave Estrellada. Victoria!");
                                return;
                            }
                        }

                        case "8" -> {
                            System.out.println("Almacen de la nave:");
                            var al = nave.verAlmacen();
                            if (al.isEmpty()) System.out.println(" (vacio)");
                            else al.forEach((t,c)-> System.out.println(" - " + t + ": " + c));
                            System.out.println("Depositar TODO el inventario del jugador en la nave? (s/n)");
                            if (sc.nextLine().trim().equalsIgnoreCase("s")){
                                nave.depositarTodoDesdeJugador(jugador);
                                System.out.println("Deposito completado.");
                            }
                        }

                        case "9" -> {
                            if (!robotConstruido || robot == null){
                                System.out.println("Robot Excavador no disponible. Fabricalo en Crafteos (opcion 6, letra e).");
                                break;
                            }
                            System.out.println("Robot Excavador:");
                            System.out.println(" 1) Extraer en zona actual");
                            System.out.println(" 2) Descargar carga en la nave");
                            System.out.println(" 3) Reparar robot (4 CABLES + 3 PIEZAS_METAL + 5 MAGNETITA)");
                            System.out.println(" 4) Mejorar capacidad (+25%) (10 TITANIO + 20 CUARZO)");
                            System.out.println(" 5) Ver estado de carga");
                            String rop = sc.nextLine().trim();
                            switch (rop){
                                case "1" -> robot.extraerEn(zonaActual, nave.getAnclaje(zonaActual));
                                case "2" -> robot.descargarEnNave(nave);
                                case "3" -> {
                                    boolean ok = true;
                                    ok &= jugador.cantidad(ItemTipo.CABLES) >= 4;
                                    ok &= jugador.cantidad(ItemTipo.PIEZAS_METAL) >= 3;
                                    ok &= jugador.cantidad(ItemTipo.MAGNETITA) >= 5;
                                    if (ok){
                                        jugador.quitar(ItemTipo.CABLES, 4);
                                        jugador.quitar(ItemTipo.PIEZAS_METAL, 3);
                                        jugador.quitar(ItemTipo.MAGNETITA, 5);
                                        robot.reparar();
                                    } else {
                                        System.out.println("Faltan recursos: 4 CABLES, 3 PIEZAS_METAL, 5 MAGNETITA.");
                                    }
                                }
                                case "4" -> {
                                    boolean ok = true;
                                    ok &= jugador.cantidad(ItemTipo.TITANIO) >= 10;
                                    ok &= jugador.cantidad(ItemTipo.CUARZO) >= 20;
                                    if (ok){
                                        jugador.quitar(ItemTipo.TITANIO, 10);
                                        jugador.quitar(ItemTipo.CUARZO, 20);
                                        int cap = robot.capacidadCarga();
                                        int nueva = (int) ceil(cap * 1.25);
                                        robot.setCapacidadCarga(nueva);
                                        System.out.println("Capacidad aumentada a " + robot.capacidadCarga() + ".");
                                    } else {
                                        System.out.println("Faltan recursos: 10 TITANIO y 20 CUARZO.");
                                    }
                                }
                                case "5" -> {
                                    var carga = robot.verCarga();
                                    System.out.println("Capacidad: " + robot.capacidadCarga() + " | Carga: " + robot.pesoActual()
                                            + (robot.danado() ? " | Estado: DANADO" : " | Estado: OK"));
                                    if (carga.isEmpty()) System.out.println(" (sin materiales)");
                                    else carga.forEach((t,c)-> System.out.println(" - " + t + ": " + c));
                                }
                                default -> {}
                            }
                        }

                        case "0" -> seguir = false;

                        default -> System.out.println("Opcion invalida.");
                    }

                } else {
                    System.out.println("\n[EN AGUA] " + Zonas.nombre(zonaActual)
                            + " | Prof: " + jugador.getProfundidad() + " m | O2: " + jugador.getOxigeno().restante());
                    System.out.println("Menu (Agua):");
                    System.out.println("1) Ver estado");
                    System.out.println("2) Mover");
                    System.out.println("3) Explorar");
                    System.out.println("4) Recolectar (elige recurso)");
                    System.out.println("5) Volver a la nave");
                    System.out.println("0) Salir");
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
                                System.out.println("Entrada invalida.");
                            }
                        }

                        case "3" -> zonaActual.explorar(jugador);

                        case "4" -> {
                            ItemTipo elegido = seleccionarRecurso(sc, zonaActual);
                            if (elegido == null) {
                                System.out.println("Seleccion invalida.");
                            } else {
                                zonaActual.recolectaTipoRecurso(jugador, elegido);
                            }
                        }

                        case "5" -> {
                            enNave = true;
                            System.out.println("Vuelves a la nave.");
                        }

                        case "0" -> seguir = false;

                        default -> System.out.println("Opcion invalida.");
                    }

                    if (!(zonaActual instanceof NaveEstrellada) && jugador.getOxigeno().restante() == 0) {
                        System.out.println("Te quedaste sin O2. Pierdes el botin de esta salida y vuelves a la nave.");
                        jugador.vaciarInventario();
                        jugador.getOxigeno().recargarCompleto();
                        enNave = true;
                    }
                }
            }
        }

        System.out.println("Fin del juego.");
    }

    private static ItemTipo seleccionarRecurso(Scanner sc, Zona z){
        if (z instanceof ZonaArrecife) {
            System.out.println("Elige recurso: 1) CUARZO  2) SILICIO  3) COBRE");
            return switch (sc.nextLine().trim()){
                case "1" -> ItemTipo.CUARZO;
                case "2" -> ItemTipo.SILICIO;
                case "3" -> ItemTipo.COBRE;
                default -> null;
            };
        } else if (z instanceof ZonaProfunda) {
            System.out.println("Elige recurso: 1) PLATA  2) ORO  3) ACERO  4) DIAMANTE  5) MAGNETITA");
            return switch (sc.nextLine().trim()){
                case "1" -> ItemTipo.PLATA;
                case "2" -> ItemTipo.ORO;
                case "3" -> ItemTipo.ACERO;
                case "4" -> ItemTipo.DIAMANTE;
                case "5" -> ItemTipo.MAGNETITA;
                default -> null;
            };
        } else if (z instanceof ZonaVolcanica) {
            System.out.println("Elige recurso: 1) TITANIO  2) SULFURO  3) URANIO");
            return switch (sc.nextLine().trim()){
                case "1" -> ItemTipo.TITANIO;
                case "2" -> ItemTipo.SULFURO;
                case "3" -> ItemTipo.URANIO;
                default -> null;
            };
        } else if (z instanceof NaveEstrellada) {
            System.out.println("Elige recurso: 1) CABLES  2) PIEZAS_METAL");
            return switch (sc.nextLine().trim()){
                case "1" -> ItemTipo.CABLES;
                case "2" -> ItemTipo.PIEZAS_METAL;
                default -> null;
            };
        }
        return null;
    }
}







