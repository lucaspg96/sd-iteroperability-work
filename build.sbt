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
    //    libraryDependencies ++= Seq(
    //      guice,
    //      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
    //    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )
