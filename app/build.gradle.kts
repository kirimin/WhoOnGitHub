import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(26)

    defaultConfig {
        applicationId = "kirimin.me.whoongithub"
        minSdkVersion(17)
        targetSdkVersion(26)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    dataBinding {
        isEnabled = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("com.android.support:appcompat-v7:26.1.0")
    implementation("io.reactivex:rxkotlin:0.60.0")
    implementation("io.reactivex:rxandroid:1.2.1")
    implementation("io.reactivex:rxjava:1.2.9")
    implementation("com.squareup.okhttp3:okhttp:3.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.7.0")
    implementation("com.squareup.picasso:picasso:2.5.2")
    implementation("com.squareup.retrofit2:retrofit:2.2.0")
    implementation("com.squareup.retrofit2:converter-gson:2.2.0")
    implementation("com.squareup.retrofit2:adapter-rxjava:2.2.0")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.10")
    testImplementation("junit:junit:4.12")
    testImplementation ("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
}