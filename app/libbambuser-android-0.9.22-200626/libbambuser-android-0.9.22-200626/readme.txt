
For building with Gradle/Android Studio:
----------------------------------------

Import the top level directory as a new project in Android Studio.
This sets up the example broadcaster and example player projects,
which use the AAR file from the top level directory.

Before running the example broadcaster or player, replace the placeholder APPLICATION_ID
in example/java/com.bambuser.broadcaster.example/ExampleActivity and
exampleplayer/java/com.bambuser.exampleplayer/ExamplePlayerActivity.
For details, see https://bambuser.com/docs/key-concepts/application-id/

For guides on how to integrate the library in your own app, see https://bambuser.com/docs/


Notes
-----

Javadocs for the library can be found under doc.

The library contains native code, currently built for the
armeabi-v7a, arm64-v8a, x86 and x86_64 ABIs. Make sure to not build apps
targeting other architectures, since the libbambuser native
libs will be missing for those architectures. For release APKs, you may
choose to include only ARM native libs. (E.g., for
gradle, you may need to set "abiFilters 'armeabi-v7a', 'arm64-v8a'".)


Legal
-----

All relevant licenses for included open source libraries
are found under example/assets and must be communicated to
end users of your application in some way. One approach
is found in our example applications.

The example broadcaster and player apps depend on the OkHttp and Okio libraries,
and they are therefore mentioned in the example license documents.
If you don't intend to use the file uploading parts from our broadcaster example
or do api requests using other another method than OkHttp, you can leave out
OkHttp and Okio from your license information. All other licenses are mandatory.
