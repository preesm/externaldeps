package org.ietr.maven.genfeature;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * The Class SiteProjectGenerator.
 */
public class SiteProjectGenerator {

  /**
   * Generate project.
   *
   * @param generateAllInOneP2Feature
   *          the generate all in one P 2 feature
   * @throws FileNotFoundException
   *           the file not found exception
   * @throws UnsupportedEncodingException
   *           the unsupported encoding exception
   */
  public void generateProject(final GenerateAllInOneP2Feature generateAllInOneP2Feature) throws IOException {

    generateSitePomFile(generateAllInOneP2Feature);
    generateSiteCategoryFile(generateAllInOneP2Feature);
  }

  /**
   * Generate site category file.
   *
   * @param generateAllInOneP2Feature
   *          the generate all in one P 2 feature
   * @throws FileNotFoundException
   *           the file not found exception
   * @throws UnsupportedEncodingException
   *           the unsupported encoding exception
   */
  private void generateSiteCategoryFile(final GenerateAllInOneP2Feature generateAllInOneP2Feature) throws FileNotFoundException, UnsupportedEncodingException {
    final StringBuilder buffer = new StringBuilder();

    buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    buffer.append("<site>\n");
    buffer.append("   <feature url=\"features/" + generateAllInOneP2Feature.project.getGroupId() + "." + generateAllInOneP2Feature.featureId + "\" id=\""
        + generateAllInOneP2Feature.project.getGroupId() + "." + generateAllInOneP2Feature.featureId + "\" >\n");
    buffer.append("      <category name=\"" + generateAllInOneP2Feature.featureId + "_cat\"/>\n");
    buffer.append("   </feature>\n");
    buffer.append("   \n");
    buffer.append("   <category-def name=\"" + generateAllInOneP2Feature.featureId + "_cat\" label=\"" + generateAllInOneP2Feature.featureName + "\">\n");
    buffer.append("   </category-def>\n");
    buffer.append("</site>\n");
    buffer.append("");

    final PrintWriter writer = new PrintWriter(
        generateAllInOneP2Feature.currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
            + GenerateAllInOneP2Feature.SECOND_LEVEL_SITE_PROJECT + "/" + GenerateAllInOneP2Feature.CATEGORY_FILE_NAME,
        GenerateAllInOneP2Feature.CHARSET);
    writer.println(buffer);
    writer.close();
  }

  /**
   * Generate site pom file.
   *
   * @param generateAllInOneP2Feature
   *          the generate all in one P 2 feature
   * @throws FileNotFoundException
   *           the file not found exception
   * @throws UnsupportedEncodingException
   *           the unsupported encoding exception
   */
  private void generateSitePomFile(final GenerateAllInOneP2Feature generateAllInOneP2Feature) throws FileNotFoundException, UnsupportedEncodingException {
    final StringBuilder buffer = new StringBuilder();

    buffer.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" ");
    buffer.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
    buffer.append("xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");
    buffer.append("  <modelVersion>4.0.0</modelVersion>\n");
    buffer.append("  <artifactId>org.ietr.externaldeps.dependency.site</artifactId>\n");
    buffer.append("  <packaging>eclipse-repository</packaging>\n");
    buffer.append("  \n");
    test(generateAllInOneP2Feature, buffer);
    buffer.append("      <plugin>\n");
    buffer.append("        <groupId>org.eclipse.tycho</groupId>\n");
    buffer.append("        <artifactId>tycho-p2-repository-plugin</artifactId>\n");
    buffer.append("        <version>" + GenerateAllInOneP2Feature.TYCHO_VERSION + "</version>\n");
    buffer.append("        <configuration>\n");
    buffer.append("          <includeAllDependencies>true</includeAllDependencies>\n");
    buffer.append("          <compress>false</compress>\n");
    buffer.append("          <repositoryName>" + generateAllInOneP2Feature.featureProvider + " Update Site</repositoryName>\n");
    buffer.append("        </configuration>\n");
    buffer.append("      </plugin>\n");
    buffer.append("    </plugins>\n");
    buffer.append("  </build>\n");
    buffer.append("</project>\n");
    buffer.append("\n");
    buffer.append("");

    final PrintWriter writer = new PrintWriter(
        generateAllInOneP2Feature.currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
            + GenerateAllInOneP2Feature.SECOND_LEVEL_SITE_PROJECT + "/" + GenerateAllInOneP2Feature.MAVEN_PROJECT_FILE_NAME,
        GenerateAllInOneP2Feature.CHARSET);
    writer.println(buffer);
    writer.close();
  }

  public static void test(final GenerateAllInOneP2Feature generateAllInOneP2Feature, final StringBuilder buffer) {
    buffer.append("  <parent>\n");
    buffer.append("    <artifactId>" + generateAllInOneP2Feature.project.getGroupId() + ".parent</artifactId>\n");
    buffer.append("    <groupId>" + generateAllInOneP2Feature.project.getGroupId() + "</groupId>\n");
    buffer.append("    <version>" + generateAllInOneP2Feature.project.getVersion() + "</version>\n");
    buffer.append("    <relativePath>..</relativePath>\n");
    buffer.append("  </parent>\n");
    buffer.append("  \n");
    buffer.append("  <build>\n");
    buffer.append("    <plugins>\n");
    buffer.append("      <plugin>\n");
    buffer.append("        <groupId>org.eclipse.tycho</groupId>\n");
    buffer.append("        <artifactId>tycho-maven-plugin</artifactId>\n");
    buffer.append("        <version>" + GenerateAllInOneP2Feature.TYCHO_VERSION + "</version>\n");
    buffer.append("        <extensions>true</extensions>\n");
    buffer.append("      </plugin>\n");
  }

}
