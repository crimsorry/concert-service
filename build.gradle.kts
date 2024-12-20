plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm")
}

group = "hhplus.tdd"
version = "0.0.1"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

tasks.named<Jar>("jar") {
	enabled = false
}

dependencies {
	// Spring Boot
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	implementation("org.springframework.retry:spring-retry")
	implementation("org.springframework:spring-aspects")

	// Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	// MySQL
	runtimeOnly("com.mysql:mysql-connector-j")

	// Kotlin
	implementation(kotlin("stdlib-jdk8"))

	// redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis")

	// Redisson
	implementation("org.redisson:redisson-spring-boot-starter:3.38.1")

	// Kafka
	implementation ("org.springframework.kafka:spring-kafka")

	// Test - h2
	testImplementation("com.h2database:h2:2.3.232")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")
	testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
	testImplementation("org.testcontainers:testcontainers:1.20.3")
	testImplementation("org.testcontainers:junit-jupiter:1.20.3")
	testImplementation("org.testcontainers:kafka:1.20.3")
	testImplementation("org.testcontainers:mysql:1.20.3")
	testImplementation ("com.redis.testcontainers:testcontainers-redis-junit:1.6.4")
	testImplementation ("org.springframework.boot:spring-boot-testcontainers")

	// run
	implementation("org.springframework.boot:spring-boot-maven-plugin:3.4.0")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
