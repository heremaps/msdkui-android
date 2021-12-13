# HERE Mobile SDK UI Kit (MSDKUI) Contribution Guide

This guide is for developers who want to contribute to the MSDKUI codebase, build the MSDKUI library, or run the demo application on their local machines. For using the `MSDKUILib.aar` on your own project, or running the accompanying example apps, please check the [QuickStart](QuickStart.md) guide.

## Contents

- [Getting the code](#getting-the-code)
- [See available Gradle tasks](#see-available-gradle-tasks)
- [Building the MSDKUI library](#building-the-msdkui-library)
	- [Building the MSDKUI API Reference from the command line](#building-the-msdkui-api-reference-from-the-command-line)
	- [Running unit tests for the MSDKUILib](#running-unit-tests-for-the-msdkuilib)
	- [Generating a unit test coverage report](#generating-a-unit-test-coverage-report)
- [Building the Demo app](#building-the-demo-app)
	- [Setting the HERE Mobile SDK credentials](#setting-the-here-mobile-sdk-credentials)
	- [Building and Running the Demo](#building-and-running-the-demo)
	- [Running unit tests for the MDKUIApp](#running-unit-tests-for-the-mdkuiapp)
	- [Generating a unit test coverage report](#generating-a-unit-test-coverage-report)
	- [Running UI tests for the MDKUIApp](#running-ui-tests-for-the-mdkuiapp)
	- [Optional: Building the MSDKUI Library and the Demo as part of one project](#optional-building-the-msdkui-library-and-the-demo-as-part-of-one-project)
- [Building the Dev app](#building-the-dev-app)
- [Commit policy](#commit-policy)
	- [Writing Git commit messages](#writing-git-commit-messages)
		- [A normal ticket](#a-normal-ticket)
		- [Solving multiple tickets](#solving-multiple-tickets)
- [Submitting a pull request](#submitting-a-pull-request)


## Getting the code

```
$ git clone https://github.com/heremaps/msdkui-android
$ cd msdkui-android
```

## See available Gradle tasks

The HERE SDK UI Kit for Android is built using Gradle. For an overview of the available tasks, switch to command line and execute:
- ./gradlew task from MSDKUIKIT folder to see all lib related tasks
- ./gradlew task from MSDKUIDemo folder to see all Demo app related tasks

## Building the MSDKUI library

The easiest way to build the MSDKUI library (MSDKUILib) is using the command line:
- Put the `HERE-sdk.aar` file from your HERE SDK package to _MSDKUIKit/libs/_.
- Build the MSDKUI library via Gradle. Navigate to the _MSDKUIKit_ folder and execute: `./gradlew clean :MSDKUILib:assembleRelease`.

>**Note:** Please, make sure to set `ANDROID_HOME` to your environment `PATH` variable. It should point to the location where you have installed the Android SDK.

The built `MSDKUILib-release.aar` is then located in your new _MSDKUIKit/MSDKUILib/build/outputs/aar/_ subfolder.

>**Note:** You can also build the lib from within Android Studio. Open the MSDKUIKit Android project and open the Gradle "Tool Windows" to see all available Gradle tasks. Select and execute the `:MSDKUILib:assemble` task.

### Building the MSDKUI API Reference from the command line

To generate the MSDKUI API Reference, navigate to the _MSDKUIKit_ folder and run:

```
./gradlew clean :MSDKUILib:javadocJar
```

It will place the API Reference at `build/docs/javadoc/`. Run `open build/docs/javadoc/index.html` to open the documentation on the default browser. The Javadoc Jar can be found at `build/libs/`.

### Running unit tests for the MSDKUILib

Switch to command line and navigate to the _MSDKUIKit_ folder. Execute: `./gradlew :MSDKUILib:testDebugUnitTest`.

>**Note:** Alternatively, you can also run the tests from within Android Studio by right clicking on a specific test or the _test/msdkui_ subfolder and clicking "Run Tests". You can also directly select the Gradle Task: `MSDKUILib -> Tasks -> verification -> test` (right-click and run).

### Generating a unit test coverage report

Execute `/gradlew :MSDKUILib:testDebugUnitTest :MSDKUILib:jacocoTestDebugReport` to run all unit tests _and_ to generate a coverage report. The report will be available in _MSDKUILib/build/reports/jacoco/jacocoTestDebugReport_. Open `index.html` to see an overview of the current code coverage.

>**Note:** To generate the coverage report from within Android Studio, execute the Gradle Task from the "Tools Window": `MSDKUILib -> Tasks -> reporting -> jacocoTestDebugReport` (right-click and run).

## Building the Demo app

Before building, testing, or running the MSDKUI Demo application it's important to set the HERE Mobile SDK credentials. If you don't know your credentials, please ask your HERE stakeholder or register on [developer.here.com](https://developer.here.com) and create new ones.

### Setting the HERE Mobile SDK credentials

Don't set your credentials directly to the Android manifest (as otherwise you would need to exclude them for every commit). Instead add `MSDKUI_APP_ID_ANDROID`, `MSDKUI_APP_TOKEN_ANDROID` and `MSDKUI_APP_LICENSE_ANDROID` to your OS `$PATH`, see _MSDKUIDemo/MSDKUIApp/build.gradle_ for details.

Alternatively, you can replace "APP_ID", "APP_TOKEN" and "APP_LICENSE" in _MSDKUIDemo/MSDKUIApp/build.gradle_:
```
resValue 'string', 'HERE_SDK_APP_ID', System.getenv("MSDKUI_APP_ID_ANDROID") ?: "APP_ID"
resValue 'string', 'HERE_SDK_APP_TOKEN', System.getenv("MSDKUI_APP_TOKEN_ANDROID") ?: "APP_TOKEN"
resValue 'string', 'HERE_SDK_LICENSE_KEY', System.getenv("MSDKUI_APP_LICENSE_ANDROID") ?: "APP_LICENSE"
```

When you register on [developer.here.com](https://developer.here.com), the registered bundle identifier must match the [applicationId](https://developer.android.com/studio/build/application-id) in the app's build.gradle. For the demo app this is by default `com.here.msdkui.demo` - you may need to adapt the `applicationId` to the one you have registered.

### Building and Running the Demo

In order to build and to run the demo app with Android Studio, you need to integrate the HERE Mobile SDK (Premium Edition) version 3.19. Additionally, you need to integrate the MSDKUI library.

- Put the HERE-sdk.aar file from your HERE SDK package and the MSDKUI library to _MSDKUIDemo/libs/_.
- Open and build the project located at _MSDKUIDemo/_ in Android Studio.
- Run it on a device or emulator of your choice.

>**Note:** Alternatively, you can build the Demo from command line. Navigate to the MSDKUIDemo folder and execute: `./gradlew clean :MSDKUIApp:assembleRelease`. The built Demo APK is then located in your new _MSDKUIDemo/MSDUIApp/output_ subfolder. Copy the APK to your device and run it.

### Running unit tests for the MDKUIApp

Switch to command line and navigate to the _MSDKUIDemo_ folder. Execute: `./gradlew :MSDKUIApp:testDebugUnitTest`.

>**Note:** Alternatively, you can also run the tests from within Android Studio by right clicking on a specific test or the _test_ subfolder and clicking "Run Tests". You can also directly select the Gradle Task: `MSDKUIApp -> Tasks -> verification -> test` (right-click and run).

### Generating a unit test coverage report

Execute `/gradlew :MSDKUIApp:testDebugUnitTest :MSDKUIApp:jacocoTestDebugReport` to run all unit tests _and_ to generate a coverage report. The report will be available in _MSDKUIApp/build/reports/jacoco/jacocoTestDebugReport_. Open `index.html` to see an overview of the current code coverage.

>**Note:** To generate the coverage report from within Android Studio, execute the Gradle Task from the "Tools Window": `MSDKUIApp -> Tasks -> reporting -> jacocoTestDebugReport` (right-click and run).

### Running UI tests for the MDKUIApp

Switch to command line and navigate to the _MSDKUIDemo_ folder. Execute: `./gradlew MSDKUIApp:connectedAndroidTest`.

>**Note:** Alternatively, you can also run the UI tests from within Android Studio by right clicking on a specific test or the _androidTest_ subfolder and clicking "Run Tests". You can also directly select the Gradle Task: `MSDKUIApp -> Tasks -> build -> assembleAndroidTest` (right-click and run).

### Optional: Building the MSDKUI Library and the Demo as part of one project

Usually, it should be sufficient to keep library and Demo project separated. If you are planning to contribute to the Demo app - or to test your new components as part of the Demo app - it may be convenient to build both as part of one Android Studio project.

As the first step, open the MSDKUIDemo project in Android Studio. In _MSDKUIDemo/settings.gradle_, add the following to include the lib project:
```java
include ':MSDKUILib'
project(':MSDKUILib').projectDir = new File('../MSDKUIKit/MSDKUILib')
```

In _MSDKUIDemo/MSDKUIApp/build.gradle_, comment the first line and add the 2nd line to ensure that the lib is built locally (no need to it add manually):
```java
 //implementation(name: 'MSDKUILib', ext: 'aar')
 implementation(project(path: ':MSDKUILib'))
```

In _MSDKUIDemo/build.gradle_ add the following build script dependency:
```java
classpath 'org.jfrog.buildinfo:build-info-extractor-gradle:4.4.15'
```

## Building the Dev app

This project includes a Dev application, which is designed to display MSDKUI views in their raw form. While the Demo app offers a polished experience of the MSDKUI components, it doesn't show all possible states of a view.

1. Put the HERE-sdk.aar file from your HERE Mobile SDK package _and_ the MSDKUI library to `MSDKUIDev/libs/`. Make sure the library is named "MSDKUILib.aar" to match the dependencies in the app's `build.gradle` file.
2. Open and build the project located at `MSDKUIDev/` in Android Studio.
3. Run it on a device or an emulator of your choice.

Note that the Dev app does not require HERE credentials.

## Commit policy

Please follow our commit policy. Once you have pushed your changes, you should be able to see your work on GitHub. Each pull request will be reviewed and merged if there are no objections. Before sending a pull request, please make sure to:

- Write well-formatted [commit messages](#writing-git-commit-messages).
- Explain what the pull request addresses (especially if your pull request bundles several commits).
- Add [unit tests](#running-unit-tests-for-the-msdkuilib) for newly added features or - if suitable - for bug fixes.
- Keep the unit test [coverage](#generating-a-unit-test-coverage-report) above 80%.
- Add new UI components to the [Dev app](#building-the-dev-app).
- If your change involves a new UI behavior, please consider to help us write a [UI test](#running-ui-tests-for-the-mdkuiapp) (not mandatory, but more than welcome).

### Writing Git commit messages

We follow the format described below to ensure all the commit messages are aligned and in a consistent format.

#### A normal ticket

```
TICKET-XYZ: Capitalized short - 80 characters or less - title

Extended description. Please wrap it to 80 characters. Don't forget
the blank line separating the title from the description, otherwise
Git will treat the entire thing as title.

Use blank lines for additional paragraphs.

- Lists/bullet points are okay.
- Typically a hyphen or asterisk is used for the bullet and don't forget
  to indent additional lines, like this one.
```

>**Note:**
- Keep the title short. It should explain what the commit is about.
- Don't end the commit title with a period.
- Use imperative mood (*Fix* instead of *Fixes*, *Add* instead of *Adds*, etc..).

#### Solving multiple tickets

```
TICKET-X, TICKET-Y, TICKET-Z: Capitalized short - 80 characters or less - title

Contains:

TICKET-X: Ticket X Title
TICKET-Y: Ticket Y Title
TICKET-Z: Ticket Z Title

Extended description (as for a normal ticket, see above).
```

## Submitting a pull request

 - Pull requests may contain multiple commits.
 - Pull requests should not include "Merge" commits.
     - Rebase your work to keep the pull request commits on top.
 - Give the pull request a short title which explains what the pull request is about.
 - Give the pull request a description with details on what the pull request is about.
 - Once the pull request is merged into master, delete the remote feature branch.
