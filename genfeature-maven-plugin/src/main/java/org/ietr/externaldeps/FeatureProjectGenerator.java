package org.ietr.externaldeps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.plexus.util.FileUtils;

public class FeatureProjectGenerator {

  public void generateProject(final File inputSite, final String featureName, final File currentWorkingDirectory)
      throws FileNotFoundException, UnsupportedEncodingException, IOException {
    generateFeaturePomFile(currentWorkingDirectory, featureName, inputSite);
    generateFeatureFile(currentWorkingDirectory, featureName, inputSite);
    generateBuildProperties(currentWorkingDirectory);
  }

  private void generateFeaturePomFile(final File currentWorkingDirectory, final String featureName, final File inputSite)
      throws FileNotFoundException, UnsupportedEncodingException {
    PrintWriter writer;

    String toto;

    toto = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
        + "  xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" + "  <modelVersion>4.0.0</modelVersion>\n"
        + "\n" + "  <artifactId>" + featureName + "</artifactId>\n" + "  <packaging>eclipse-feature</packaging>\n" + "  \n" + "  <parent>\n"
        + "  <artifactId>org.ietr.externaldeps.parent</artifactId>\n" + "  <groupId>org.ietr.externaldeps</groupId>\n" + "    <version>1.0.0</version>\n"
        + "    <relativePath>..</relativePath>\n" + "  </parent>\n" + "  \n" + "  <build>\n" + "    <plugins>\n" + "      <plugin>\n"
        + "        <groupId>org.eclipse.tycho</groupId>\n" + "        <artifactId>tycho-maven-plugin</artifactId>\n" + "        <version>1.0.0</version>\n"
        + "        <extensions>true</extensions>\n" + "      </plugin>\n" + "    </plugins>\n" + "  </build>\n" + "</project>\n" + "";

    writer = new PrintWriter(currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
        + GenerateAllInOneP2Feature.SECOND_LEVEL_FEATURE_PROJECT + "/" + GenerateAllInOneP2Feature.MAVEN_PROJECT_FILE_NAME, "UTF-8");
    writer.println(toto);
    writer.close();
  }

  private void generateFeatureFile(final File currentWorkingDirectory, final String featureName, final File inputSite) throws IOException {

    final List<PluginIU> pluginList = generatePluginList(inputSite);

    final StringBuffer buffer = new StringBuffer();
    buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    buffer.append("\n");
    buffer.append("<feature id=\"").append(featureName).append("\" version=\"1.0.0\" provider-name=\"IETR/INSA Rennes\">\n");
    buffer.append("\n");
    for (final PluginIU plugin : pluginList) {
      buffer.append("\t" + plugin.generateFeatureSection() + "\n");
    }
    buffer.append("\n");
    buffer.append("</feature>\n");
    buffer.append("\n");

    final PrintWriter writer = new PrintWriter(currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
        + GenerateAllInOneP2Feature.SECOND_LEVEL_FEATURE_PROJECT + "/" + GenerateAllInOneP2Feature.FEATURE_FILE_NAME, "UTF-8");
    writer.println(buffer.toString());
    writer.close();
  }

  private void generateBuildProperties(final File currentWorkingDirectory) throws FileNotFoundException, UnsupportedEncodingException {
    PrintWriter writer;
    final String buildPropertiesContent = "bin.includes = " + GenerateAllInOneP2Feature.FEATURE_FILE_NAME;

    writer = new PrintWriter(currentWorkingDirectory.getAbsoluteFile() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
        + GenerateAllInOneP2Feature.SECOND_LEVEL_FEATURE_PROJECT + "/" + GenerateAllInOneP2Feature.BUILD_PROPERTIES_FILE_NAME, "UTF-8");
    writer.println(buildPropertiesContent);
    writer.close();
  }

  private final List<PluginIU> generatePluginList(final File inputSite) {
    final List<PluginIU> pluginList = new ArrayList<PluginIU>();
    final String pluginsPath = inputSite.getAbsolutePath() + "/plugins";
    final File pluginFolder = new File(pluginsPath);
    final File[] listFiles = pluginFolder.listFiles();
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
