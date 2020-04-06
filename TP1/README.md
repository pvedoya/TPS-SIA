README

** Compilación

En la carpeta del proyecto ya se encuentra el binario TP1.jar listo para ejecutar. 

Si aún así desea compilar su propia versión, simplemente importe el projecto a Intellij (utilizando Java 11), 
y en la sección File->Project Structure->Artifacts, seleccione el signo '+', y seleccione el tipo 'JAR', y 
seleccione la opción 'From modules with dependencies'. Seleccionar la clase Main como clase principal, y dar 'OK'.
Una vez hecho esto dar 'Apply', e ir a Build->Build Artifact->Build para crear el .jar en TP1/out/artifacts/TP1_jar.


** Ejecución

Para ejecurar el programa, correr el comando:
$>java -jar <camino al ejecutable> <archivo de configuración>

En nuestro ejemplo, el camino al ejecutable es TP1/out/artifacts/TP1_jar/TP1.jar.
El archivo de configuración debe ser del estilo "nombre.txt", y debe encontrarse dentro de la carpeta TP1/maps.
El .txt debe respetar la estructura que se encuentra en la siguiente sección.

** Configuración

Si bien el TP cuenta con algunos modelos de mapas con algoritmos ya prefabricados, si quiere hacer uno este es el 
formato que debe respetar:

Línea 1: algoritmo que va a ser usado (BFS, DFS, IDDFS, A*, IDA*, GGS)**.
Línea 2: en el caso de ser un algoritmo informado, especificar que heurística se va a usar (MANHATTAN, SLB, MMLB)**, 
	 si no es informado, la línea 3 se convierte en línea 2.
Línea 3: El tablero, siguiendo el formato que se encuentra en el sitio web presentado en el enunciado del TP.
	 En este se debe respetar el espaciado y lineado que tendrá el tablero en el programa. 

Ejemplos que recomendamos:
1)
BFS
      ###
      #.#
  #####.#####
 ##         ##
##  # # # #  ##
#  ##     ##  #
# ##  # #  ## #
#     $@$     #
####  ###  ####
   #### ####

2)
A*
MMLB
 ###########
##         ##
#  $     $  #
# $# #.# #$ #
#    #*#    #####
#  ###.###  #   #
#  .*.@.*.      #
#  ###.###  #   #
#    #*#    #####
# $# #.# #$ #
#  $     $  #
##         ##
 ###########

**Los algoritmos y heurísticas están detallados en el archivo de presentación del TP.
