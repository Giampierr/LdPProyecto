//Para los q usan Maven esto es el pom en scala


scalaVersion := "3.9.0"

lazy val root = rootProject
  .settings(
    name := "LdPProyecto",
    libraryDependencies ++= Seq(
       // SQLite
      "org.xerial" % "sqlite-jdbc" % "3.53.4.0",
      
      // BCrypt
      "org.mindrot" % "jbcrypt" % "0.4",
      
      // JavaFX
      "org.openjfx" % "javafx-controls" % "21.0.8",
      "org.openjfx" % "javafx-fxml" % "21.0.8"
    )
  )
