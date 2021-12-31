# FreshWorkStudio

# Developer Note - Run code in [Android Studio Arctic Fox](https://developer.android.com/studio) with Java-11 support inorder to compile code correctly and download required dependencies

## Library and tech stack

- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based
    + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- JetPack
    - Lifecycle - dispose observing data when lifecycle state changes.
    - ViewModel - UI related data holder, lifecycle aware.
    - Room - Store data offline
- Architecture
    - MVVM Architecture (View - DataBinding - ViewModel - Model)
    - Repository pattern
    - Hilt - dependency injection
    - DataBinding - Android DataBinding kit for notifying data changes to UI layers.
    - Coroutines and flow
- DataBinding(Third Party Library)
    - Base Recycler Viewadapter - Used to bind view in adapter
    - Whatif - Plugin gives direct access to scope function without using if conditions
    - Bindables - Observable data binding library
- Material Design & Animations
- [Glide](https://github.com/bumptech/glide) - loading images
- Shared element transition - for activity launch animation
