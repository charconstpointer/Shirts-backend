name := "SMNTV"

version := "1.0"

lazy val `smntv` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.2.0"
libraryDependencies ++= Seq(
  "com.lightbend.play" %% "play-file-watch" % "1.1.3",
  "com.typesafe.play" %% "play-slick" % "3.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.0"
//
//   "org.postgresql" % "postgresql" % "42.2.5"
)

libraryDependencies += "org.postgresql" % "postgresql" % "42.2.1"
libraryDependencies ++= Seq( ehcache, ws, specs2 % Test, guice)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

      