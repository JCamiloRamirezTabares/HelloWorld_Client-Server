# "Hello World" Aplicacion de Servidor/Cliente

Esta es una implementacion basica del modelo de software Servidor/Cliente

Aunque su nombre es "Hello World" esta implementacion no tiene nada que ver con la famosa cadena de texto. Su nombre es debido a que se utilizo como base el codigo titulado "Hello World" que fue proporcionado por los tutores a cargo del curso de Arquitectura de Software. En el cual se mostraba una implementacion basica del modelo Servidor/Cliente donde el servidor respondia con la famosa frase "Hello World" al comunicarse con el cliente.

Esta implementacion soporta los siguientes comandos:
- **[numero entero]**                    ==> Al ingresar un numero entero positivo, el servidor retornara la sucesion de fibonacci del numero ingresado.
- **listifs**                            ==> Este comando hara que el servidor muestre en consola las interfaces logicas que tiene configuradas.
- **listports [Alguna direccion IPv4]**  ==> Este comando hara que el servidor muestre en consola los puertos y servicios abiertos en dicha direccion.
- **! [Alguna cadena]** ==> El servidor mostrara en consola el resultado de ejecutar [Alguna cadena] en el sistema.
- **list clients** ==> Lista el numero de clientes registrados en el servidor.
- **register** ==> Registra al cliente actual en el servidor.
- **to [client] [Mensaje]** ==> Envia [Mensaje] a [client], si y solo si, [client] esta registrado en el servidor.
- **BC [Mensaje]** ==> Envia [Mensaje] a todos los clientes registrados en el servidor (incluido el client actual).
