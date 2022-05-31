<h1 align="center">Volga-IT 22</h1>
<br>
<img src="/preview/screen.png" width="336" align="right" hspace="20">

## [Detailed task condition](https://docs.google.com/document/d/1DGTNxuDqqxOytIUBvCwtJ8IPv-LY9K6DqDRno2DMIK0/edit#)

## Tech stack
- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection.
- Jetpack
  - ViewModel
  - Room
- Architecture
  - MVVM Architecture (View - ViewModel - Model)
  - Clean Architecture
  - Repository Pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - Construct the REST APIs.

## Usage

Step1: Fork this repo directly:

```shell
$ git clone https://github.com/BrainScreame/volga-it22.git
```

Step2: Put the `USER_ACCESS_TOKEN` into `local.properties`:


```groovy
USER_ACCESS_TOKEN="xxxxxxxxxxxxxxxxxxxxxxxxx" // your access token
```
