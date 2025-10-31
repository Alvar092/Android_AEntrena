## Dragon Ball Fight :flame:

### ¿Qué es Dragon Ball Fight? 

Es una aplicación desarrollada en Kotlin, aplicando una arquitectura MVVM(Model-View-ViewModel) para mantener una clara separación de responsabilidades entre la interfaz, la lógica de negocio y la gestión de datos. 

### ¿Cómo funciona Dragon Ball Fight?

- Al iniciar la aplicación aparecerá una pantalla de login donde tendras que introducir las siguientes credenciales: 

Usuario: entrenah92@gmail.com

Password: asdfgh

- Al iniciar sesión, aparecerá un listado de heroes donde puedes elegir tu heroe favorito, así como la opción de curarle a todos la vida. 

- Si selecciones un heroe, podrás ver su vida, curarle o hacerle daño. 

### Arquitectura general 

· Model: Contiene las clases de datos (Hero, HeroDTO) y la lógica de red implementada en HeroRepositoryImpl, que maneja peticiones HTTP a la API. 

· ViewModel: GameViewmodel gestiona el estado de la interfaz mediante StateFlow y LiveData, lanzando corrutinas con viewModelScope para operaciones asíncronas. 

· View: las activities/fragments observan el estado del viewModel y actualizan la interfaz en consecuencia. 

· Se emplea un repositorio como capa intermedia entre la lógica de dominio y la fuente de datos remota. 

### Frameworks y librerias 

- Android Jetpack: gestión del ciclo de vida y estado de la UI con ViewModel y LiveData, acceso seguro a las vistas del layout con ViewBinding. 

- Kotlin: Corrutinas y StateFlow para el manejo de concurrencia estructurada y flujos reactivos, serialización de datos. 

- Inyección de dependencias con Koin: gestión ligera de dependencias, permitiendo inyección del repositorio en el viewModel. 

- Testing: JUnit para el testing unitario, MockWebServer para simular pruebas de red, Coroutine tests para validar comportamientos asíncronos. 

### Clona este repositorio! 

git clone https://github.com/Alvar092/Android_AEntrena

