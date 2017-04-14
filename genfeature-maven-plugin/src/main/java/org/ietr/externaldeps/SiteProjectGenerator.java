package org.ietr.externaldeps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class SiteProjectGenerator {

  public void generateProject(final File inputSite, final String featureName, final File currentWorkingDirectory, String featureProvider, String featureId)
      throws FileNotFoundException, UnsupportedEncodingException {

    generateSitePomFile(currentWorkingDirectory, featureName, inputSite, featureProvider);
    generateSiteCategoryFile(currentWorkingDirectory, featureName, inputSite, featureId);
  }

  private void generateSiteCategoryFile(final File currentWorkingDirectory, final String featureName, final File inputSite, String featureId)
      throws FileNotFoundException, UnsupportedEncodingException {

    PrintWriter writer;

    final StringBuffer buffer = new StringBuffer();

    buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    buffer.append("<site>\n");
    buffer.append("   <feature url=\"features/"
        + featureId
        + "\" id=\""
        + featureId
        + "\" >\n");
    buffer.append("      <category name=\""
        + featureId
        + "_cat\"/>\n");
    buffer.append("   </feature>\n");
    buffer.append("   \n");
    buffer.append("   <category-def name=\""
        + featureId
        + "_cat\" label=\""
        + featureName
        + "\">\n");
    buffer.append("   </category-def>\n");
    buffer.append("</site>\n");
    buffer.append("");

    writer = new PrintWriter(currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
        + GenerateAllInOneP2Feature.SECOND_LEVEL_SITE_PROJECT + "/" + GenerateAllInOneP2Feature.CATEGORY_FILE_NAME, "UTF-8");
    writer.println(buffer);
    writer.close();
  }

  private void generateSitePomFile(final File currentWorkingDirectory, final String featureName, final File inputSite, String featureProvider)
      throws FileNotFoundException, UnsupportedEncodingException {
    PrintWriter writer;

    final StringBuffer buffer = new StringBuffer();

    buffer.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" ");
    buffer.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
    buffer.append("xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");
    buffer.append("  <modelVersion>4.0.0</modelVersion>\n");
    buffer.append("  <artifactId>org.ietr.externaldeps.dependency.site</artifactId>\n");
    buffer.append("  <packaging>eclipse-repository</packaging>\n");
    buffer.append("  \n");
    buffer.append("  <parent>\n");
    buffer.append("  <artifactId>org.ietr.externaldeps.parent</artifactId>\n");
    buffer.append("  <groupId>org.ietr.externaldeps</groupId>\n");
    buffer.append("    <version>1.0.0</version>\n");
    buffer.append("    <relativePath>..</relativePath>\n");
    buffer.append("  </parent>\n");
    buffer.append("  \n");
    buffer.append("  <build>\n");
    buffer.append("    <plugins>\n");
    buffer.append("      <plugin>\n");
    buffer.append("        <groupId>org.eclipse.tycho</groupId>\n");
    buffer.append("        <artifactId>tycho-maven-plugin</artifactId>\n");
    buffer.append("        <version>1.0.0</version>\n");
    buffer.append("        <extensions>true</extensions>\n");
    buffer.append("      </plugin>\n");
    buffer.append("      <!--\n");
    buffer.append("        This plugins builds the update site for the current release\n");
    buffer.append("        and puts all files in ${project.build.directory}/repository\n");
    buffer.append("        -->\n");
    buffer.append("      <plugin>\n");
    buffer.append("        <groupId>org.eclipse.tycho</groupId>\n");
    buffer.append("        <artifactId>tycho-p2-repository-plugin</artifactId>\n");
    buffer.append("        <version>1.0.0</version>\n");
    buffer.append("        <configuration>\n");
    buffer.append("          <includeAllDependencies>true</includeAllDependencies>\n");
    buffer.append("          <compress>false</compress>\n");
    buffer.append("          <repositoryName>"
        + featureProvider
        + " Update Site</repositoryName>\n");
    buffer.append("        </configuration>\n");
    buffer.append("      </plugin>\n");
    buffer.append("    </plugins>\n");
    buffer.append("  </build>\n");
    buffer.append("</project>\n");
    buffer.append("\n");
    buffer.append("\n");
    buffer.append("");

    writer = new PrintWriter(currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
        + GenerateAllInOneP2Feature.SECOND_LEVEL_SITE_PROJECT + "/" + GenerateAllInOneP2Feature.MAVEN_PROJECT_FILE_NAME, "UTF-8");
    writer.println(buffer);
    writer.close();
  }

}
