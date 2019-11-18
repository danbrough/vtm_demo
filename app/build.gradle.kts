plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  kotlin("android.extensions")
}

android {
  compileSdkVersion(ProjectVersions.SDK_VERSION)

  defaultConfig {

    minSdkVersion(ProjectVersions.MIN_SDK_VERSION)
    targetSdkVersion(ProjectVersions.SDK_VERSION)
    versionCode = ProjectVersions.VERSION_CODE
    versionName = ProjectVersions.VERSION_NAME
    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }


  compileOptions {
    sourceCompatibility = ProjectVersions.JAVA_VERSION
    targetCompatibility = ProjectVersions.JAVA_VERSION
  }

  dataBinding {
    isEnabled = false
  }

  androidExtensions {
    isExperimental = true
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"
      //freeCompilerArgs = listOf("-Xjsr305=strict")
      freeCompilerArgs = listOf("-Xuse-experimental=kotlin.Experimental")
    }
  }


}

/*
configurations.all {
  resolutionStrategy.force "com.squareup.okhttp3:okhttp:$okhttp_version"
}

project.afterEvaluate {
  android.applicationVariants.all { variant ->
    task "installRun${variant.name.capitalize()}"(type: Exec, dependsOn: "install${variant.name.capitalize()}", group: "run") {
      commandLine = ["adb", "shell", "monkey", "-p", variant.applicationId + " 1"]
      doLast {
        println "Launching ${variant.applicationId}"
      }
    }
  }
}
*/

project.afterEvaluate {
  android.applicationVariants.forEach { variant ->
    tasks.register("installRun${variant.name.capitalize()}", Exec::class) {
      setDependsOn(listOf("install${variant.name.capitalize()}"))
      group = "run"
      setCommandLine("adb", "shell", "monkey", "-p", variant.applicationId + " 1")
      doLast {
        println("Launching ${variant.applicationId}")
      }

    }
  }
}

dependencies {

  val vtm_version = "0.12.0-dan01"
  implementation("com.github.danbrough.vtm:vtm-android:$vtm_version")
  implementation("com.github.danbrough.vtm:vtm-http:$vtm_version")
  implementation("com.github.danbrough.vtm:vtm-themes:$vtm_version")


  implementation(Libs.lifecycle_runtime_ktx)
  implementation(Libs.lifecycle_extensions)
  implementation(Libs.lifecycle_livedata_ktx)
  implementation(Libs.core_ktx)

  //sliding panel library
  implementation(Libs.library)


  //implementation(Libs.slf4j_android)
  implementation(Libs.slf4j_api)
  implementation(Libs.slf4j)
  testImplementation(Libs.logback_classic)
  testImplementation(Libs.logback_core)

  implementation(Libs.navigation_fragment_ktx)
  implementation(Libs.navigation_ui_ktx)
  implementation(Libs.constraintlayout)
  implementation(Libs.preference_ktx)
  implementation(Libs.androidx_media_media)
  implementation(Libs.material)


}
