plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "app.playerandroid"
    compileSdk = 34

    defaultConfig {
        applicationId = "app.playerandroid"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.androidsvg.aar)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(files("src/main/libs/libCommon/gtranslateapi-1.0.jar"))
    implementation(files("src\\main\\libs\\libCommon\\hamcrest-all-1.3.jar"))
    implementation(files("src\\main\\libs\\libCommon\\json-20180813.jar"))
    implementation(files("src\\main\\libs\\libCommon\\junit-4.12.jar"))
    implementation(files("src\\main\\libs\\libCommon\\Trove4j_ApacheCommonsRNG.jar"))
    implementation(files("src\\main\\libs\\libCommon\\Trove4j_ApacheCommonsRNG-sources.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\activation-1.1.1.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\itextpdf-5.5.13.3.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\javax.mail.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\jaxb-api-2.3.1.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\svgSalamander-1.1.2.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\xml-apis-ext-1.3.04.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\xmlgraphics-commons-2.3.jar"))
    implementation(files("src\\main\\libs\\libViewController\\activation-1.1.1.jar"))
    implementation(files("src\\main\\libs\\libViewController\\jaxb-api-2.3.1.jar"))
    implementation(files("src\\main\\libs\\libViewController\\xml-apis-ext-1.3.04.jar"))
    implementation(files("src\\main\\libs\\libViewController\\xmlgraphics-commons-2.3.jar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}