plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "app.playerandroid"
    compileSdk = 34

    defaultConfig {
        applicationId = "app.playerandroid"
        minSdk = 30
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
            isShrinkResources= false
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
    sourceSets {
        getByName("main").assets.srcDirs("src/main/assets")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }
}


dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.github.bmelnychuk:atv:1.2.9") {
        exclude(group = "com.google.code.gson")
        exclude(group = "com.android.support")
    }
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

    implementation(libs.androidsvg)
    annotationProcessor(libs.org.atteo.classindex.classindex2)
    implementation(libs.org.atteo.classindex.classindex2)
    implementation(libs.reflections)
    implementation(libs.slf4j.slf4j.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("-Xlint:deprecation", "-Xlint:unchecked"))
}