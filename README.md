# FreshWorkStudio
  Application is developed with two search implementation which can be tested using below build flavours 
- [Build Flavours]
  + [singleSearch] - This build flavour will load search feature within single launched activity with open close search animation.
  + [activitySearch]- This build flavour will load search feature in new activity with animation
  
## [Developer Note](https://developer.android.com/studio) - Run code in [Android Studio Arctic Fox with Java-11 support](https://developer.android.com/studio) inorder to compile code correctly and download required dependencies
## [Known issue](https://stackoverflow.com/questions/66980512/error-message-android-gradle-plugin-requires-java-11-to-run-you-are-currently) - If java-8 is default java_home you will be see below error while building the APK file
An exception occurred applying plugin request [id: 'com.android.application']
> Failed to apply plugin 'com.android.internal.application'.
> Android Gradle plugin requires Java 11 to run. You are currently using Java 1.8.
You can try some of the following options:
- changing the IDE settings.
- changing the JAVA_HOME environment variable.
- changing `org.gradle.java.home` in `gradle.properties`.
## [Above issue can be resolved by following steps written here or follow steps mentioned below in android studio](https://stackoverflow.com/questions/66980512/error-message-android-gradle-plugin-requires-java-11-to-run-you-are-currently)
[Steps](https://stackoverflow.com/questions/66980512/error-message-android-gradle-plugin-requires-java-11-to-run-you-are-currently) Go to Preferences(Android studio Left top)-->Build,Execution,Deployment-->Build Tools-->Gradle-->Gradle JDK (update to JDK 11)-->APPLY-->OK


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
    - Mockito - Used for mocking dummy class or data(interface)
- Material Design & Animations
- [Glide](https://github.com/bumptech/glide) - loading images
- Shared element transition - for activity launch animation
