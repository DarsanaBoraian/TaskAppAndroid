# TaskAppAndroid
**Overview**

The app allows users to add, edit, and manage tasks, along with marking them as favorite. Each task comes with a title, description, status, and timestamp of when it was created or updated.

The app leverages Kotlin, Jetpack Compose for UI, Room for local storage, Firebase Realtime Database for cloud syncing, Hilt for dependency injection, and Coroutines for asynchronous task management.

**Features**

Add Task: Users can create new tasks by providing a title and description.
Update Task: Tasks can be updated with a new title, description, and status.
Task Status: Tasks can be marked as 'Open', 'In Progress', 'Completed', or 'Deferred'.
Mark/Unmark as Favourite: Users can toggle a task as favorite.
Realtime Sync: Task data is synced between local Room database and Firebase Realtime Database for cloud storage.
Task Timestamps: Each task includes a timestamp to track the date and time it was created or updated.

**Technologies Used**

**1. Kotlin**
The entire codebase is written in Kotlin, offering modern and safe programming language features for Android development.
**2. Jetpack Compose**
Declarative UI: Jetpack Compose is used for building the UI components, providing a reactive and flexible interface for users.
LazyColumn: Displays a scrollable list of tasks with the ability to edit, delete, and mark as favorite.
**3. Room Database**
Local Persistence: Room is used as the local database for storing tasks. It provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite.
Entity-DAO Structure: Tasks are represented as Room entities, and data access is handled through DAOs (Data Access Objects).
**4. Firebase Realtime Database**
Cloud Storage: Firebase Realtime Database is integrated to sync tasks with a remote server. This allows real-time updates and cloud-based persistence.
Offline Support: Firebase supports offline caching, so tasks can be edited even when there is no internet connection.
**5. Hilt Dependency Injection**
Simplified Dependency Injection: Hilt is used for injecting dependencies like the ViewModel, Repository, and Database objects. This helps in writing clean, maintainable, and testable code.
**6. Coroutines**
Asynchronous Programming: Kotlin Coroutines are used for managing background threads, ensuring smooth UI performance during database operations and network calls.
Structured Concurrency: Coroutines make it easy to handle long-running tasks, like syncing with Firebase, without blocking the main thread.

**Installation**

To run this project locally, you will need to set up the following.

**Prerequisites**
Android Studio: Download and install the latest version of Android Studio.
Firebase Project: Set up a Firebase project and add the google-services.json file to the app's /app directory.
Gradle: Ensure that your Gradle settings are properly configured for Kotlin, Jetpack Compose, and Hilt.

**Steps**
Clone this repository.
Open the project in Android Studio.
Add your Firebase google-services.json file for Firebase Realtime Database integration.
Sync Gradle: In Android Studio, click on File -> Sync Project with Gradle Files to ensure all dependencies are correctly configured.
Run the app on an emulator or physical device: In Android Studio, click on Run -> Run 'app'.

**App Architecture**
The application follows MVVM (Model-View-ViewModel) architecture with the following layers:

1. Model
The data layer consists of the Task entity class which represents a task, and the DAO (Data Access Object) for accessing the Room database.
The Repository abstracts data sources from both Room and Firebase.
2. ViewModel
The TaskViewModel handles the interaction between the UI and the repository, exposing tasks as a state to the UI via LiveData or StateFlow.
It also handles the business logic for adding, updating, deleting, and marking tasks as favorite.
3. View
The UI is composed of Composable functions in Jetpack Compose.
It includes TaskList to display all tasks, and TaskScreen for adding or updating tasks.
Key Files
Task.kt: Defines the Task data model as a Room entity.
TaskDao.kt: Interface defining database operations.
TaskRepository.kt: Repository to handle data sources (Room and Firebase).
TaskViewModel.kt: ViewModel that interacts with the repository and exposes tasks to the UI.
TaskScreen.kt: Compose screen for adding or updating tasks.
TaskList.kt: Compose screen displaying a list of tasks.

**Dependencies**

Here are the main dependencies used in the project:

groovy
Copy code
dependencies {
    // Kotlin
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"

    // Jetpack Compose
    implementation "androidx.compose.ui:ui:1.2.0"
    implementation "androidx.compose.material:material:1.2.0"
    implementation "androidx.compose.ui:ui-tooling-preview:1.2.0"

    // Room Database
    implementation "androidx.room:room-runtime:2.4.2"
    kapt "androidx.room:room-compiler:2.4.2"
    implementation "androidx.room:room-ktx:2.4.2"

    // Firebase Realtime Database
    implementation platform('com.google.firebase:firebase-bom:31.1.0')
    implementation 'com.google.firebase:firebase-database-ktx'

    // Hilt for Dependency Injection
    implementation "com.google.dagger:hilt-android:2.41"
    kapt "com.google.dagger:hilt-android-compiler:2.41"

    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4'
}
