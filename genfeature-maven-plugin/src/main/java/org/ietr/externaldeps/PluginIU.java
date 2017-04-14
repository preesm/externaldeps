package org.ietr.externaldeps;

import org.codehaus.plexus.util.FileUtils;

public class PluginIU {

  public static void main(final String[] args) {
    final String input = "jfree.jcommon_1.0.16.jar";
    final PluginIU build = PluginIU.build(input);
    System.out.println(build);
  }

  public static final PluginIU build(final String jarFileName) {
    final String removeExtension = FileUtils.removeExtension(jarFileName);
    final int lastIndexOf = jarFileName.lastIndexOf("_");
    final String artName = removeExtension.substring(0, lastIndexOf);
    final String version = removeExtension.substring(lastIndexOf + 1);
    return new PluginIU(artName, version);
  }

  private final String artifactName;
  private final String version;

  private PluginIU(final String artifactName, final String version) {
    this.artifactName = artifactName;
    this.version = version;
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append(this.artifactName).append(":").append(this.version);
    return buffer.toString();
  }

  public final String generateFeatureSection() {

    final StringBuffer buffer = new StringBuffer();
    buffer.append("<plugin id=\"").append(this.artifactName).append("\" download-size=\"0\" install-size=\"0\" version=\"").append(this.version)
        .append("\" unpack=\"false\"/>");
    return buffer.toString();
  }
}
