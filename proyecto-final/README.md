1. Abre una terminal en la ubicación del proyecto.
2. Ejecuta el comando `gradle build` para construir el proyecto. Esto compilará el código fuente y generará los archivos de salida en la carpeta `build`.
3. Luego, ejecuta el comando `gradle deploy` para crear los archivos de ejecución que serán alojados en la carpeta `build/deploy` de cada subproyecto. Esto generará los archivos necesarios para ejecutar el proyecto.
4. Una vez que se haya completado el proceso de construcción y despliegue, puedes encontrar los archivos de ejecución en la carpeta `build/deploy` de cada subproyecto.
5. Dentro de la carpeta `build/deploy` de cada subproyecto encontraras un archivo Zip que podras descomprimir.
6. Al descomprimir los archivos Zip para el Master y Worker, podras ejecutar en la linea de comandos (ubicandote donde esta el Zip) el comando `java -cp "./*" Master` o `java -cp "./*" Worker`