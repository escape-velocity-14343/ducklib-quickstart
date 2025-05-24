pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		google()
		maven("https://repo.dairy.foundation/releases")
	}
}

includeBuild("../ducklib/ducklib-ftc") {
	dependencySubstitution {
		substitute(module("com.escapevelocity.ducklib:ftc")).using(project(":"))
	}
}