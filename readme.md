# ApkTool-kotlin

A kotlin/java library to use [ApkTool](https://github.com/iBotPeaches/Apktool)
programatically and provides good api

currently apktool's [maven build are broken](https://github.com/iBotPeaches/Apktool/issues/1142) and its api aren't that good to be used
as library

## Features
- apk signing via [uber-apk-signer](https://github.com/patrickfav/uber-apk-signer)
- inject permission to `AndroidManifest.xml`


## Usage

full example of usage [here](./apktool-kotlin/src/main/kotlin/apktool/kotlin/app/App.kt)

## Download 

- add `maven { url "https://jitpack.io" }` in `settings.gradle`

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url "https://jitpack.io" }
    }
}

```
add to your app's `build.gradle`
```groovy
dependencies {
}
```
