Java Maven Errorprone Compiler Configuration Demo
=================================================

Why this project?
-----------------

You might think that configuring another Java compiler in Maven would be
trivial. I certainly expected it to be, but that turned out to be an
unwarranted assumption, and given that I had spent much more time than I
had anticipated, I thought that I would explain what I experienced so
that others might gain from what I had stumbled upon.


      <properties>
        <errorprone.compiler.version>2.0.15</errorprone.compiler.version>
        <java.version>1.8</java.version>
        <maven.compiler.source>${java.version}</maven.compiler.source>
        <maven.compiler.target>${java.version}</maven.compiler.target>
        <maven.version>3.3.9</maven.version>
        <plexus.compiler.version>2.8.1</plexus.compiler.version>
        <plexus.utils.version>3.0.24</plexus.utils.version>
      </properties>
    
        <plugin>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.6.1</version>
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