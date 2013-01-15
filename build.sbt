name := "monkeyman"

version := "0.2.1"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
  "org.pegdown" % "pegdown" % "1.1.0",
  "org.fusesource.scalamd" % "scalamd" % "1.5",
  "joda-time" % "joda-time" % "2.0",
  "eu.medsea.mimeutil" % "mime-util" % "2.1.3" intransitive,
  "commons-io" % "commons-io" % "2.1",
  "org.joda" % "joda-convert" % "1.2",
  "org.fusesource.scalate" % "scalate-core" % "1.5.3",
  "org.clapper" %% "argot" % "0.4",
  "com.ibm.icu" % "icu4j" % "4.8.1.1",
  "ch.qos.logback" % "logback-core" % "1.0.0",
  "ch.qos.logback" % "logback-classic" % "1.0.0",
  "com.asual.lesscss" % "lesscss-engine" % "1.1.5",
  "org.specs2" %% "specs2" % "1.12.1",
  "org.specs2" %% "specs2-scalaz-core" % "6.0.1" % "test",
  "junit" % "junit" % "4.8.1" % "test",
  "org.jsoup" % "jsoup" % "1.7.1"            
)

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

initialCommands in console := "import java.io._; import nl.flotsam.monkeyman._"

mainClass in (Compile, run) := Some("nl.flotsam.monkeyman.Monkeyman")

mainClass in (Compile, packageBin) := Some("nl.flotsam.monkeyman.Monkeyman")

fork in run := true

connectInput in run := true
