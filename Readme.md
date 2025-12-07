# Cuju Video Maintenance Application

This project delivers a modern Android experience for capturing short exercise videos, saving them 
locally with metadata, viewing recorded sessions, and simulating video uploads through a 
structured background workflow.

## Prerequisites 
    *   Latest Stable version of Android Studio
    *   Android SDK API Level 32+
    *   Physical Android device (CameraX requires real hardware)

## How to run the application 
    * Clone the repository — link goes here
    * Open the project in Android Studio and allow Gradle to sync all dependencies.
    * Connect a physical Android device and select it from the device dropdown.
    * Press Run to launch the application.

## Architecture OverView
    The app is built using Clean Architecture combined with a reactive MVVM pattern. 
    This approach ensures clear separation of concerns, strong testability, and long-term scalability.

 # Layers
    * Presentation Layer
        1. Activity
        2.Jetpack Compose UI components
        3.ViewModels
        4.UI util
    * Domain Layer
        1.Use cases
        2.Repository interfaces
        3.ViewDataModels
        4.ViewMappers
    * Data Layer
        1.Entities, DAO, Room Database
        2.Repository implementations
        3.Workers (WorkManager)
        4.DataMappers

    # Dependency Injection
        1.Implemented using Koin, consolidating the application’s dependency graph into a clean, 
        maintainable module structure.

## Explain the key decisions you made.
    * Koin for Dependency Injection: Since this is a compact project, Koin provided a faster and 
      more straightforward setup compared to Hilt, helping accelerate development without compromising modularity.
    * Reactive MVVM: StateFlow made UI state handling predictable, streamlined, and responsive—particularly
      during recording and upload simulations.
    * Clean Architecture: Initially considered a simple MVVM setup, but opted for a full clean architecture
      approach because the app required Room database integration, background uploads, and scalable data handling.
      Once the structure was in place, feature development became significantly faster.
    
## Libraries/tools used
    * Android Studio 
    * Kotlin 
    * Jetpack Compose
    * Koin
    * Coroutine
    * Camera X
    * ExoPlayer
    * Room Database
    * Work Maanger

## Limitations and Assumptions 
    * The upload feature uses WorkManager but does not upload to an actual backend. 
      A 3-second delay simulates the upload process.
    * Edge-case failure scenarios are not fully considered.

## Approximate time spent
    * 3 days

    



    