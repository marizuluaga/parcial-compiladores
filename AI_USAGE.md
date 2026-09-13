# Uso de herramientas de IA

## Integrantes

- Valeria Perez
- Mariana Zuluaga

## Herramientas utilizadas

Durante el desarrollo se utilizo **Claude** como herramienta de apoyo para interpretar requisitos, proponer una estructura inicial del proyecto, revisar implementaciones, apoyar la construccion de la interfaz y plantear casos de prueba. El codigo final fue revisado con base en el enunciado y en los resultados esperados de cada mision.

## Prompts que fueron importantes

1. Se solicito apoyo para organizar el proyecto a partir del enunciado, separando algoritmos, lectura de datos e interfaz grafica.
2. Se pidio revisar la ejecucion de la interfaz para identificar errores que solo aparecieran al iniciar la aplicacion.
3. Se pidio contrastar las salidas de los algoritmos con los ejemplos del enunciado para verificar que coincidieran exactamente.

## Casos que requirieron correccion

### 1. Error al iniciar algunos paneles

En una version inicial, la clase base de los paneles intentaba construir la zona de visualizacion antes de que terminaran de inicializarse los atributos propios de cada panel. Esto podia producir un `NullPointerException`. Se corrigio dejando la inicializacion completa de la interfaz para el final del constructor de cada panel mediante `initUi()`.

### 2. Reconstruccion de rutas en Floyd-Warshall

La primera implementacion calculaba correctamente la matriz de valores, pero no conservaba informacion suficiente para reconstruir la ruta que debia mostrarse en la interfaz. Se agrego una matriz de siguientes saltos para poder recuperar y resaltar la ruta correspondiente. Tambien se utilizo para apoyar la visualizacion de ciclos de ganancia positiva.

## Aprendizajes del equipo

**Valeria Perez:** completar antes de la entrega con un aprendizaje real que pueda explicar durante la sustentacion.

**Mariana Zuluaga:** completar antes de la entrega con un aprendizaje real que pueda explicar durante la sustentacion.

## Verificacion realizada

Se revisaron los casos de ejemplo de las cuatro misiones y la estructura general del proyecto. Antes de entregar, el equipo debe ejecutar nuevamente `mvn test`, abrir la interfaz, probar cada ejemplo y verificar que ambas integrantes puedan explicar y modificar cualquier parte del codigo.
