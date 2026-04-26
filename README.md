# Lab-Backend-FullStack

# Como Ejecutar
Tras haber clonado el repositorio se debe abrir cada proyecto con tu editor favorito, para las pruebas se ha usado Netbeans e IntelliJ Community, en cualquiera funcionó, incluso un grupo en netbeans y otro en intellij.

Para cada proyecto requiere tener el JDK-21 instalado en tu máquina.

Para cada proyecto debes descargar las dependencias, si unas netbeans le das clic sobre la carpeta dependencies->download declare dependencies. En intellij debes correr maven en el icono que aparece al lado derecho.

Debes levantar los proyectos en este orden.

1.- EurekaServerApp
2.- MsBooksCatalogue
3.- MsBookPayments
4.- CloudGatewayProxy

No se ha probado otro orden

# CloudGatewayProxy (NOOB)
Utiliza Cloud Gateway para crear un proxy inverso que será el punto de entrada de cualquier cliente a nuestro back-end, éste solo redirige las peticiones desde el frontal hacia las correspondientes GET, POST, PUT, PATCH o DELETE en cada microservicio.

# EurekaServerApp
Para cumplir: A su vez, ambos microservicios deberán registrarse automáticamente en su arranque en un servidor de Eureka, por lo que cualquier petición HTTP entre operador y buscador tiene que hacerse utilizando nombres relativos y no se debe incluir en ningún caso una dirección IP y un puerto.

# MsBooksCatalogue
Es el encargado de acceder y tratar con una base de datos que contendrá los libros de nuestra aplicación. La API que exponga este microservicio debe permitir, además de crear, modificar (total o parcialmente) o eliminar ítems, buscar por todos los atributos de un libro (de forma individual o combinada). Estos son el título, el autor, la fecha de publicación, la categoría, el código ISBN, la valoración (nota de 1 a 5) y la visibilidad (algunos libros pueden estar ocultos y, por tanto, el front-end no debería mostrarlos nunca). La base de datos relacional utilizada es H2.

# MsBookPayments
Es el encargado de ejecutar la acción principal de la aplicación (registrar compras). Para ello, realizará peticiones HTTP al microservicio buscador siempre que se necesite validar aquellos ítems sobre los que se está ejecutando una operación (principalmente, que existen y que están en un estado correcto, como, por ejemplo, que haya stock o que no estén ocultos). El resultado (el acuse de que la compra se ha realizado) se debe persistir en una base de datos relacional, para este cometido se ha usado H2.

# Extra: GatewayTranscripcionPeticiones (PRO)
Similar al Cloud Gateway para crear un proxy inverso que será el punto de entrada de cualquier cliente a nuestro back-end, con la particularidad que este transcribe peticiones POST desde el frontal hacia las correspondientes GET, POST, PUT, PATCH o DELETE en cada microservicio. 

