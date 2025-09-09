plugins {
	kotlin("jvm") version "2.2.10"
	kotlin("plugin.spring") version "2.2.10"
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.techtest"
version = "0.0.1-SNAPSHOT"
description = "product catalog app"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

// Runtime can be JDK 23/25+ while targeting bytecode compatibility with JDK 17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("io.github.wimdeblauwe:htmx-spring-boot-thymeleaf:4.0.1")
	implementation("org.flywaydb:flyway-core:11.11.2")
	implementation("org.postgresql:postgresql:42.7.7")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
