organization in ThisBuild := "de.surfice"

version in ThisBuild := "0.0.2-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.12"

val Version = new {
  val swog        = "0.1.0-SNAPSHOT"
  val smacrotools = "0.0.8"
  val utest       = "0.6.8-SNAPSHOT"
}


lazy val commonSettings = Seq(
  scalacOptions ++= Seq("-deprecation","-unchecked","-feature","-language:implicitConversions","-Xlint"),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "de.surfice" %%% "swog-cobj" % Version.swog,
    "com.lihaoyi" %%% "utest" % Version.utest % "test"
    ),
  resolvers += Opts.resolver.sonatypeSnapshots,
  testFrameworks += new TestFramework("utest.runner.Framework")
  )

lazy val nativeSettings = Seq(
  nativeLinkingOptions ++= Seq("-lglib-2.0","-lgtk-3.0","-lgobject-2.0"),
  nativeLinkStubs := true
)

lazy val scalanativeGtk = project.in(file("."))
  .enablePlugins(ScalaNativePlugin)
  .aggregate(glib,gobj,gio,gtk3,ui,json,soup) //,sourceview,tepl)
  .settings(commonSettings ++ dontPublish:_*)
  .settings(
    name := "scalanative-gtk-bindings"
    )

lazy val glib = project
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings ++ nativeSettings ++ publishingSettings:_*)
  .settings(
    name := "scalanative-glib"
  )


lazy val gobj = project
  .dependsOn(glib)
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings ++ publishingSettings:_*)
  .settings(
    name := "scalanative-gobj",
    libraryDependencies ++= Seq(
//      "de.surfice" %% "smacrotools" % Version.smacrotools
    )
  )


lazy val gio = project
  .dependsOn(gobj)
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings ++ publishingSettings:_*)
  .settings(
    name := "scalanative-gio"
  )


lazy val gtk3 = project
  .dependsOn(gio)
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings ++ publishingSettings:_*)
  .settings(
    name := "scalanative-gtk3"
  )

lazy val ui = project
  .dependsOn(gtk3)
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings ++ publishingSettings:_*)
  .settings(
    name := "scalanative-gtk3-ui"
  )

lazy val sourceview = project
  .dependsOn(gtk3)
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings ++ publishingSettings:_*)
  .settings(
    name := "scalanative-gtksourceview"
  )

lazy val tepl = project
  .dependsOn(sourceview)
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings ++ publishingSettings:_*)
  .settings(
    name := "scalanative-tepl"
  )

lazy val json = project
  .dependsOn(glib,gobj)
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings ++ publishingSettings:_*)
  .settings(
    name := "scalanative-json-glib"
  )

lazy val soup = project
  .dependsOn(gio)
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings ++ publishingSettings:_*)
  .settings(
    name := "scalanative-libsoup"
  )

lazy val macIntegration = project
  .dependsOn(gtk3)
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings ++ publishingSettings:_*)
  .settings(
    name := "scalanative-gtk-mac-integration"
  )

lazy val gtkTest = project
  .dependsOn(gtk3)
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings ++ dontPublish ++ nativeSettings:_*)
  .settings(
    //nativeLinkingOptions ++= nbhNativeLinkingOptions.value
    //nativeLinkingOptions ++= Seq("-lgtk-3.0","-lglib-2.0")
  )
  

lazy val dontPublish = Seq(
  publish := {},
  publishLocal := {},
  com.typesafe.sbt.pgp.PgpKeys.publishSigned := {},
  com.typesafe.sbt.pgp.PgpKeys.publishLocalSigned := {},
  publishArtifact := false,
  publishTo := Some(Resolver.file("Unused transient repository",file("target/unusedrepo")))
)

lazy val publishingSettings = Seq(
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := (
    <url>https://github.com/jokade/scalanative-gtk</url>
    <licenses>
      <license>
        <name>MIT License</name>
        <url>http://www.opensource.org/licenses/mit-license.php</url>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:jokade/scalanative-gtk</url>
      <connection>scm:git:git@github.com:jokade/scalanative-gtk.git</connection>
    </scm>
    <developers>
      <developer>
        <id>jokade</id>
        <name>Johannes Kastner</name>
        <email>jokade@karchedon.de</email>
      </developer>
    </developers>
  )
)
 
