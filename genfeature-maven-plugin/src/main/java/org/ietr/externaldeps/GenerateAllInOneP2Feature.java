package org.ietr.externaldeps;

import java.io.File;
import java.util.Arrays;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.cli.CommandLineException;

@Mojo(name = "generate-feature", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateAllInOneP2Feature extends AbstractMojo {

  public static final String SECOND_LEVEL_FOLDER_NAME     = "secondLevel";
  public static final String SECOND_LEVEL_FEATURE_PROJECT = "feature";
  public static final String FEATURE_FILE_NAME            = "feature.xml";
  public static final String CATEGORY_FILE_NAME           = "category.xml";
  public static final String BUILD_PROPERTIES_FILE_NAME   = "build.properties";
  public static final String SECOND_LEVEL_SITE_PROJECT    = "site";
  public static final String MAVEN_PROJECT_FILE_NAME      = "pom.xml";

  @Parameter(defaultValue = "${project.build.directory}", property = "currentWorkingDirectory", required = true)
  private File currentWorkingDirectory;

  @Parameter(defaultValue = "${project.build.directory}/repository/", property = "inputSite", required = true)
  private File inputSite;

  @Parameter(defaultValue = "${project.build.directory}/repository-featured/", property = "outputDirectory", required = true)
  private File outputDirectory;

  @Parameter(defaultValue = "${project.artifactId}", property = "featureName", required = true)
  private String featureName;

  public void execute() throws MojoFailureException {
    getLog().info("Starting all-in-one feature generation for all jars in ");
    getLog().info(this.inputSite.getAbsolutePath());

    try {
      new SecondLevelGenerator().generate(this.currentWorkingDirectory, this.inputSite, this.featureName);

      getLog().info("Calling 2nd level");
      call2ndLevel();
      getLog().info("2nd level done");

      getLog().info("Copy generated repository");
      FileUtils.copyDirectoryStructure(
          new File(currentWorkingDirectory.getAbsolutePath() + "/" + SECOND_LEVEL_FOLDER_NAME + "/" + SECOND_LEVEL_SITE_PROJECT + "/target/repository"),
          outputDirectory);
    } catch (final Exception e) {
      e.printStackTrace();
      throw new MojoFailureException(e, "Could not execute second level", e.getMessage());
    }
    getLog().info("Feature generated.");
  }

  private void call2ndLevel() throws MavenInvocationException {
    final InvocationRequest request = new DefaultInvocationRequest();
    request.setDebug(false);
    request.setPomFile(new File(this.currentWorkingDirectory.getAbsolutePath() + "/" + GenerateAllInOneP2Feature.SECOND_LEVEL_FOLDER_NAME + "/"
        + GenerateAllInOneP2Feature.MAVEN_PROJECT_FILE_NAME));
    request.setGoals(Arrays.asList("clean", "package"));
    final Invoker invoker = new DefaultInvoker();
    final InvocationResult result = invoker.execute(request);
    if (result.getExitCode() != 0) {
      final CommandLineException executionException = result.getExecutionException();
      throw new IllegalStateException("Build failed: return code != 0", executionException);
    }
  }

}
