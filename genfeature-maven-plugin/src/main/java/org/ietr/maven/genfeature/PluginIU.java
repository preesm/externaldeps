package org.ietr.maven.genfeature;

import org.codehaus.plexus.util.FileUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class PluginIU.
 */
public class PluginIU {

  /**
   * The main method.
   *
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {
    final String input = "jfree.jcommon_1.0.16.jar";
    final PluginIU build = PluginIU.build(input);
    System.out.println(build);
  }

  /**
   * Builds the.
   *
   * @param jarFileName
   *          the jar file name
   * @return the plugin IU
   */
  public static final PluginIU build(final String jarFileName) {
    final String removeExtension = FileUtils.removeExtension(jarFileName);
    final int lastIndexOf = jarFileName.lastIndexOf("_");
    final String artName = removeExtension.substring(0, lastIndexOf);
    final String version = removeExtension.substring(lastIndexOf + 1);
    return new PluginIU(artName, version);
  }

  /** The artifact name. */
  private final String artifactName;

  /** The version. */
  private final String version;

  /**
   * Instantiates a new plugin IU.
   *
   * @param artifactName
   *          the artifact name
   * @param version
   *          the version
   */
  private PluginIU(final String artifactName, final String version) {
    this.artifactName = artifactName;
    this.version = version;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append(this.artifactName).append(":").append(this.version);
    return buffer.toString();
  }

  /**
   * Generate feature section.
   *
   * @return the string
   */
  public final String generateFeatureSection() {

    final StringBuffer buffer = new StringBuffer();
    buffer.append("<plugin id=\"").append(this.artifactName).append("\" download-size=\"0\" install-size=\"0\" version=\"").append(this.version)
        .append("\" unpack=\"false\"/>");
    return buffer.toString();
  }
}
