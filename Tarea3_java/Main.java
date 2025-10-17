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
    /*
     * inicia y gestiona el bucle principal del juego de exploracion subacuatica.
     * @param args: string[] - argumentos de linea de comandos (no se utilizan).
     * @return void
     */
    public static void main(String[] args) {
        System.out.println("=== exploracion subacuatica ===");

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
                    System.out.println("\n[en nave] zona: " + Zonas.nombre(zonaActual)
                            + " | anclaje: " + anclajeTexto
                            + " | limite nave: " + nave.limiteProfundidad() + " m");
                    System.out.println("menu (nave):");
                    System.out.println("1) ver estado");
                    System.out.println("2) recargar o2");
                    System.out.println("3) elegir zona destino");
                    System.out.println("4) ajustar anclaje");
                    System.out.println("5) salir al agua");
                    System.out.println("6) crafteos");
                    System.out.println("7) reparar nave estrellada");
                    System.out.println("8) almacen de la nave");
                    System.out.println("9) robot excavador " + (robotConstruido ? "" : "(bloqueado: fabrica primero)"));
                    System.out.println("0) salir");
                    System.out.print("opcion: ");
                    String op = sc.nextLine().trim();

                    switch (op) {
                        case "1" -> jugador.verEstadoJugador();

                        case "2" -> {
                            jugador.getOxigeno().recargarCompleto();
                            System.out.println("o2 recargado.");
                        }

                        case "3" -> {
                            System.out.println("destino: 1) arrecife  2) profunda  3) volcanica  4) nave estrellada");
                            String d = sc.nextLine().trim();
                            Zona candidata = switch (d) {
                                case "1" -> arrecife;
                                case "2" -> profunda;
                                case "3" -> volcanica;
                                case "4" -> naveE;
                                default -> null;
                            };
                            if (candidata == null) {
                                System.out.println("opcion invalida.");
                                break;
                            }
                            if (!(candidata instanceof NaveEstrellada) && !nave.puedeAcceder(candidata.zMin())) {
                                System.out.println("no puedes mover la nave a " + Zonas.nombre(candidata)
                                        + ": requiere modulo de profundidad (limite actual " + nave.limiteProfundidad() + " m).");
                                break;
                            }
                            zonaActual = candidata;
                            System.out.println("nave movida a " + Zonas.nombre(zonaActual) + ".");
                        }

                        case "4" -> {
                            if (!nave.puedeAcceder(zonaActual.zMin())) {
                                System.out.println("no puedes anclar en " + Zonas.nombre(zonaActual)
                                        + ": requiere modulo de profundidad.");
                                break;
                            }
                            int zMin = zonaActual.zMin();
                            int zMax = Math.min(zonaActual.zMax(), nave.limiteProfundidad());
                            System.out.print("nuevo anclaje (" + zMin + ".." + zMax + " m): ");
                            String in = sc.nextLine().trim();
                            try {
                                int nuevo = Integer.parseInt(in);
                                if (nuevo < zMin || nuevo > zMax) {
                                    System.out.println("fuera de rango o sobre el limite de la nave.");
                                } else {
                                    nave.setAnclaje(zonaActual, nuevo);
                                    System.out.println("anclaje actualizado a " + nuevo + " m.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("entrada invalida.");
                            }
                        }

                        case "5" -> {
                            if (zonaActual instanceof ZonaVolcanica) {
                                if (!(jugador.tieneMejoraTanque() && jugador.tieneTrajeTermico())) {
                                    System.out.println("no puedes salir a volcanica sin mejora de tanque y traje termico.");
                                    break;
                                }
                            }
                            if (!(zonaActual instanceof NaveEstrellada) && !nave.puedeAcceder(zonaActual.zMin())) {
                                System.out.println("no puedes salir al agua aqui: instala el modulo de profundidad.");
                                break;
                            }
                            int anclaje = nave.getAnclaje(zonaActual);
                            jugador.setProfundidad(anclaje);
                            enNave = false;
                            if (zonaActual instanceof NaveEstrellada ne) {
                                ne.resetAccionVisita();
                            }
                            System.out.println("bajas al agua a " + anclaje + " m.");
                        }

                        case "6" -> {
                            System.out.println("crafteos:");
                            System.out.println("  a) instalar modulo de profundidad (1x modulo_profundidad)");
                            System.out.println("  b) mejorar tanque (3x pieza_tanque)");
                            System.out.println("  c) mejora o2 +30 (10x plata + 15x cuarzo)");
                            System.out.println("  d) traje termico (10x silicio + 3x oro + 5x cuarzo)");
                            System.out.println("  e) fabricar robot excavador (15x cobre + 10x magnetita + 5x diamante + 20x acero)");
                            System.out.print("elige (a/b/c/d/e): ");
                            String cc = sc.nextLine().trim().toLowerCase();
                            switch (cc){
                                case "a" -> {
                                    if (jugador.cantidad(ItemTipo.MODULO_PROFUNDIDAD) >= 1) {
                                        if (!nave.moduloActivo()) {
                                            jugador.quitar(ItemTipo.MODULO_PROFUNDIDAD, 1);
                                            nave.instalarModuloProfundidad();
                                            System.out.println("modulo instalado. limite ahora: " + nave.limiteProfundidad() + " m.");
                                        } else {
                                            System.out.println("la nave ya tiene el modulo instalado.");
                                        }
                                    } else System.out.println("no tienes modulo_profundidad.");
                                }
                                case "b" -> {
                                    if (jugador.cantidad(ItemTipo.PIEZA_TANQUE) >= 3) {
                                        jugador.quitar(ItemTipo.PIEZA_TANQUE, 3);
                                        if (!jugador.tieneMejoraTanque()){
                                            jugador.activarMejoraTanque();
                                            System.out.println("mejora de tanque aplicada.");
                                        } else System.out.println("ya posees la mejora de tanque.");
                                    } else System.out.println("faltan pieza_tanque (3).");
                                }
                                case "c" -> {
                                    if (jugador.cantidad(ItemTipo.PLATA) >= 10 && jugador.cantidad(ItemTipo.CUARZO) >= 15){
                                        jugador.quitar(ItemTipo.PLATA, 10);
                                        jugador.quitar(ItemTipo.CUARZO, 15);
                                        jugador.getOxigeno().mejorarOxigenoMas30();
                                        System.out.println("capacidad de o2 +30 aplicada.");
                                    } else System.out.println("faltan recursos: 10 plata y 15 cuarzo.");
                                }
                                case "d" -> {
                                    if (jugador.cantidad(ItemTipo.SILICIO) >= 10 && jugador.cantidad(ItemTipo.ORO) >= 3 && jugador.cantidad(ItemTipo.CUARZO) >= 5){
                                        jugador.quitar(ItemTipo.SILICIO, 10);
                                        jugador.quitar(ItemTipo.ORO, 3);
                                        jugador.quitar(ItemTipo.CUARZO, 5);
                                        if (!jugador.tieneTrajeTermico()){
                                            jugador.activarTrajeTermico();
                                            System.out.println("traje termico equipado.");
                                        } else System.out.println("ya tienes traje termico.");
                                    } else System.out.println("faltan recursos: 10 silicio, 3 oro, 5 cuarzo.");
                                }
                                case "e" -> {
                                    if (robotConstruido) {
                                        System.out.println("ya tienes un robot excavador.");
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
                                        System.out.println("robot excavador fabricado y listo para operar.");
                                    } else {
                                        System.out.println("faltan recursos: 15 cobre, 10 magnetita, 5 diamante, 20 acero.");
                                    }
                                }
                                default -> {}
                            }
                        }

                        case "7" -> {
                            if (!jugador.tienePlanos()){
                                System.out.println("no tienes el plano_nave.");
                                break;
                            }
                            boolean ok = true;
                            ok &= jugador.cantidad(ItemTipo.TITANIO) >= 50;
                            ok &= jugador.cantidad(ItemTipo.ACERO) >= 30;
                            ok &= jugador.cantidad(ItemTipo.URANIO) >= 15;
                            ok &= jugador.cantidad(ItemTipo.SULFURO) >= 20;
                            if (!ok){
                                System.out.println("faltan recursos: 50 titanio, 30 acero, 15 uranio, 20 sulfuro.");
                            } else {
                                jugador.quitar(ItemTipo.TITANIO, 50);
                                jugador.quitar(ItemTipo.ACERO, 30);
                                jugador.quitar(ItemTipo.URANIO, 15);
                                jugador.quitar(ItemTipo.SULFURO, 20);
                                System.out.println("has reparado la nave estrellada. victoria!");
                                return;
                            }
                        }

                        case "8" -> {
                            System.out.println("almacen de la nave:");
                            var al = nave.verAlmacen();
                            if (al.isEmpty()) System.out.println(" (vacio)");
                            else al.forEach((t,c)-> System.out.println(" - " + t + ": " + c));

                            System.out.println("depositar todo el inventario del jugador en la nave? (s/n)");
                            if (sc.nextLine().trim().equalsIgnoreCase("s")){
                                nave.depositarTodoDesdeJugador(jugador);
                                System.out.println("deposito completado.");
                            }

                            System.out.println("retirar desde la nave? (s/n)");
                            if (sc.nextLine().trim().equalsIgnoreCase("s")){
                                System.out.print("tipo (igual al enum, ej: cobre): ");
                                String ts = sc.nextLine().trim().toUpperCase();
                                System.out.print("cantidad: ");
                                String cs = sc.nextLine().trim();
                                try {
                                    ItemTipo t = ItemTipo.valueOf(ts);
                                    int cant = Integer.parseInt(cs);
                                    if (cant <= 0){ System.out.println("cantidad invalida."); }
                                    else if (nave.transferirAJugador(jugador, t, cant)){
                                        System.out.println("retiro completado.");
                                    } else {
                                        System.out.println("no hay stock suficiente en la nave.");
                                    }
                                } catch (Exception e){
                                    System.out.println("entrada invalida.");
                                }
                            }
                        }


                        case "9" -> {
                            if (!robotConstruido || robot == null){
                                System.out.println("robot excavador no disponible. fabricalo en crafteos (opcion 6, letra e).");
                                break;
                            }
                            System.out.println("robot excavador:");
                            System.out.println(" 1) extraer en zona actual");
                            System.out.println(" 2) descargar carga en la nave");
                            System.out.println(" 3) reparar robot (4 cables + 3 piezas_metal + 5 magnetita)");
                            System.out.println(" 4) mejorar capacidad (+25%) (10 titanio + 20 cuarzo)");
                            System.out.println(" 5) ver estado de carga");
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
                                        System.out.println("faltan recursos: 4 cables, 3 piezas_metal, 5 magnetita.");
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
                                        System.out.println("capacidad aumentada a " + robot.capacidadCarga() + ".");
                                    } else {
                                        System.out.println("faltan recursos: 10 titanio y 20 cuarzo.");
                                    }
                                }
                                case "5" -> {
                                    var carga = robot.verCarga();
                                    System.out.println("capacidad: " + robot.capacidadCarga() + " | carga: " + robot.pesoActual()
                                            + (robot.danado() ? " | estado: danado" : " | estado: ok"));
                                    if (carga.isEmpty()) System.out.println(" (sin materiales)");
                                    else carga.forEach((t,c)-> System.out.println(" - " + t + ": " + c));
                                }
                                default -> {}
                            }
                        }

                        case "0" -> seguir = false;

                        default -> System.out.println("opcion invalida.");
                    }

                } else {
                    System.out.println("\n[en agua] " + Zonas.nombre(zonaActual)
                            + " | prof: " + jugador.getProfundidad() + " m | o2: " + jugador.getOxigeno().restante());
                    System.out.println("menu (agua):");
                    System.out.println("1) ver estado");
                    System.out.println("2) mover");
                    System.out.println("3) explorar");
                    System.out.println("4) recolectar (elige recurso)");
                    System.out.println("5) volver a la nave");
                    System.out.println("0) salir");
                    System.out.print("opcion: ");
                    String op = sc.nextLine().trim();

                    switch (op) {
                        case "1" -> jugador.verEstadoJugador();

                        case "2" -> {
                            if (zonaActual instanceof NaveEstrellada) {
                                System.out.println("dentro de la nave estrellada no se navega con o2.");
                                break;
                            }
                            int zMin = zonaActual.zMin();
                            int zMax = zonaActual.zMax();
                            System.out.print("nueva profundidad (" + zMin + ".." + zMax + " m): ");
                            String in = sc.nextLine().trim();
                            try {
                                int zNueva = Integer.parseInt(in);
                                if (zNueva < zMin || zNueva > zMax) {
                                    System.out.println("fuera de rango de la zona.");
                                } else {
                                    int zVieja = jugador.getProfundidad();
                                    int costo = zonaActual.costoMoverTurno(jugador, zVieja, zNueva);
                                    jugador.getOxigeno().consumirO2(costo);
                                    jugador.setProfundidad(zNueva);
                                    System.out.println("te mueves de " + zVieja + " a " + zNueva + " m (o2 -" + costo + ")");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("entrada invalida.");
                            }
                        }

                        case "3" -> zonaActual.explorar(jugador);

                        case "4" -> {
                            ItemTipo elegido = seleccionarRecurso(sc, zonaActual);
                            if (elegido == null) {
                                System.out.println("seleccion invalida.");
                            } else {
                                zonaActual.recolectaTipoRecurso(jugador, elegido);
                            }
                        }

                        case "5" -> {
                            enNave = true;
                            System.out.println("vuelves a la nave.");
                        }

                        case "0" -> seguir = false;

                        default -> System.out.println("opcion invalida.");
                    }

                    if (!(zonaActual instanceof NaveEstrellada) && jugador.getOxigeno().restante() == 0) {
                        System.out.println("te quedaste sin o2. pierdes el botin de esta salida y vuelves a la nave.");
                        jugador.vaciarInventario();
                        jugador.getOxigeno().recargarCompleto();
                        enNave = true;
                    }
                }
            }
        }

        System.out.println("fin del juego.");
    }

    /*
     * muestra un menu para que el jugador elija un recurso a recolectar segun la zona actual.
     * @param sc: scanner - el objeto scanner para leer la entrada del usuario.
     * @param z: zona - la zona actual en la que se encuentra el jugador.
     * @return itemtipo: el tipo de item seleccionado por el usuario, o null si la opcion es invalida.
     */
    private static ItemTipo seleccionarRecurso(Scanner sc, Zona z){
        if (z instanceof ZonaArrecife) {
            System.out.println("elige recurso: 1) cuarzo  2) silicio  3) cobre");
            return switch (sc.nextLine().trim()){
                case "1" -> ItemTipo.CUARZO;
                case "2" -> ItemTipo.SILICIO;
                case "3" -> ItemTipo.COBRE;
                default -> null;
            };
        } else if (z instanceof ZonaProfunda) {
            System.out.println("elige recurso: 1) plata  2) oro  3) acero  4) diamante  5) magnetita");
            return switch (sc.nextLine().trim()){
                case "1" -> ItemTipo.PLATA;
                case "2" -> ItemTipo.ORO;
                case "3" -> ItemTipo.ACERO;
                case "4" -> ItemTipo.DIAMANTE;
                case "5" -> ItemTipo.MAGNETITA;
                default -> null;
            };
        } else if (z instanceof ZonaVolcanica) {
            System.out.println("elige recurso: 1) titanio  2) sulfuro  3) uranio");
            return switch (sc.nextLine().trim()){
                case "1" -> ItemTipo.TITANIO;
                case "2" -> ItemTipo.SULFURO;
                case "3" -> ItemTipo.URANIO;
                default -> null;
            };
        } else if (z instanceof NaveEstrellada) {
            System.out.println("elige recurso: 1) cables  2) piezas_metal");
            return switch (sc.nextLine().trim()){
                case "1" -> ItemTipo.CABLES;
                case "2" -> ItemTipo.PIEZAS_METAL;
                default -> null;
            };
        }
        return null;
    }
}








