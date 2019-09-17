[![DepShield Badge](https://depshield.sonatype.org/badges/andyglick/java-errorprone-compiler-demo/depshield.svg)](https://depshield.github.io)

Java Maven Errorprone Compiler Configuration and Example Demo
=============================================================

Why this project?
-----------------

current as of September 16, 2019

You might expect that configuring yet another Java compiler in Maven
would be trivial. I certainly expected it to be, but that turned out to
be an unwarranted assumption, and given that I had spent far more time
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

Configuration details:

It turns out that the compiler that actually gets used by the
maven-compiler-plugin is configurable, but how to configure the actual
compiler that you might use isn't trivially obvious to the casual
observer. In the case of the error prone compiler, there are a number of
jars from the error prone project, and as is often the case with maven
supplied plugins, there are a number of plexus jars that are required.
The relationship between the maven and the plexus projects is probably a
rant for a different day -- HEH!

This is the example code:

    package org.zrgs 
    import org.junit.Test;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    import java.lang.invoke.MethodHandles;
    import java.util.HashSet;
    import java.util.Set;

    import static org.assertj.core.api.Assertions.assertThat;

    /**
     * ShortSetTest is a demonstration class to show what kinds of problems the errorprone
     * compiler can identify and report with existing Java code
     *
     * the code below compiles with javac, but fails compilation when using the error prone compiler
     *
     * what doesn't compile under error prone is the statement
     *
     * s.remove(i - 1);
     *
     * as it resolves to int and not to short
     */
    public class ShortSetTest
    {
      private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

      @Test
      public void testShortSet()
      {

        Set<Short> s = new HashSet<>();

        for (short i = 0; i < 100; i++)
        {
          s.add(i);
          s.remove(i - 1);
        }

        log.info("the size  of the set is " + s.size());

        assertThat(s.size()).isEqualTo(1);
      }
    }

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
        <errorprone.compiler.version>2.3.3</errorprone.compiler.version>
        <java.version>1.8</java.version>
        <maven.compiler.source>${java.version}</maven.compiler.source>
        <maven.compiler.target>${java.version}</maven.compiler.target>
        <maven.version>3.6.1</maven.version>
        <plexus.compiler.version>2.8.5</plexus.compiler.version>
        <plexus.utils.version>3.2.1</plexus.utils.version>
    </properties>

    <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.1</version>
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

*versions and compiler plugin configuration current as of 2019-09-16*
