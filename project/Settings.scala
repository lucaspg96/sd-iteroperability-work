import play.sbt.PlayImport.guice
import sbt.Keys.{libraryDependencies, organization, scalaVersion, version}

object Settings {
  val commonSettings = Seq(
    libraryDependencies ++= Seq(
      guice,
//      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
    ),
    organization := "br.ufc",
    version := "0.1.0",
    scalaVersion := "2.13.1",
  )
}
