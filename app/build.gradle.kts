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


  implementation(Libs.slf4j_api)
  implementation(Libs.slf4j)
  testImplementation(Libs.logback_classic)
  testImplementation(Libs.logback_core)

  implementation(Libs.constraintlayout)
  implementation(Libs.preference_ktx)
  implementation(Libs.material)


}
