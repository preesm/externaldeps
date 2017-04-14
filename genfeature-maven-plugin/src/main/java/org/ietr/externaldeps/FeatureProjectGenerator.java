package org.ietr.externaldeps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.plexus.util.FileUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class FeatureProjectGenerator.
 */
public class FeatureProjectGenerator {

  /**
   * Generate project.
   *
   * @param generateAllInOneP2Feature
   *          the generate all in one P 2 feature
   * @throws FileNotFoundException
   *           the file not found exception
   * @throws UnsupportedEncodingException
   *           the unsupported encoding exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public void generateProject(final GenerateAllInOneP2Feature generateAllInOneP2Feature)
      throws FileNotFoundException, UnsupportedEncodingException, IOException {
    generateFeaturePomFile(generateAllInOneP2Feature);
    generateFeatureFile(generateAllInOneP2Feature);
    generateBuildProperties(generateAllInOneP2Feature);
  }

  /**
   * Generate feature pom file.
   *
   * @param generateAllInOneP2Feature
   *          the generate all in one P 2 feature
   * @throws FileNotFoundException
   *           the file not found exception
   * @throws UnsupportedEncodingException
   *           the unsupported encoding exception
   */
  private void generateFeaturePomFile(final GenerateAllInOneP2Feature generateAllInOneP2Feature) throws FileNotFoundException, UnsupportedEncodingException {
    final StringBuffer buffer = new StringBuffer();

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

    PrintWriter writer = new PrintWriter(
        generateAllInOneP2Feature.currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
            + GenerateAllInOneP2Feature.SECOND_LEVEL_FEATURE_PROJECT + "/" + GenerateAllInOneP2Feature.MAVEN_PROJECT_FILE_NAME,
        "UTF-8");
    writer.println(buffer);
    writer.close();
  }

  /**
   * Generate feature file.
   *
   * @param generateAllInOneP2Feature
   *          the generate all in one P 2 feature
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private void generateFeatureFile(final GenerateAllInOneP2Feature generateAllInOneP2Feature) throws IOException {

    final List<PluginIU> pluginList = generatePluginList(generateAllInOneP2Feature.inputSite);

    final StringBuffer buffer = new StringBuffer();
    buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    buffer.append("\n");
    buffer.append("<feature id=\"" + generateAllInOneP2Feature.project.getGroupId() + ".").append(generateAllInOneP2Feature.featureId)
        .append("\" version=\"" + generateAllInOneP2Feature.project.getVersion() + "\" provider-name=\"" + generateAllInOneP2Feature.featureProvider + "\">\n");
    buffer.append("\n");
    for (final PluginIU plugin : pluginList) {
      buffer.append("\t" + plugin.generateFeatureSection() + "\n");
    }
    buffer.append("\n");
    buffer.append("</feature>\n");
    buffer.append("\n");

    final PrintWriter writer = new PrintWriter(
        generateAllInOneP2Feature.currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
            + GenerateAllInOneP2Feature.SECOND_LEVEL_FEATURE_PROJECT + "/" + GenerateAllInOneP2Feature.FEATURE_FILE_NAME,
        "UTF-8");
    writer.println(buffer.toString());
    writer.close();
  }

  /**
   * Generate build properties.
   *
   * @param generateAllInOneP2Feature
   *          the generate all in one P 2 feature
   * @throws FileNotFoundException
   *           the file not found exception
   * @throws UnsupportedEncodingException
   *           the unsupported encoding exception
   */
  private void generateBuildProperties(final GenerateAllInOneP2Feature generateAllInOneP2Feature) throws FileNotFoundException, UnsupportedEncodingException {
    PrintWriter writer;
    final String buildPropertiesContent = "bin.includes = " + GenerateAllInOneP2Feature.FEATURE_FILE_NAME;

    writer = new PrintWriter(generateAllInOneP2Feature.currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME
        + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FEATURE_PROJECT + "/" + GenerateAllInOneP2Feature.BUILD_PROPERTIES_FILE_NAME, "UTF-8");
    writer.println(buildPropertiesContent);
    writer.close();
  }

  /**
   * Generate plugin list.
   *
   * @param inputSite
   *          the input site
   * @return the list
   */
  private final List<PluginIU> generatePluginList(final File inputSite) {
    final List<PluginIU> pluginList = new ArrayList<PluginIU>();
    final String pluginsPath = inputSite.getAbsolutePath() + "/plugins";
    final File pluginFolder = new File(pluginsPath);
    final File[] listFiles = pluginFolder.listFiles();
    if (listFiles == null) {
      throw new RuntimeException();
    }
    for (final File file : listFiles) {
      final boolean isDirectory = file.isDirectory();
      if (isDirectory) {
        // skip directories
        continue;
      }
      final String fileName = file.getName();
      final String fileExtension = FileUtils.extension(fileName);
      if (!"jar".equals(fileExtension.toLowerCase())) {
        // skip non jar files
        continue;
      }
      pluginList.add(PluginIU.build(fileName));
    }
    return pluginList;
  }
}
