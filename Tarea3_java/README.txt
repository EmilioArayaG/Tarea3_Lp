Nombre alumno: Emilio Araya
Rol alumno: 202473561-1

Instrucciones para compilar y ejecutar el programa correctamente
1.Requisitos precompilacion
    -requisitos: jdk 17 y make (probado en wsl ubuntu y linux nativo, se recomienda en estas plataformas)
    -archivos necesatios:
        -Makefile
        -Main.java
        -entorno/: Zona.java, ZonaArrecife.java, ZonaProfunda.java, Zonas.java, ZonaVolcanica.java, NaveEstrellada.java
        -objetos/: AccesoProfundidad.java, Item.java, ItemTipo.java. NaveExploradora.java, RobotExcavador.java, Vehiculo.java
        -player/: Jugador.java, Oxigeno.java
    -Se exige mantener los archivos como vienen ya que al sacarlos de una carpeta o cambiar archivos de lugar esto puede comprometer el funcionamiento del programa

2.Compilar y ejecutar (2 Casos ambos sirven)
Caso a) Usar Terminal de wsl ubuntu o linux
    0- ir a la carpeta del proyecto (donde esta el makefile) con cd~/ladireccioncorrespondiente/...
    1- instalar toolchain : sudo apt update && sudo apt install openjdk-17-jdk make
    2- compilar con makefile : (comando) make 
    3- ejecutar: make run
    4- limpieza opcional: make clean

Caso b) Usar visual studio code (So ideal: wsl en windows ya que fue probado en este so)
    1- abrir carpeta del proyecto en vs code
    2- usar terminal integrada (view->terminal) y ejecutar:
        make 
        make run 
        make clean (al terminar opcional)

Es de SUMA importancia asegurarse que en ambos casos se encuentren en la carpeta del proyecto para compilar y ejecutar de lo contrario no sera posible 
Tambien como fue antes mencionado no mover nada a una carpeta o desde una carpeta ya que esto tambien compromete el funcionamiento del programa  

3. Instrucciones de juego y asunsiones
- el programa funciona por menus en consola.
- ingresar exactamente una opcion por linea y presionar enter.
  * en nave: numeros 0..9, y en crafteos letras a..e.
  * en agua: numeros 0..5.
- para recolectar por tipo, se muestra un sub-menu con opciones numericas (1..n) segun la zona.
- entradas invalidas (letras donde van numeros, numeros fuera de rango, textos como "12" esperando dos acciones) se marcan con "opcion invalida" o "entrada invalida" y no se ejecuta la accion.
- no ingresar multiples acciones en una sola linea (ej: "123"); la linea completa se toma como una sola opcion y puede rechazarse.
- mover la nave entre zonas no consume o2.
- al salir al agua, el jugador aparece exactamente en el anclaje definido para esa zona.
- almacen de la nave:
  - ver inventario de la nave
  - depositar todo el inventario del jugador
  - retirar por tipo y cantidad desde la nave al jugador
- si el o2 del jugador llega a 0 en el agua:
  - vuelve a la nave con o2 lleno
  - pierde solo el inventario del jugador
  - el almacen de la nave no se toca
- crafteos tienen que ser con el inventario del jugador, si estan en la nave no se detectaran y necesitan ser transportados al jugador
- persistencia:
  - estado no se guarda en disco; cada ejecucion empieza en limpio.
- los items clave para la progresion como la PIEZA_TANQUE, el MODULO_PROFUNDIDAD y el PLANO_NAVE tienen un stock limitado por partida. Una vez que se obtienen todos, no volveran a aparecer.
- funcionamiento robot: 
    -las acciones del Robot Excavador (extraer, reparar, etc.) se realizan desde la seguridad de la nave y no consumen oxigeno del jugador.