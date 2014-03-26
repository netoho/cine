name := "Movie Free"

version := "0.0.1"

organization := "net.liftweb"

scalaVersion := "2.10.0"

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
                  "staging"       at "http://oss.sonatype.org/content/repositories/staging",
                  "releases"      at "http://oss.sonatype.org/content/repositories/releases"
                 )

seq(webSettings :_*)

unmanagedResourceDirectories in Test <+= (baseDirectory) { _ / "src/main/webapp" }

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= {
  val liftVersion = "2.5.1"
  Seq(
    "net.liftweb"       %% "lift-webkit"        % liftVersion        % "compile",
    "net.liftmodules"   %% "lift-jquery-module_2.5" % "2.4",
    "net.liftmodules" %% "omniauth_2.5" % "0.11",
    "net.liftmodules" %% "facebook_2.5" % "1.2",
    "net.liftmodules" %% "paypal_2.5" % "1.2",
    "org.eclipse.jetty" % "jetty-webapp"        % "8.1.7.v20120910"  % "container,test",
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,test" artifacts Artifact("javax.servlet", "jar", "jar"),
    "ch.qos.logback"    % "logback-classic"     % "1.0.6",
    "org.specs2"        %% "specs2"             % "1.14"             % "test",
    "com.typesafe.slick" %% "slick" % "2.0.1",
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    "mysql" % "mysql-connector-java" % "5.1.28"
  )
}

//port in container.Configuration := 80
