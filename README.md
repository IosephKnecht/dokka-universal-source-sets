## What is this?
This project demonstrates the options for setting up the Dokka plugin for an Android project.
And is intended to serve as a demo example for [issue](https://github.com/Kotlin/dokka/issues/4100).

## Quick start
There is a `gradle.properties` file in the project root:
Before you start, please set the field in it to `io.github.iosephknecht.dokka.plugin.convention.mode`.

```text
in gradle.properties file

# One of three values:
# DEFAULT - default setting from dokka plugin
# WITH_SUPPRESSED_SOURCE_SETS - with sourcesets flag suppressed
# WITH_WORKAROUND_SUPPRESSED_SOURCE_SETS - with sourcesets flag suppressed and classpath substitution
io.github.iosephknecht.dokka.plugin.convention.mode=DEFAULT
```

Then enter the following in terminal:
```bash
./gradlew clean && ./gradlew :app:dokkaGeneratePublicationHtml
```

After which, you can find the generated documentation at the following path:

```text
iosephknecht@iosephknecht-laptop:~/AndroidStudioProjects/dokka-universal-source-sets$ tree
dokka-universal-source-sets
├── app
│   ├── build
│   │   ├── dokka
│   │   │   └── html
│   │   │       ├── some file
│   │   │       ├── ...
│   │   │       ├── index.html


```