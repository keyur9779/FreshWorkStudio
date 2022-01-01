# FreshWorkStudio

# Developer Note - [Run code in Android Studio Arctic Fox with Java-11 support inorder to compile code correctly and download required dependencies](https://developer.android.com/studio)

## Library and tech stack

- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based
    + [Coroutines](https://developer.android.com/kotlin/coroutines)
    + [Flow](https://developer.android.com/kotlin/flow)
- JetPack
    - Lifecycle - dispose observing data when lifecycle state changes.
    - ViewModel - UI related data holder, lifecycle aware.
    - LiveData - observable data holder class, lifecycle aware.
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
- Unit test case
    - robolectric - Used to test android specific test case without using android device
    - mockwebserver - Help to mock http request
    - Mockito - Used for mocking dummy interface
- Material Design & Animations
- [Glide](https://github.com/bumptech/glide) - loading images
- Shared element transition - for activity launch animation
