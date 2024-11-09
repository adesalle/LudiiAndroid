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
    implementation(files("src\\main\\libs\\libCommon\\gtranslateapi-1.0.jar"))
    implementation(files("src\\main\\libs\\libCommon\\hamcrest-all-1.3.jar"))
    implementation(files("src\\main\\libs\\libCommon\\jfreesvg-3.4.jar"))
    implementation(files("src\\main\\libs\\libCommon\\json-20180813.jar"))
    implementation(files("src\\main\\libs\\libCommon\\junit-4.12.jar"))
    implementation(files("src\\main\\libs\\libCommon\\Trove4j_ApacheCommonsRNG.jar"))
    implementation(files("src\\main\\libs\\libCommon\\Trove4j_ApacheCommonsRNG-sources.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\activation-1.1.1.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-anim-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-awt-util-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-bridge-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-codec-1.14.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-constants-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-css-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-dom-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-ext-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-gvt-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-i18n-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-parser-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-script-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-svg-dom-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-svggen-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-transcoder-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-util-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\batik-xml-1.11.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\itextpdf-5.5.13.3.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\javax.mail.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\jaxb-api-2.3.1.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\svgSalamander-1.1.2.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\xml-apis-ext-1.3.04.jar"))
    implementation(files("src\\main\\libs\\libPlayer\\xmlgraphics-commons-2.3.jar"))
    implementation(files("src\\main\\libs\\libViewController\\activation-1.1.1.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-anim-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-awt-util-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-bridge-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-constants-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-css-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-dom-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-ext-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-gvt-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-i18n-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-parser-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-script-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-svg-dom-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-svggen-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-transcoder-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-util-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\batik-xml-1.11.jar"))
    implementation(files("src\\main\\libs\\libViewController\\jaxb-api-2.3.1.jar"))
    implementation(files("src\\main\\libs\\libViewController\\xml-apis-ext-1.3.04.jar"))
    implementation(files("src\\main\\libs\\libViewController\\xmlgraphics-commons-2.3.jar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}