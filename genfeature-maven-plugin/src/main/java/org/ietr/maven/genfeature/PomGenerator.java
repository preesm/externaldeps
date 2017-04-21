package org.ietr.maven.genfeature;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class PomGenerator {

  public static final String generateSitePomFile(final GenerateAllInOneP2Feature generateAllInOneP2Feature)
      throws FileNotFoundException, UnsupportedEncodingException {
    final StringBuilder buffer = new StringBuilder();

    buffer.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" ");
    buffer.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
    buffer.append("xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");
    buffer.append("  <modelVersion>4.0.0</modelVersion>\n");
    buffer.append("  <artifactId>org.ietr.externaldeps.dependency.site</artifactId>\n");
    buffer.append("  <packaging>eclipse-repository</packaging>\n");
    buffer.append("  \n");
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

    return buffer.toString();
  }

  public static final String generateFeaturePomFile(final GenerateAllInOneP2Feature generateAllInOneP2Feature)
      throws FileNotFoundException, UnsupportedEncodingException {
    final StringBuilder buffer = new StringBuilder();

    buffer.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
    buffer.append("  xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");
    buffer.append("  <modelVersion>4.0.0</modelVersion>\n");
    buffer.append("\n");
    buffer.append("  <artifactId>");
    buffer.append(generateAllInOneP2Feature.project.getGroupId() + ".");
    buffer.append(generateAllInOneP2Feature.featureId);
    buffer.append("</artifactId>\n");
    buffer.append("  <packaging>eclipse-feature</packaging>\n");
    buffer.append("  \n");
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
    buffer.append("    </plugins>\n");
    buffer.append("  </build>\n");
    buffer.append("</project>\n");
    buffer.append("");

    return buffer.toString();
  }
}
