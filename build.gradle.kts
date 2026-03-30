plugins {
    id("java")
    kotlin("jvm")
}

group = "com.diy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // ObjectMapper
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")

    // 서블릿
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("javax.servlet:jstl:1.2")

    // 톰캣
    implementation("org.apache.tomcat.embed:tomcat-embed-core:8.5.42")
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper:8.5.42")

    implementation(kotlin("stdlib-jdk8"))

    // 로깅
    implementation("org.slf4j:slf4j-api:2.0.16")
    testImplementation("ch.qos.logback:logback-classic:1.5.18")

    // 리플렉션 의존성 주입
    implementation("org.reflections:reflections:0.10.2")
}

tasks.test {
    useJUnitPlatform()
}