package org.ietr.externaldeps;

import java.io.File;
import java.io.IOException;

public class CheckParameters {

  public static final void checkParameters(final File inputSite, final String featureName, final File outputDirectory) throws IOException {
    CheckParameters.checkInputFolder(inputSite);
    CheckParameters.checkOutputFolder(outputDirectory);
    CheckParameters.checkFeatureName(featureName);
  }

  private static final void checkFeatureName(final String featureName) {
    if (featureName == null) {
      throw new NullPointerException();
    }
    if (featureName.isEmpty()) {
      throw new IllegalArgumentException();
    }
  }

  private static void checkOutputFolder(final File outputDirectory) throws IOException {
    final boolean exists = outputDirectory.exists();
    if (exists) {
      final boolean canWrite = outputDirectory.canWrite();
      if (!canWrite) {
        throw new IOException("Output exists but is not writeable [" + outputDirectory + "]");
      }
      final boolean isDir = outputDirectory.isDirectory();
      if (!isDir) {
        throw new IOException("Output exists but is not a direcetory [" + outputDirectory + "]");
      }
    } else {
      final boolean mkdirs = outputDirectory.mkdirs();
      if (!mkdirs) {
        throw new IOException("Output exists but is not a direcetory [" + outputDirectory + "]");
      }
    }
  }

  private static void checkInputFolder(final File inputSite) throws IOException {
    final boolean exists = inputSite.exists();
    if (!exists) {
      throw new IOException("Could not locate input folder [" + inputSite + "]");
    }
    final boolean canRead = inputSite.canRead();
    if (!canRead) {
      throw new IOException("Can not read input folder [" + inputSite + "]");
    }
    final boolean isDir = inputSite.isDirectory();
    if (!isDir) {
      throw new IOException("Input folder is not a directory [" + inputSite + "]");
    }
    final File[] list = inputSite.listFiles();
    final boolean isEmpty = list.length == 0;
    if (isEmpty) {
      throw new IOException("Input folder is empty [" + inputSite + "]");
    }

    boolean containsPluginFolder = false;
    boolean containsContentXml = false;

    for (final File file : list) {
      if ("plugins".equals(file.getName())) {
        containsPluginFolder = true;
        CheckParameters.checkPluginFolder(file);
      }
      if ("content.xml".equals(file.getName())) {
        containsContentXml = true;
      }
    }
    if (!containsContentXml) {
      throw new IllegalArgumentException("Could not locate content.xml file");
    }
    if (!containsPluginFolder) {
      throw new IllegalArgumentException("Could not locate plugins folder");
    }
  }

  private static final void checkPluginFolder(final File inputPluginFolder) throws IOException {

    final boolean exists = inputPluginFolder.exists();
    if (!exists) {
      throw new IOException("Could not locate input plugin folder [" + inputPluginFolder + "]");
    }
    final boolean canRead = inputPluginFolder.canRead();
    if (!canRead) {
      throw new IOException("Can not read input plugin folder [" + inputPluginFolder + "]");
    }
    final boolean isDir = inputPluginFolder.isDirectory();
    if (!isDir) {
      throw new IOException("Input plugin folder is not a directory [" + inputPluginFolder + "]");
    }
    final File[] list = inputPluginFolder.listFiles();
    final boolean isEmpty = list.length == 0;
    if (isEmpty) {
      throw new IOException("Input plugin folder is empty [" + inputPluginFolder + "]");
    }
  }
}
