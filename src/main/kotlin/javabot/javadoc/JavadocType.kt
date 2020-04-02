package javabot.javadoc

import java.io.File
import java.util.jar.Manifest

enum class JavadocType {
     JAVA6 {
     },
     JAVA7 {
     },
     JAVA8 {
     },
     JAVA11 {
     };

     companion object {
         fun discover(root: File) : JavadocType {
             val manFile = File(root, "META-INF/MANIFEST.MF")
             if(manFile.exists()) {
                 val manifest = Manifest()
                 manifest.read(manFile.inputStream())
                 val jdkVersion = manifest.mainAttributes.getValue("Build-Jdk")
                 return when {
                     jdkVersion.startsWith("1.6") -> JAVA6
                     jdkVersion.startsWith("1.7") -> JAVA7
                     jdkVersion.startsWith("1.8") -> JAVA8
                     jdkVersion.startsWith("11") -> JAVA11
                     else -> throw IllegalArgumentException("unknown JDK version for javadoc:  $jdkVersion")
                 }
             }
             return when {
                 File(root, "package-list").exists() -> JAVA8
                 File(root, "element-list").exists() -> JAVA11
                 else -> throw IllegalArgumentException("unknown JDK version for javadoc for $root")
             }
         }
     }

}