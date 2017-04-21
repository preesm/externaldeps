package org.ietr.externaldeps.sftp_maven_plugin;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JschSftpTransfertLayer implements ISftpTransfertLayer {

  private static final ExecutorService threadPool = Executors.newFixedThreadPool(8);

  public static final JschSftpTransfertLayer connect(final String host, final int port, final String user, final String password,
      final boolean strictHostKeyChecking) {
    final JschSftpTransfertLayer jschSftpConnection = new JschSftpTransfertLayer();
    try {
      jschSftpConnection.connectTo(host, port, user, password, strictHostKeyChecking);
    } catch (final JSchException e) {
      e.printStackTrace();
      return null;
    }
    return jschSftpConnection;
  }

  private JschSftpTransfertLayer() {
  }

  private Session session   = null;
  private boolean connected = false;

  @Override
  public final void connectTo(final String host, final int port, final String user, final String password, final boolean strictHostKeyChecking)
      throws JSchException {
    final JSch jsch = new JSch();
    this.session = jsch.getSession(user, host, port);
    this.session.setPassword(password);
    final java.util.Properties config = new java.util.Properties();
    if (!strictHostKeyChecking) {
      // do not check for key checking
      config.put("StrictHostKeyChecking", "no");
    }
    this.session.setConfig(config);
    this.session.connect();
    this.connected = true;
  }

  @Override
  public final void disconnect() {
    JschSftpTransfertLayer.threadPool.shutdown();
    try {
      JschSftpTransfertLayer.threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    } catch (final InterruptedException e) {
    }
    JschSftpTransfertLayer.threadPool.shutdown();
    this.session.disconnect();
    this.connected = false;
  }

  @Override
  public final boolean isConnected() {
    return this.connected;
  }

  @Override
  public final void send(final String localFilePath, final String remoteFilePath) {
    JschSftpTransfertLayer.threadPool.execute(() -> {
      ChannelSftp sftpChannel = null;
      try {
        sftpChannel = (ChannelSftp) this.session.openChannel("sftp");
        sftpChannel.connect();

        sftpChannel.put(localFilePath, remoteFilePath);

      } catch (JSchException | SftpException e) {
        e.printStackTrace();
      } finally {
        sftpChannel.exit();
        sftpChannel.disconnect();
      }
    });
  }

  @Override
  public final void receive(final String remoteFilePath, final String localFilePath) {
    JschSftpTransfertLayer.threadPool.execute(() -> {
      ChannelSftp sftpChannel = null;
      try {
        sftpChannel = (ChannelSftp) this.session.openChannel("sftp");
        sftpChannel.connect();

        sftpChannel.get(remoteFilePath, localFilePath);

      } catch (JSchException | SftpException e) {
        e.printStackTrace();
      } finally {
        sftpChannel.exit();
        sftpChannel.disconnect();
      }
    });
  }

  @Override
  public final void mkdir(final String remoteDirPath) throws JSchException, SftpException {
    ChannelSftp sftpChannel = null;
    try {
      sftpChannel = (ChannelSftp) this.session.openChannel("sftp");
      sftpChannel.connect();

      sftpChannel.mkdir(remoteDirPath);

    } finally {
      sftpChannel.exit();
      sftpChannel.disconnect();
    }
  }

  @Override
  public final boolean isDirectory(final String remoteDirPath) {
    ChannelSftp sftpChannel = null;
    try {
      sftpChannel = (ChannelSftp) this.session.openChannel("sftp");
      sftpChannel.connect();

      try {
        sftpChannel.readlink(remoteDirPath);
        sftpChannel.exit();
        sftpChannel.disconnect();
        return false;
      } catch (final Exception e) {
        // expected exception
      }
      // do not use this.ls
      @SuppressWarnings("unchecked")
      final Vector<LsEntry> ls = sftpChannel.ls(remoteDirPath);
      final int size = ls.size();
      // ls should list . and .. at least if it is a directory. If it is a file, it will list the filename itself only
      final boolean isDir = size > 1;
      return isDir;
    } catch (final Exception e) {
      return false;
    } finally {
      sftpChannel.exit();
      sftpChannel.disconnect();
    }
  }

  @Override
  public final void mkdirs(final String remoteDirPath) throws SftpException, JSchException {
    // final boolean isDirectory = isDirectory(remoteDirPath);
    // if (isDirectory) {
    // return;
    // }
    final Path remoteDestinationDir = Paths.get(remoteDirPath);
    final Stack<String> parents = new Stack<>();
    Path parent = remoteDestinationDir;
    while (parent != null) {
      parents.push(parent.toAbsolutePath().toString());
      parent = parent.getParent();
    }
    while (!parents.isEmpty()) {
      final String currentParentToTest = parents.pop();
      final boolean existDir = isDirectory(currentParentToTest);
      if (!existDir) {
        mkdir(currentParentToTest);
      }
    }
  }

  @Override
  public final List<String> ls(final String remoteDirPath) throws SftpException, JSchException {
    final List<String> res = new ArrayList<>();

    final ChannelSftp sftpChannel = (ChannelSftp) this.session.openChannel("sftp");
    sftpChannel.connect();

    try {
      @SuppressWarnings("unchecked")
      final Vector<LsEntry> ls = sftpChannel.ls(remoteDirPath);
      for (final LsEntry fileEntry : ls) {
        final String filename = fileEntry.getFilename();
        if (".".equals(filename) || "..".equals(filename) || filename.startsWith(".")) {
          continue;
        }
        res.add(remoteDirPath + "/" + filename);
      }
    } finally {
      sftpChannel.exit();
      sftpChannel.disconnect();
    }

    return res;
  }

  @Override
  public final boolean isSymlink(final String remotePath) throws JSchException, SftpException {
    final ChannelSftp sftpChannel = (ChannelSftp) this.session.openChannel("sftp");
    sftpChannel.connect();
    boolean res;
    try {
      sftpChannel.readlink(remotePath);
      res = true;
    } catch (final Exception e) {
      res = false;
    } finally {
      sftpChannel.exit();
      sftpChannel.disconnect();
    }
    return res;
  }

  @Override
  public final void writeSymlink(final String remotePath, final String linkPath) throws JSchException, SftpException {
    final ChannelSftp sftpChannel = (ChannelSftp) this.session.openChannel("sftp");
    sftpChannel.connect();
    try {

      final Path path = Paths.get(remotePath);
      final Path parent = path.getParent();
      final String linkParentDirPath = parent.toString();
      // Jsch implementation actually requires to CD first.
      sftpChannel.cd(linkParentDirPath);
      final String actualLinkName = path.getFileName().toString();

      if (isSymlink(remotePath)) {
        sftpChannel.rm(actualLinkName);
      }

      sftpChannel.symlink(linkPath, actualLinkName);

    } finally {
      sftpChannel.exit();
      sftpChannel.disconnect();
    }
  }

  @Override
  public final String readSymlink(final String remotePath) throws JSchException, SftpException {
    final ChannelSftp sftpChannel = (ChannelSftp) this.session.openChannel("sftp");
    sftpChannel.connect();

    String readlink = null;

    try {
      readlink = sftpChannel.readlink(remotePath);
    } finally {
      sftpChannel.exit();
      sftpChannel.disconnect();
    }

    return readlink;
  }

  @Override
  public String toString() {
    if (isConnected()) {
      return "SftpConnection (" + this.session.getUserName() + "@" + this.session.getHost() + ":" + this.session.getPort() + ")";
    } else {
      return "SftpConnection (disconneced)";
    }
  }

}
