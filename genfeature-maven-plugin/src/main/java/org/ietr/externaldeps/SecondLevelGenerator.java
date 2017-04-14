package org.ietr.externaldeps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.apache.maven.plugin.MojoFailureException;

public class SecondLevelGenerator {

  public void generate(final GenerateAllInOneP2Feature generateAllInOneP2Feature) throws MojoFailureException, IOException {

    // make directories
    new File(generateAllInOneP2Feature.currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME).mkdirs();
    new File(generateAllInOneP2Feature.currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
        + GenerateAllInOneP2Feature.SECOND_LEVEL_FEATURE_PROJECT).mkdirs();
    new File(generateAllInOneP2Feature.currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
        + GenerateAllInOneP2Feature.SECOND_LEVEL_SITE_PROJECT).mkdirs();

    generateFeature(generateAllInOneP2Feature);

  }

  private final void generateFeature(final GenerateAllInOneP2Feature generateAllInOneP2Feature) throws IOException {
    CheckParameters.checkParameters(generateAllInOneP2Feature);

    generateParentPomFile(generateAllInOneP2Feature);
    new FeatureProjectGenerator().generateProject(generateAllInOneP2Feature);
    new SiteProjectGenerator().generateProject(generateAllInOneP2Feature);
  }

  private void generateParentPomFile(final GenerateAllInOneP2Feature generateAllInOneP2Feature) throws FileNotFoundException, UnsupportedEncodingException {
    PrintWriter writer;

    final StringBuffer buffer = new StringBuffer();

    buffer.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
    buffer.append("  xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");
    buffer.append("  <modelVersion>4.0.0</modelVersion>\n");
    buffer.append("\n");
    buffer.append("  <artifactId>org.ietr.externaldeps.parent</artifactId>\n");
    buffer.append("  <groupId>org.ietr.externaldeps</groupId>\n");
    buffer.append("  <packaging>pom</packaging>\n");
    buffer.append("  <version>" + generateAllInOneP2Feature.project.getVersion() + "</version>\n");
    buffer.append("  \n");
    buffer.append("  <repositories>\n");
    buffer.append("    <repository>\n");
    buffer.append("      <id>original-site</id>\n");
    buffer.append("      <layout>p2</layout>\n");
    buffer.append("      <url>");
    buffer.append(generateAllInOneP2Feature.inputSite.toURI().toString());
    buffer.append("</url>\n");
    buffer.append("    </repository>\n");
    buffer.append("  </repositories>\n");
    buffer.append("  \n");
    buffer.append("  <modules>\n");
    buffer.append("  <module>feature</module>\n");
    buffer.append("  <module>site</module>\n");
    buffer.append("  </modules>\n");
    buffer.append("</project>\n");
    buffer.append("");

    writer = new PrintWriter(generateAllInOneP2Feature.currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME
        + "/" + GenerateAllInOneP2Feature.MAVEN_PROJECT_FILE_NAME, "UTF-8");
    writer.println(buffer.toString());
    writer.close();
  }
}
