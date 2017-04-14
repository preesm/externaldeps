package org.ietr.externaldeps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class SiteProjectGenerator {

  public void generateProject(final File inputSite, final String featureName, final File currentWorkingDirectory)
      throws FileNotFoundException, UnsupportedEncodingException {

    generateSitePomFile(currentWorkingDirectory, featureName, inputSite);
    generateSiteCategoryFile(currentWorkingDirectory, featureName, inputSite);
  }

  private void generateSiteCategoryFile(final File currentWorkingDirectory, final String featureName, final File inputSite)
      throws FileNotFoundException, UnsupportedEncodingException {

    PrintWriter writer;

    String toto;

    toto = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<site>\n"
        + "   <feature url=\"features/org.ietr.externaldeps.dependency.site\" id=\"org.ietr.externaldeps.dependency.site\" >\n"
        + "      <category name=\"PREESM\"/>\n" + "   </feature>\n" + "   \n" + "   <category-def name=\"source_components\" label=\"Developer Resources\">\n"
        + "      <description>\n" + "         Developper resources (includes source code).\n" + "      </description>\n" + "   </category-def>\n" + "   \n"
        + "   <category-def name=\"PREESM\" label=\"PREESM\">\n" + "      <description>\n"
        + "         PREESM (the Parallel and Real-time Embedded Executives Scheduling Method) is an open source rapid prototyping and code generation tool. It is primarily employed to simulate signal processing applications and generate code for multi-core Digital Signal Processors. PREESM is developed at the Institute of Electronics and Telecommunications-Rennes (IETR) in collaboration with Texas Instruments France in Nice.\n"
        + "      </description>\n" + "   </category-def>\n"
        + "   <repository-reference location=\"http://download.eclipse.org/releases/neon/\" enabled=\"true\" />\n"
        + "   <repository-reference location=\"http://preesm.sourceforge.net/eclipse/update-site/\" enabled=\"true\" />\n" + "</site>\n" + "";

    writer = new PrintWriter(currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
        + GenerateAllInOneP2Feature.SECOND_LEVEL_SITE_PROJECT + "/" + GenerateAllInOneP2Feature.CATEGORY_FILE_NAME, "UTF-8");
    writer.println(toto);
    writer.close();
  }

  private void generateSitePomFile(final File currentWorkingDirectory, final String featureName, final File inputSite)
      throws FileNotFoundException, UnsupportedEncodingException {
    PrintWriter writer;

    String toto;

    toto = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
        + "  <modelVersion>4.0.0</modelVersion>\n" + "  <artifactId>org.ietr.externaldeps.dependency.site.site</artifactId>\n"
        + "  <packaging>eclipse-repository</packaging>\n" + "  \n" + "  <parent>\n" + "  <artifactId>org.ietr.externaldeps.parent</artifactId>\n"
        + "  <groupId>org.ietr.externaldeps</groupId>\n" + "    <version>1.0.0</version>\n" + "    <relativePath>..</relativePath>\n" + "  </parent>\n" + "  \n"
        + "  <build>\n" + "    <plugins>\n" + "      <plugin>\n" + "        <groupId>org.eclipse.tycho</groupId>\n"
        + "        <artifactId>tycho-maven-plugin</artifactId>\n" + "        <version>1.0.0</version>\n" + "        <extensions>true</extensions>\n"
        + "      </plugin>\n" + "      <!--\n" + "        This plugins builds the update site for the current release\n"
        + "        and puts all files in ${project.build.directory}/repository\n" + "        -->\n" + "      <plugin>\n"
        + "        <groupId>org.eclipse.tycho</groupId>\n" + "        <artifactId>tycho-p2-repository-plugin</artifactId>\n"
        + "        <version>1.0.0</version>\n" + "        <configuration>\n" + "          <includeAllDependencies>true</includeAllDependencies>\n"
        + "          <compress>false</compress>\n" + "          <repositoryName>IETR/INSA - Rennes Update Site</repositoryName>\n"
        + "        </configuration>\n" + "      </plugin>\n" + "    </plugins>\n" + "  </build>\n" + "</project>\n" + "\n" + "\n" + "";

    writer = new PrintWriter(currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
        + GenerateAllInOneP2Feature.SECOND_LEVEL_SITE_PROJECT + "/" + GenerateAllInOneP2Feature.MAVEN_PROJECT_FILE_NAME, "UTF-8");
    writer.println(toto);
    writer.close();
  }

}
