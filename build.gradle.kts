plugins {
	id("dev.frozenmilk.teamcode") version "10.1.1-0.1.3"
}

repositories {
	maven("https://maven.brott.dev/")
}

ftc {
	// use this to easily add more FTC libraries

	// adds support for kotlin
	kotlin
}

dependencies {
	implementation("com.escapevelocity.ducklib:core")
	implementation("com.escapevelocity.ducklib:ftc")
	implementation("com.acmerobotics.dashboard:dashboard:0.4.16")
}