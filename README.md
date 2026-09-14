## app rick and morty
## Estas vistas fueron creadas por figma son las siguientes: ( Home, Explore, profile)
<img width="432" height="928" alt="image" src="https://github.com/user-attachments/assets/058ac155-25d4-4499-ad26-270d5094f685" />
<img width="485" height="952" alt="image" src="https://github.com/user-attachments/assets/1dd4cf94-024d-4f4e-bc7b-191bfe0a36b4" />
<img width="436" height="971" alt="image" src="https://github.com/user-attachments/assets/bda806a4-8abc-4890-a07c-a46e57092c2c" />
<img width="1288" height="1011" alt="image" src="https://github.com/user-attachments/assets/169c7500-11a5-4fe2-8ab8-e46ee9f406e2" />
<img width="1823" height="1037" alt="image" src="https://github.com/user-attachments/assets/2d6023a7-519e-476b-a7ec-3eb3816c7ec9" />
# AppRickMorty

App Android nativa en Jetpack Compose que consume la API pública de [Rick and Morty](https://rickandmortyapi.com/) para mostrar personajes, con un diseño oscuro y acento verde neón basado en un prototipo de Figma.

## Stack técnico

- **UI**: Jetpack Compose + Material 3
- **Navegación**: Navigation Compose
- **Red**: Retrofit + Gson
- **Imágenes**: Coil
- **Arquitectura**: MVVM (`ViewModel` + `StateFlow`)
- **Lenguaje**: Kotlin

## Pantallas

| Pantalla | Archivo | Descripción |
|---|---|---|
| Splash | `ui/screens/SplashScreen.kt` | Pantalla de carga con gradiente radial y logo, navega automáticamente a Home tras 2s |
| Home | `ui/screens/HomeScreen.kt` | Lista de personajes reales de la API, con buscador y filtro por status (Alive/Dead/Unknown) |
| Profile | `ui/screens/ProfileScreen.kt` | Perfil de usuario con estadísticas y lista de configuración (switches) |
| Explore | `ui/screens/CharacterScreen.kt` | Grilla de personajes en formato tarjeta, con buscador propio |

Navegación: `Splash → Home ⇄ Profile ⇄ Explore` (bottom nav bar compartida entre las últimas tres).

## Estructura del proyecto

```
app/src/main/java/com/danidev/apprickmorty/
├── data/
│   ├── model/          # RickCharacter, CharacterResponse, Origin
│   ├── remote/         # RickAndMortyApi (Retrofit) + RetrofitClient
│   └── repository/     # CharacterRepository (llama a la API, devuelve Result<>)
├── ui/
│   ├── components/     # BottomNavBar (barra de navegación inferior)
│   ├── navigation/      # AppNavigation (NavHost y rutas)
│   ├── screens/         # Pantallas + sus ViewModels
│   ├── theme/           # Color, Type, Shape, Theme (paleta extraída de Figma)
│   └── viewModel/       # CharacterViewModel (para la pantalla Explore)
└── MainActivity.kt
```

## Cómo correrlo

1. Abrir la carpeta del proyecto en Android Studio.
2. Esperar el sync de Gradle.
3. Ejecutar en un emulador o dispositivo físico (▶️ Run).

No requiere configuración adicional: la API es pública y no necesita API key.

## documentacion hecha con claude 


