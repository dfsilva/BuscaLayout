-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-dontobfuscate
-dontoptimize
-repackageclasses ''

#Jackson
-dontwarn com.fasterxml.jackson.databind.**

#View Pager Indicator
-dontwarn com.viewpagerindicator.**

#Android
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.app.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep class android.support.v13.app.** { *; }
-keep interface android.support.v13.app.** { *; }

#droid4me
-keep class com.smartnsoft.** { *; }

#my app
-keep class my.app.package.** { *; }

#Critercism
-keep public class com.crittercism.**
-keepclassmembers public class com.crittercism.* { *; }