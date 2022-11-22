Issue [#20581](https://github.com/gradle/gradle/issues/20581) reproducer
========================================================================

Project to reproduce the eventual inconsistency between GMM and Maven publications.

# Context

The issue is being reproduced by registering
a [feature variant](https://docs.gradle.org/current/userguide/feature_variants.html) into the project, and then removing
it
from the GMM publications **after** the Maven publication has been realized. This happens when the Maven publication is
populated from the software component via `populateFromComponent()`.

The resulting situation is that the GMM has effectively removed the feature, but the Maven publication still contains
the feature variant:

```
> Configure project :

Maven published artifacts:
build/libs/issue-20581-lazy-publish-1.0-SNAPSHOT.jar
build/libs/issue-20581-lazy-publish-1.0-SNAPSHOT-test-feature.jar

GMM published artifacts:
build/libs/issue-20581-lazy-publish-1.0-SNAPSHOT.jar

BUILD SUCCESSFUL in 426ms
```

## Usage

Refresh the project in your IDE, or run this command to print to the console the divergence between GMM and Maven
artifacts:

```shell
./gradlew prepareKotlinBuildScriptModel
```
