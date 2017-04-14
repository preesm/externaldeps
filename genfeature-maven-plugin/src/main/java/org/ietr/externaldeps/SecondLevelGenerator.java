package org.ietr.externaldeps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.apache.maven.plugin.MojoFailureException;

public class SecondLevelGenerator {

  public void generate(final File currentWorkingDirectory, final File inputSite, final String featureName) throws MojoFailureException, IOException {

    // make directories
    new File(currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME).mkdirs();
    new File(currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
        + GenerateAllInOneP2Feature.SECOND_LEVEL_FEATURE_PROJECT).mkdirs();
    new File(currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
        + GenerateAllInOneP2Feature.SECOND_LEVEL_SITE_PROJECT).mkdirs();

    generateFeature(inputSite, featureName, currentWorkingDirectory);

  }

  private final void generateFeature(final File inputSite, final String featureName, final File currentWorkingDirectory) throws IOException {
    CheckParameters.checkParameters(inputSite, featureName, currentWorkingDirectory);

    generateParentPomFile(currentWorkingDirectory, inputSite);
    new FeatureProjectGenerator().generateProject(inputSite, featureName, currentWorkingDirectory);
    new SiteProjectGenerator().generateProject(inputSite, featureName, currentWorkingDirectory);
  }

  private void generateParentPomFile(final File currentWorkingDirectory, final File inputSite) throws FileNotFoundException, UnsupportedEncodingException {
    PrintWriter writer;

    String toto;

    toto = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
        + "  xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" + "  <modelVersion>4.0.0</modelVersion>\n"
        + "\n" + "  <artifactId>org.ietr.externaldeps.parent</artifactId>\n" + "  <groupId>org.ietr.externaldeps</groupId>\n" + "  <packaging>pom</packaging>\n"
        + "  <version>1.0.0</version>\n" + "  \n" + "  <repositories>\n" + "    <repository>\n" + "      <id>original-site</id>\n"
        + "      <layout>p2</layout>\n" + "      <url>file://" + inputSite.getAbsolutePath() + "</url>\n" + "    </repository>\n" + "  </repositories>\n"
        + "  \n" + "  <modules>\n" + "  <module>feature</module>\n" + "  <module>site</module>\n" + "  </modules>\n" + "</project>\n" + "";

    writer = new PrintWriter(currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
        + GenerateAllInOneP2Feature.MAVEN_PROJECT_FILE_NAME, "UTF-8");
    writer.println(toto);
    writer.close();
  }
}
