lazy val root = (project in file("."))

lazy val commons = (project in file("commons"))
  .settings(Settings.commonSettings:_*)

lazy val httpCalculator = (project in file("http-calculator"))
  .settings(Settings.commonSettings:_*)
  .settings(libraryDependencies += ws)
  .dependsOn(commons)
  .enablePlugins(PlayScala)
  .settings(
    name := """http-calculator-example""",
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )

lazy val socketCalculator = (project in file("socket-calculator"))
  .settings(Settings.commonSettings:_*)
  .dependsOn(commons)
  .settings(
    name := """http-socket-example""",
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )
