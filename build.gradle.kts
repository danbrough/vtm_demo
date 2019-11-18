buildscript {

  dependencies {
    classpath(Libs.com_android_tools_build_gradle)
    classpath(Libs.kotlin_gradle_plugin)
   // classpath(Libs.navigation_safe_args_gradle_plugin)
  }


  repositories {
    google()
    jcenter()
  }
}

plugins {
  buildSrcVersions
}

allprojects {

  repositories {
    google()
    jcenter()
    maven {
      setUrl("https://jitpack.io")
    }

  }
  configurations.all {
    if (name.toLowerCase().contains("test")) {
      resolutionStrategy.dependencySubstitution {
        substitute(module(Libs.slf4j)).with(module(Libs.logback_classic))
      }
    }
  }
}

