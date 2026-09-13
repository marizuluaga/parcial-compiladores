# Las cronicas felinas de grafos

Proyecto de **Lenguajes y Compiladores - Universidad EIA**.

## Integrantes

- **Valeria Perez**
- **Mariana Zuluaga**

## Descripcion

La aplicacion resuelve las cuatro misiones planteadas en el parcial mediante una interfaz grafica en Java Swing. Los algoritmos estan separados de la interfaz para que puedan probarse de forma independiente.

Los mensajes especiales de salida, por ejemplo `Case #k: Nina is unreachable`, se conservan exactamente en ingles porque el enunciado indica que se comparan automaticamente caracter por caracter y que no deben modificarse.

## Como ejecutar

Requisitos: JDK 17 o superior y Maven 3.6 o superior.

```bash
mvn compile exec:java
```

Para generar el JAR ejecutable:

```bash
mvn package
java -jar target/feline-graph-chronicles.jar
```

Para ejecutar las pruebas:

```bash
mvn test
```

## Estructura del proyecto

```text
src/main/java/eia/felinegraph/
  Main.java
  algo/   -> algoritmos y estructuras de datos
  io/     -> lectura y validacion de entradas
  gui/    -> interfaz grafica y visualizaciones

src/test/java/eia/felinegraph/algo/
  BfsDfsSolverTest.java
  DijkstraSolverTest.java
  FloydWarshallBellmanFordTest.java
  KruskalSolverTest.java
```

## Mision 1 - BFS y DFS

Se trabaja sobre una cuadricula con bombas. BFS obtiene la ruta minima en numero de movimientos. DFS usa una pila explicita para evitar problemas de desbordamiento en cuadriculas grandes y respeta el orden obligatorio: arriba, abajo, izquierda y derecha.

La visualizacion se muestra hasta 50 x 50 celdas. En casos mayores se calcula la respuesta, pero se omite el dibujo.

## Mision 2 - Dijkstra

Se calcula la ruta de menor costo en un grafo no dirigido con pesos no negativos. Se utiliza `PriorityQueue` para seleccionar el nodo con menor distancia pendiente.

La visualizacion se muestra hasta 60 nodos.

## Mision 3 - Floyd-Warshall y Bellman-Ford

Los dos algoritmos se ejecutan para cada caso. Floyd-Warshall calcula la maxima cantidad de churun entre todos los pares y Bellman-Ford calcula los valores desde el nodo inicial. Tambien se detectan ciclos de ganancia positiva que puedan llevar al destino.

La matriz N x N se presenta en un panel desplazable. Se usa `-` cuando no existe ruta e `inf` cuando el valor es ilimitado. Los resultados de ambos algoritmos se comparan para detectar inconsistencias.

## Mision 4 - Kruskal

Se construye el arbol de expansion minima con Kruskal. Para controlar los componentes se utiliza union-find con compresion de caminos y union por tamano.

## Decisiones de implementacion

- DFS es iterativo para soportar cuadriculas de hasta 1.000 x 1.000.
- Los pesos acumulados usan `long` para evitar desbordamientos.
- No se realizan operaciones aritmeticas con el valor centinela que representa una ruta inexistente.
- No se utilizan librerias de grafos para los algoritmos.
- La interfaz esta separada del paquete de algoritmos.
- En la mision 4 la entrada usa nodos desde 1 hasta N; internamente se convierten a indices desde 0.

## Limitaciones conocidas

La distribucion circular de los grafos prioriza una implementacion sencilla y clara. En grafos densos puede haber cruces entre aristas. Cuando hay demasiadas aristas, algunas etiquetas de peso se ocultan para mantener la visualizacion legible; esto no modifica los calculos.

## Autoras

**Valeria Perez y Mariana Zuluaga**
