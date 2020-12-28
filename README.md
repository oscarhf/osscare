## Proyecto OSSECO.Measures.Generator:
* En este proyecto es donde se extraen las métricas desde Eclipse, esto se hace en el paquete gessi.plateoss.osseco.measure_calculator, 
específicamente en la clase MeasureExtractor, solo los métodos de esta clase que tienen definida una instancia de OssecoCrawlerController 
pueden obtener las métricas actuales de Eclipse, las demás lo hacen del dataset que se tiene hasta 2016. Si se desean aumentar nuevas métricas 
este es el lugar.
* En el paquete gessi.plateoss.osseco.updater se encuentra la clase MeasureExtractorUpdater y el método **updateMeasures** que es el que actualiza 
la base de datos de las métricas de Eclipse.
* Es importante saber que en el archivo *config.properties* se encuentran las propiedades para configurar la conexión a la base de datos.  De este proyecto se genera un paquete measuresgenerator.jar
* En mi caso lo incluyo en el mvn local (previamente instalado)  para poder utilizarlo en proyectos tipo maven
mvn install:install-file -Dfile=D:\OSSECOImplentation\jars\measuresgenerator.jar  -DgroupId=org.queso -DartifactId=measuresgenerator -Dversion=0.8 -Dpackaging=jar
	
## Proyecto OSSECOMeasuresRESTServer:
*  Este proyecto es el servidor web de servicios con las métricas crudas de Eclipse, debe incluir en el pom.xml la dependencia al proyecto anterior
```
<dependency>
		<groupId>org.queso</groupId>
		<artifactId>measuresgenerator</artifactId>
		<version>0.8</version>
</dependency>
```	
*  De este proyecto se genera el archivo OSSECOMeasuresRESTServer.war
	
## Proyecto: PLATEOSS.QuESo
* Este proyecto es el que genera los nodos bayesianos
* En el paquete gessi.plateoss.queso se encuentra la clase ModelInstanceJSON y el método **generateFile** que genera el archivo de Eclipse.json  este archivo es generado en el current dir de la aplicación y la subcarpeta /json (String currentDir = System.getProperty("user.dir");) aquí se debe verificar que efectivamente corresponde con la carpeta en donde etan los json de los ecosistemas (el cual se busca en File configDir = new File(System.getProperty("catalina.base")+"/webapps/OSSECOMeasuresRESTServer", "json");)
* De este proyecto se genera el archivo kqisqueso.jar 
* Se incorpora en el mvn local con
	 mvn install:install-file -Dfile=D:\OSSECOImplentation\jars\kqisqueso.jar -DgroupId=org.queso -DartifactId=kqisqueso -Dversion=0.8 -Dpackaging=jar
	
## Proyecto PLATEOSSRESTServer
*  Es el proyecto con el servidor web de las métricas bayesianas y de la instancia del ecosistema

## Que Falta

* Se debe implementar un servicio en background que cada cierto tiempo llame a los metodos updateMeasures  y  generateFile  y luego llame al observer.
