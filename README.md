# <img alt="app-icon" height="50" src="https://github.com/axiel7/AniHyou-android/blob/master/app/src/main/res/mipmap-hdpi/ic_launcher_round.webp"/>AniHyou

[![Donate](https://img.shields.io/badge/buy%20me%20a%20coffee-donate-yellow.svg)](https://ko-fi.com/axiel7)

Another unofficial Android AniList client

Follow the development on the official Discord server:

[![Discord Banner 3](https://discordapp.com/api/guilds/741059285122940928/widget.png?style=banner2)](https://discord.gg/CTv3WdfxHh)

**Libraries used:**
* [AniList GraphQL API](https://github.com/AniList/ApiV2-GraphQL-Docs)
* [Apollo Kotlin](https://github.com/apollographql/apollo-kotlin)
* [Material3 Components](https://github.com/material-components/material-components-android)
* [Jetpack Compose](https://developer.android.com/jetpack/compose)
* [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
* [Coil](https://github.com/coil-kt/coil)

# Building
Create a file `app/scr/main/java/com/axiel7/anihyou/ClientId.kt` and put the following content:

```kotlin
package com.axiel7.anihyou

const val CLIENT_ID = 1234 //your AniList API client ID here
```
