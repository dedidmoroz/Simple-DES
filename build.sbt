name := "SimpleDES"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies +=  "org.scalafx" %% "scalafx" % "8.0.60-R9"

fork :=  true

sourceDirectory in Compile += file("src/main/scala")

resourceDirectory in Compile += file("src/main/resources")

