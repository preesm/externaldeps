package org.ietr.externaldeps.sftp_maven_plugin;

import java.io.File;
import java.text.MessageFormat;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;

@Mojo(name = "sftp-transfert", defaultPhase = LifecyclePhase.NONE)
public final class SftpTransfertMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  public MavenProject project;

  @Parameter(property = "serverId", required = true)
  private String serverId;

  @Parameter(property = "serverHost", required = true)
  private String serverHost;

  @Parameter(property = "mode", defaultValue = "receive", required = true)
  private String mode;

  @Parameter(defaultValue = "22", property = "serverPort", required = true)
  private int serverPort;

  @Parameter(defaultValue = "true", property = "strictHostKeyChecking", required = true)
  private boolean strictHostKeyChecking;

  @Parameter(property = "localPath", required = true)
  private String localPath;
  @Parameter(property = "remotePath", required = true)
  private String remotePath;

  @Parameter(defaultValue = "${settings}", readonly = true)
  private Settings settings;

  @Parameter(defaultValue = "${project.basedir}", readonly = true)
  private File basedir;

  @Parameter(defaultValue = "${project.build.directory}", readonly = true)
  private File target;

  @Override
  public final void execute() throws MojoExecutionException, MojoFailureException {
    final Log log = getLog();

    final Server server = this.settings.getServer(this.serverId);
    if (server == null) {
      final String message = MessageFormat.format(
          "Error: Could not find server with id '{0}'. Make sure you have a <servers>...</servers> section with proper <server> configuration in your maven settings. See https://maven.apache.org/settings.html#Servers.",
          this.serverId);
      log.error(message);
      throw new MojoFailureException(message);
    }

    final String SFTPHOST = this.serverHost;
    final int SFTPPORT = this.serverPort;
    final String SFTPUSER = server.getUsername();
    final String SFTPPASS = server.getPassword();
    final boolean receivingMode;

    if ("receive".equals(this.mode)) {
      receivingMode = true;
    } else {
      if ("send".equals(this.mode)) {
        receivingMode = false;
      } else {
        final String message = MessageFormat.format("Unsupported mode '{0}'. Supported modes are 'receive' (default) and 'send'.", this.mode);
        log.error(message);
        throw new MojoFailureException(message);
      }
    }

    final SftpConnection sftpTransfert = new SftpConnection(log, SFTPUSER, SFTPHOST, SFTPPORT, SFTPPASS, this.strictHostKeyChecking);
    if (receivingMode) {
      sftpTransfert.receive(this.remotePath, this.localPath);
    } else {
      sftpTransfert.send(this.localPath, this.remotePath);
    }
    sftpTransfert.disconnect();
  }
}
