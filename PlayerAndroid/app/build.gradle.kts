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
    packaging {
        resources.pickFirsts.add("org/apache/xmlgraphics/fonts/glyphlist.txt")
        resources.pickFirsts.add("license/LICENSE.dom-documentation.txt")
        resources.pickFirsts.add("license/LICENSE.dom-documentation.txt")
        resources.pickFirsts.add("org.apache.xmlgraphics:xml-apis-ext:1.3.04")
        resources.pickFirsts.add("javax/xml/bind/util/Messages.properties")
        resources.pickFirsts.add("license/NOTICE")
        resources.pickFirsts.add("license/README.dom.txt")
        resources.pickFirsts.add("META-INF/mimetypes.default")
        resources.pickFirsts.add("license/LICENSE.dom-software.txt")
        resources.pickFirsts.add("javax/xml/bind/Messages.properties")
        resources.pickFirsts.add("org/apache/xmlgraphics/image/writer/default-preferred-order.properties")
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
    implementation(files("src/main/libs/libCommon/hamcrest-all-1.3.jar"))
    implementation(files("src/main/libs/libCommon/json-20180813.jar"))
    implementation(files("src/main/libs/libCommon/junit-4.12.jar"))
    implementation(files("src/main/libs/libCommon/Trove4j_ApacheCommonsRNG.jar"))
    implementation(files("src/main/libs/libCommon/Trove4j_ApacheCommonsRNG-sources.jar"))
    implementation(files("src/main/libs/libPlayer/activation-1.1.1.jar"))
    implementation(files("src/main/libs/libPlayer/itextpdf-5.5.13.3.jar"))
    implementation(files("src/main/libs/libPlayer/javax.mail.jar"))
    implementation(files("src/main/libs/libPlayer/jaxb-api-2.3.1.jar"))
    implementation(files("src/main/libs/libPlayer/svgSalamander-1.1.2.jar"))
    implementation(files("src/main/libs/libPlayer/xml-apis-ext-1.3.04.jar"))
    implementation(files("src/main/libs/libPlayer/xmlgraphics-commons-2.3.jar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("-Xlint:deprecation", "-Xlint:unchecked"))
}