# FreshWorkStudio

## Library and tech stack
- Minimum SDK level 23
- [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- JetPack
  - Lifecycle - dispose observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - Repository pattern
  - Hilt - dependency injection
  - DataBinding - Android DataBinding kit for notifying data changes to UI layers.
- Material Design & Animations
- [Glide](https://github.com/bumptech/glide) - loading images
- Shared element transition - for activity launch animation
