Java Maven Errorprone Compiler Configuration Demo
=================================================

Why this project?
-----------------

You might think that configuring yet another Java compiler in Maven
would be trivial. I certainly expected it to be, but that turned out to
be an unwarranted assumption, and given that I had spent much more time
than I had anticipated, I thought that I would explain what I
experienced so that others might gain from what I had stumbled upon.

Developers at Google have created the errorprone compiler with the
intention of producing a compiler which is more strict than the vanilla
javac compiler. There is a source code file, in this project which I
took from the compiler's site, the name of the class is ShortSet.java.
All that you have to do to see that javac allows the erroneous code to
compile and that errorprone catches it is to run the project using maven.

    1st invoke maven using javac "mvn clean test" -- that build will fail 
    during the test phase as the code as written compiles, but then produces a
    RuntimeException

    2nd invoke maven using the errorprone compiler "mvn clean test-compile -P
    errorprone" -- in that build the error prone compiler detects the
    coding error and fails the build  -- brilliant

javac allows the code to compile and errorprone rejects the code,
produces an error message and causes the build to fail.

    [INFO] -------------------------------------------------------------
    [ERROR] COMPILATION ERROR : 
    [INFO] -------------------------------------------------------------
    [ERROR] /Users/andy/java/java-errorprone/java-errorprone-compiler-demo/src/test/java/org/zrgs/errorprone/ShortSetTest.java:
    [39,15] [CollectionIncompatibleType] Argument 'i - 1' should not be passed to this method; 
    its type int is not compatible with its collection's type argument Short
    (see http://errorprone.info/bugpattern/CollectionIncompatibleType)
    [INFO] 1 error

That's it -- pretty short and sweet.

I created this github project because it took me quite a while to work
out how to configure the errorprone compiler within the context of the
maven-compiler-plugin's configuration.

The unexpected items had to do with the 2 plexus components that are
required dependencies of the errorprone compiler. The plexus bits have
to be added as dependencies of the compiler plugin, not intuitively
obvious to the casual observer, or at least they weren't at 1st obvious
to me.

[The errorprone compiler's site](http://errorprone.info/)

    <properties>
        <errorprone.compiler.version>2.2.0</errorprone.compiler.version>
        <java.version>1.8</java.version>
        <maven.compiler.source>${java.version}</maven.compiler.source>
        <maven.compiler.target>${java.version}</maven.compiler.target>
        <maven.version>3.5.3</maven.version>
        <plexus.compiler.version>2.8.4</plexus.compiler.version>
        <plexus.utils.version>31.0</plexus.utils.version>
    </properties>

    <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.7.0</version>
        <configuration>
            <compilerId>javac-with-errorprone</compilerId>
            <forceJavacCompilerUse>true</forceJavacCompilerUse>
        </configuration>
        <dependencies>
            <dependency>
                <groupId>org.codehaus.plexus</groupId>
                <artifactId>plexus-utils</artifactId>
                <version>${plexus.utils.version}</version>
            </dependency>
            <dependency>
                <groupId>com.google.errorprone</groupId>
                <artifactId>error_prone_core</artifactId>
                <version>${errorprone.compiler.version}</version>
            </dependency>
            <dependency>
                <groupId>org.codehaus.plexus</groupId>
                <artifactId>plexus-compiler-javac-errorprone</artifactId>
                <version>${plexus.compiler.version}</version>
            </dependency>
            <dependency>
                <groupId>org.codehaus.plexus</groupId>
                <artifactId>plexus-compiler-api</artifactId>
                <version>${plexus.compiler.version}</version>
            </dependency>
            <dependency>
                <groupId>org.codehaus.plexus</groupId>
                <artifactId>plexus-compiler-manager</artifactId>
                <version>${plexus.compiler.version}</version>
            </dependency>
        </dependencies>
    </plugin>

current as of 2018-04-04
