# MSDKUI Contribution Guide

This guide is for developers who want to contribute to the MSDKUI codebase, build the MSDKUI library, or run the demo application on their local machines. For using the `MSDKUILib.aar` on your own project, or running the accompanying example apps, please check the [QuickStart](QuickStart.md) guide.

## Contents


- [Getting the code](#getting-the-code)
- [See available Gradle tasks](#see-available-gradle-tasks)
- [Building the HERE UI Kit library](#building-the-here-ui-kit-library)
	- [Building the MSDKUI API Reference from the Command line](#building-the-msdkui-api-reference-from-the-command-line)
	- [Running Unit Tests for the MSDKUILib](#running-unit-tests-for-the-msdkuilib)
	- [Generating a Unit Test Coverage Report](#generating-a-unit-test-coverage-report)
- [Building the Demo app](#building-the-demo-app)
	- [Setting the HERE Mobile SDK license](#setting-the-here-mobile-sdk-license)
	- [Building and running the Demo](#building-and-running-the-demo)
	- [Running Unit Tests for the MDKUIApp](#running-unit-tests-for-the-mdkuiapp)
	- [Generating a Unit Test Coverage Report](#generating-a-unit-test-coverage-report)
	- [Running UI Tests for the MDKUIApp](#running-ui-tests-for-the-mdkuiapp)
	- [Optional: Building the HERE UI Kit Library and the Demo as part of one project](#optional-building-here-ui-kit-library-and-demo-as-part-of-one-project)
- [Commit Policy](#commit-policy)
	- [Writing Git Commit Messages](#writing-git-commit-messages)
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

## Building the HERE UI Kit library

The easiest way to build the HERE Mobile SDK UI Kit library (MSDKUILib) is using the command line:
- Put the `HERE-sdk.aar` file from your HERE SDK package to _MSDKUIKit/libs/_.
- Build the HERE Mobile SDK UI Kit library via Gradle. Navigate to the _MSDKUIKit_ folder and execute: `./gradlew clean :MSDKUILib:assembleRelease`.

>**Note:** Please, make sure to set `ANDROID_HOME` to your environment `PATH` variable. It should point to the location where you have installed the Android SDK.

The built `MSDKUILib-release.aar` is then located in your new _MSDKUIKit/MSDKUILib/build/outputs/aar/_ subfolder.

>**Note:** You can also build the lib from within Android Studio. Open the MSDKUIKit Android project and open the Gradle "Tool Windows" to see all available Gradle tasks. Select and execute the `:MSDKUILib:assemble` task.

### Building the MSDKUI API Reference from the Command line

To generate the MSDKUI API Reference, navigate to the _MSDKUIKit_ folder and run:

```
./gradlew clean :MSDKUILib:javadocJar
```

It will place the API Reference at `build/docs/javadoc/`. Run `open build/docs/javadoc/index.html` to open the documentation on the default browser. The Javadoc Jar can be found at `build/libs/`.

### Running Unit Tests for the MSDKUILib

Switch to command line and navigate to the _MSDKUIKit_ folder. Execute: `./gradlew :MSDKUILib:testDebugUnitTest`.

>**Note:** Alternatively, you can also run the tests from within Android Studio by right clicking on a specific test or the _test/msdkui_ subfolder and clicking "Run Tests". You can also directly select the Gradle Task: `MSDKUILib -> Tasks -> verification -> test` (right-click and run).

### Generating a Unit Test Coverage Report

Execute `/gradlew :MSDKUILib:testDebugUnitTest :MSDKUILib:jacocoTestDebugReport` to run all unit tests _and_ to generate a coverage report. The report wil be available in _MSDKUILib/build/reports/jacoco/jacocoTestDebugReport_. Open `index.html` to see an overview of the current code coverage.

>**Note:** To generate the coverage report from within Android Studio, execute the Gradle Task from the "Tools Window": `MSDKUILib -> Tasks -> reporting -> jacocoTestDebugReport` (right-click and run).

## Building the Demo app

Before building, testing, or running the MSDKUI Demo application it's important to set the HERE Mobile SDK credentials. If you don't know your credentials, please ask your HERE stakeholder or register on [developer.here.com](https://developer.here.com) and create new ones.

### Setting the HERE Mobile SDK license

Don't set your credentials directly to the Android manifest (as otherwise you would need to exclude them for every commit). Instead add `MSDKUI_APP_ID_ANDROID`, `MSDKUI_APP_TOKEN_ANDROID` and `MSDKUI_APP_LICENSE_ANDROID` to your OS `$PATH`, see _MSDKUIDemo/MSDKUIApp/build.gradle_ for details.

Alternatively, you can replace "APP_ID", "APP_TOKEN" and "APP_LICENSE" in _MSDKUIDemo/MSDKUIApp/build.gradle_:
```
resValue 'string', 'HERE_SDK_APP_ID', System.getenv("MSDKUI_APP_ID_ANDROID") ?: "APP_ID"
resValue 'string', 'HERE_SDK_APP_TOKEN', System.getenv("MSDKUI_APP_TOKEN_ANDROID") ?: "APP_TOKEN"
resValue 'string', 'HERE_SDK_LICENSE_KEY', System.getenv("MSDKUI_APP_LICENSE_ANDROID") ?: "APP_LICENSE"
```

When you register on [developer.here.com](https://developer.here.com), the registered bundle identifier must match the [applicationId](https://developer.android.com/studio/build/application-id) in the app's build.gradle. For the demo app this is by default `com.here.msdkui.demo` - you may need to adapt the `applicationId` to the one you have registered.

### Building and Running the Demo

In order to build and to run the demo app with Android Studio, you need to integrate the HERE Mobile SDK (Premium) version 3.9. Additionally, you need to integrate the HERE Mobile SDK UI Kit library.

- Put the HERE-sdk.aar file from your HERE SDK package and the HERE UI Kit library to _MSDKUIDemo/libs/_.
- Open and build the project located at _MSDKUIDemo/_ in Android Studio.
- Run it on a device or emulator of your choice.

>**Note:** Alternatively, you can build the Demo from command line. Navigate to the MSDKUIDemo folder and execute: `./gradlew clean :MSDKUIApp:assembleRelease`. The built Demo APK is then located in your new _MSDKUIDemo/MSDUIApp/output_ subfolder. Copy the APK to your device and run it.

### Running Unit Tests for the MDKUIApp

Switch to command line and navigate to the _MSDKUIDemo_ folder. Execute: `./gradlew :MSDKUIApp:testDebugUnitTest`.

>**Note:** Alternatively, you can also run the tests from within Android Studio by right clicking on a specific test or the _test_ subfolder and clicking "Run Tests". You can also directly select the Gradle Task: `MSDKUIApp -> Tasks -> verification -> test` (right-click and run).

### Generating a Unit Test Coverage Report

Execute `/gradlew :MSDKUIApp:testDebugUnitTest :MSDKUIApp:jacocoTestDebugReport` to run all unit tests _and_ to generate a coverage report. The generated report can be found in _MSDKUIApp/build/reports/jacoco/jacocoTestDebugReport_. Open `index.html` to see an overview of the current code coverage.

>**Note:** To generate the coverage report from within Android Studio, execute the Gradle Task from the "Tools Window": `MSDKUIApp -> Tasks -> reporting -> jacocoTestDebugReport` (right-click and run).

### Running UI Tests for MDKUIApp

Switch to the command line and navigate to the _MSDKUIDemo_ folder. Execute: `./gradlew MSDKUIApp:connectedAndroidTest`.

>**Note:** Alternatively, you can also run the UI tests from within Android Studio by right clicking on a specific test or the _androidTest_ subfolder and clicking "Run Tests". You can also directly select the Gradle Task: `MSDKUIApp -> Tasks -> build -> assembleAndroidTest` (right-click and run).

### Optional: Building the HERE UI Kit Library and the Demo as part of one project

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

## Commit Policy

Please follow our commit policy. Once you have pushed your changes, you should be able to see your work on GitHub. Each pull request will be reviewed and merged if there are no objections. Before sending a pull request, please make sure to:

- Write well-formatted [commit messages](#writing-git-commit-messages).
- Explain what the pull request addresses (especially if your pull request bundles several commits).
- Add [unit tests](#running-unit-tests-for-the-msdkuilib) for newly added features or - if suitable - for bug fixes.
- Keep the unit test [coverage](#generating-a-unit-test-coverage-report) above 80%.
- If your change involves a new UI behavior, please consider to help us write a [UI test](#running-ui-tests-for-the-mdkuiapp) (not mandatory, but more than welcome).

### Writing Git Commit Messages

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
