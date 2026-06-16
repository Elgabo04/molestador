Planificación de gaynert (asi se llama para mi por ahora)



Fuentes para decidir colores:



interfaces para personas con tdah: https://es.linkedin.com/advice/3/how-can-you-design-web-application-interface-users-tvuje?lang=es



https://medium.com/design-bootcamp/designing-for-adhd-users-a-psychology-informed-approach-d2fc055d5e33





https://www.researchgate.net/publication/368668751\_Interfaces\_de\_Usuario\_en\_Aplicaciones\_de\_Apoyo\_Didacticas\_para\_Ninos\_con\_TDAH\_Hacia\_el\_Analisis\_de\_la\_Carga\_Cognitiva\_en\_IHC



objetos gosus: https://www.understood.org/es-mx/articles/fidgets-for-kids-with-adhd

colores\_gosus: https://titula.universidadeuropea.com/bitstream/handle/20.500.12880/2583/VILLALBA%20VILLALBA%20Alejandra%20Del%20Valle.pdf?sequence=1\&isAllowed=y



estudio de colores:https://pmc.ncbi.nlm.nih.gov/articles/PMC4401829/pdf/main.pdf  (según esto dice que azul y amarillo cuesta mas, rojo y otros son mas seguros)



Todas las fuentes que usé las coloqué en el drive (creo)





REQUISITOS PARA UNA BUENA INTERFAZ



\-Navegación clara

\-Diseños claros y coherentes

\-Existencia de jerarquía visual y contraste

\-Presencia de iconos e imágenes de respaldo de contenido

\-Color y animación moderada y determinada

\-Destacar puntos clave y llamadas a la acción

\-División de tareas complejas en pasos manejables

\-Proporcionar instrucciones y consejos claros y consisos

\-Gamificación y recompensas



\---xd---

\-Minimizar distracciones

\-Apoyar la atención

\-Ayudar a centrarse en la información y acciones mas importantes y relevantes

\-Adaptarse a sus preferencias

//El texto dice que nos adaptemos a sus preferencias personalizando su experiencia de acuerdo a sus necesidades, como varios métodos de entrada y salida (esto podría ser audio y texto al crear sus tareas), niveles de dificultad (aun no se me ocurre como integrar esto xd), personalización de colores, fuentes, sonidos, etc. permitir la pausa, reanudación y omision de tareas y proporcionar opciones y características de accesibilidad (esto ultimo de pausa, reanudación y omision de tareas me parece que no puede ser, ya que el usuario se acostumbraría a dejar de lado sus responsabilidades al permitir la aplicación estas acciones y lo que se busca lograr es todo lo contrario xd)











//Características para un objeto gosu (Ruido si hace nuestro proyecto, color tendrá supongo, sonido es lo mismo o me parece xd, almenos uno tendrá la maqueta del robot de hora de aventura supongo)



\-Ruido

\-Color

\-Sonido



\-pegajoso

\-viscoso

\-aspero



(Objetos estirables, giratorios, alternación de colores)





//colores candidatos gosus

\-tonalidades calidas













Decisión de colores:



**1. Villalba (2022)** - El que más habla de esto

El estudio muestra que el amarillo ralentiza las respuestas. Pero ojo: eso es cuando el amarillo es un distractor irrelevante.



Aplicación: Si el amarillo es importante (ej. un botón de "enviar" o una alerta crítica), entonces no es un distractor, es el objetivo. En ese caso, su capacidad de capturar atención podría ser útil, no problemática.



El estudio no probó esta situación (amarillo como objetivo importante vs. distractor). Así que no podemos afirmar nada con seguridad.



**2. Los otros estudios (Kim 2013, Kim 2015)**

No midieron "captura de atención intencional". Midieron discriminación de color y respuesta cerebral pasiva.



**3. Vivanco (2025) - Revisión sistemática**

Menciona que el uso de color (sin especificar cuál) puede ser útil para:



* Destacar información clave
* Guiar la atención
* Reducir la carga cognitiva
* Pero no da colores específicos.



4\. LinkedIn (artículo)

Sugiere usar color con moderación y determinación, destacar puntos clave y llamadas a la acción. Pero no es evidencia científica.





|Necesidad|Conclusión basada en archivos|Color sugerido (de los que aparecen en las fuentes xd)|
|-|-|-|
|Elemento muy importante (ej. botón de pago, alerta crítica)|Usa un color que destaque del fondo. Pero evitar el amarillo si hay riesgo de que se confunda con distractores.|Rojo (aparece en estudios, sin déficit reportado) o Naranja (menos ralentizador que amarillo)|
|Elemento secundario pero relevante|Usar un color que no compita con el principal.|**Verde** o Gris|
|Fondo o elementos no importantes|Usar colores neutros que no capturen atención.|Gris (el menos ralentizador según Villalba)|
|Llamar la atención sin sobresaturar|Usar contraste de brillo o tamaño además de color. Los archivos no hablan de esto, pero es sentido común.|Cualquier color con alto contraste sobre el fondo|





Importante: Usaré el amarillo con precaución porque es un color difícil de ignorar, si no usamos bien podríamos distraer al usuario, entonces solo tendrá papel en acciones importantes o donde necesitamos que tome atención.

EYE: El color azul según el kim en 2013 y 2015, dice que es mas difícil distinguir sus tonos, por lo que no se recomienda usar muchos tonos de azul, usar uno con alto contraste es permitido o aceptado.







Descripción del sistema: El usuario mediante una aplicación móvil/web, inicia sesión para posteriormente crear y configurar sus tareas académicas, cuando esté configurado, la configuración del frontend se manda a un backend para posteriormente guardarlos en una bd de supabase, el api tiene un cron configurado cada 30 segundos para ejecutar la validación de tareas del usuario  y el dispositivo vinculado al celular (que es enlazado mediante un qr posiblemente), entonces al recibir los datos, el dispositivo recibe los datos ya ordenados, cuando haya tareas próximas (5 min para vencer por ejemplo) empieza a lanzar alertas sutiles, luego cuando se cumpla y venza la fecha de vencimiento, entonces suelta alertas mas fuertes para llamar la atención, cuando venza la tarea, dicha tarea aparece en la pantalla y mediante un botón, se posterga 1 hora automáticamente, y si decide validarlo entonces con otro botón se activa la validación para esa tarea, al hacer click se abre la cámara con un temporizador de 3 segundos, al llegar a 0 se toma la foto y se envia automáticamente al backend para que este lo redirija a la IA para su validación(de la foto), si se valida entonces el estado de la tarea cambia a cumplido y finaliza, si es rechazado, entonces sigue molestando el muñequito. Las tareas que valida se ordenan según la mas próxima a vencer, se manejara en forma de colas, la mas próxima a vencer se muestra en pantalla, entonces si el usuario decide validar una tarea que aun no vencio, apreta botón de tomar foto, entonces se toma la foto y esa foto se vincula respecto a la tarea mas reciente y se valida, si hubiese el caso que necesita priorizar la validación de una tarea mas importante que tiene una fecha de vencimiento mas lejana a otras, esas otras pueden ser reprogramadas una hora mas cuando suene la alarma para esas tareas, asi tiene conocimiento que a pesar de la mas importantes, también tiene las otras que tiene que cumplir. Cuando se valida una tarea se manda su id y se cambian los estados que son 3 valores de enum ("realizado", "en proceso", "postergado), el valor de postergado al menos yo tengo la intención de usarlo para mostrar al usuario como un resumen del mes de cuantas veces se postergó una tarea. Las tareas vencidas tienen prioridad sobre las próximas a vencer, porque las vencidas ya debieron ser hechas, pero las próximas tienen aun plazo, entonces si no se quiere hacer aun se reprograma, la pila es ordenada según a las fechas de vencimiento, siendo la punta la mas próxima a vencer en ese orden, al crearse una tarea se empieza con una de "en proceso" para representar a que esta en proceso a cumplirse, no pendiente. Son dos botones físicos de dos colores para evitar manejar eventos posiblemente complejos con un solo botón. El cron y validaciones de cumplimiento se manejan en el backend, lo que maneja esp32 mostrar las tareas, enviar imágenes de las pruebas y postergar las fechas de vencimiento de las tareas ya vencidas (con esto actualizar su estado al que corresponde para postergado), mientras que el backend enlista por fecha las que estan pendientes para mandarselo al dispositivo, envia la imagen y recibe respuesta de ia para asi cambiar el estado de una tarea (si fue cumplida o no){este proceso creo que deberia enviar un mensaje de confirmación por el dispositivo para avisar que fue validada correctamente o no, lo mismo para postergaciones en el asp32}, maneja sesiones y guarda historiales.













Flujo de la app movil

1. Pantalla con boton para autenticarse con google
2. Dashboard con navegaciones para añadir tareas y visualizar el historial;boton al principio si no se tiene vinculado un dispositivo donde pueda hacer click para escanear un QR para vincular el dispositivo, luego de esto desaparece
3. Dentro del dashboard con navegaciones, tambien aparece el nombre y boton para cerrar sesión.



Reglas de negocio:

Una vez escaneado un qr se enlaza el codigo en la base de datos a los registros asociados del usuario

No se pueden eliminar tareas creadas

Se pueden postergar tareas libremente (no solo 1 hora como en el dispositivo)

En la app, una tarea realizada desaparece de la sección tareas, pero se muestra en el historial como "realizado"





Flujo de la app web(lo mismo)

1. Pestaña de autenticación
2. Dashboard de usuario con todo lo del movil pero para desktop
3. Lo mismo que la app web pero en el navbar







Ambas apps tienen indicador de conexión al dispositivo y notificaciones de vencimiento de tareas(para complementar), solo la aplicación web muestra graficos de las tareas cumplidas y postergadas, en el celular no debido al espacio limitado que se tiene.





Paleta de colores:

Rojo: FC3D03 (para indicadores de tareas vencidas)

Verde: 5DD62C (puede variar la intensidad para comodidad visual) (para editar, añadir y guardar tareas)

Amarillo: F6F637 -> complemento azul si es que se requiere (poquisimo uso): 3737F6  (tareas próximas a vencer el amarillo) (azul para aviso de numero de veces de tareas postergadas)

Gris: E4E9FB (fondo)

cancelaciones es solo texto con gran área de pulsación pero sin fondo que compita con otros botones.

Rojo para indicadores de notificaciones de tareas vencidas









Tecnologías para:



backend (API):

* Express
* Node.js

FrontEnd:

* React
* Axios
* Tailwind.css
* recharts

Gráficos a mostrar

* Gráfico de barras de cumplidas vs postergadas
* Gráfico de pastel circular para mostrar proporción de tareas realizadas, en progreso y postergados

Google auth para sesiones y base de datos supabase



