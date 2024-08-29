# Flickr Photo Gallery App

![Flickr Photo Gallery](https://img.shields.io/badge/Version-1.0-blue)

A modern Android application to browse and view photos from Flickr, featuring a seamless and responsive UI built with Jetpack Compose, along with powerful dependency injection using Koin and infinite scroll functionality for a smooth user experience.

## Features

- **Browse Photos**: Explore a vast collection of photos from Flickr.
- **Search Functionality**: Search for specific photos using keywords.
- **Infinite Scroll**: Automatically load more photos as you scroll down.
- **Photo Details**: View detailed information about each photo.
- **Offline Support**: View cached photos when there’s no internet connection.
- **Modern UI**: Built with Jetpack Compose for a responsive and intuitive UI.
- **Dependency Injection**: Powered by Koin for easy dependency management.
- **Room Database**: Local caching using Room to store photos for offline access.
- **Coroutine Support**: Efficient background tasks handled by Kotlin Coroutines.

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Dependency Injection**: Koin
- **Networking**: FlickrJ-Android
- **Image Loading**: Coil
- **Local Storage**: Room Database
- **Concurrency**: Kotlin Coroutines
- **Navigation**: Jetpack Navigation Compose



### Folder Structure

- `app/src/main/java/com/vsebastianvc/flickr/`:
  - `application/`: Entry point of the application.
  - `data/`: Contains Room database setup, DAOs, and data entities.
  - `di/`: Dependency injection modules for Koin.
  - `navigation/`: Contains navigation components.
  - `network/`: API service and network-related classes.
  - `photodetail/`: UI and ViewModel classes for the photo detail feature.
  - `photogallery/`: UI and ViewModel classes for the photo gallery feature.
  - `repository/`: Repository.
  - `ui/`: Common UI components.
  - `utils/`: Utility classes, including network utilities.


