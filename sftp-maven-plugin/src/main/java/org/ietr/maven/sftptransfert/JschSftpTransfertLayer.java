package org.ietr.maven.sftptransfert;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JschSftpTransfertLayer implements ISftpTransfertLayer {

  private static final ExecutorService threadPool = Executors.newFixedThreadPool(4);

  private boolean     connected       = false;
  private Session     session         = null;
  private ChannelSftp mainSftpChannel = null;

  private JschSftpTransfertLayer() {
  }

  public static final JschSftpTransfertLayer connect(final String host, final int port, final String user, final String password,
      final boolean strictHostKeyChecking) {
    final JschSftpTransfertLayer jschSftpConnection = new JschSftpTransfertLayer();
    jschSftpConnection.connectTo(host, port, user, password, strictHostKeyChecking);
    return jschSftpConnection;
  }

  @Override
  public final void connectTo(final String host, final int port, final String user, final String password, final boolean strictHostKeyChecking) {
    try {
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

      this.mainSftpChannel = (ChannelSftp) this.session.openChannel("sftp");
      if (this.mainSftpChannel == null) {
        throw new JSchException("Could not create channel", new NullPointerException());
      }
      this.mainSftpChannel.connect();
    } catch (final JSchException e) {
      throw new org.ietr.maven.sftptransfert.SftpException("Could not connect", e);
    }
  }

  @Override
  public final void disconnect() {
    JschSftpTransfertLayer.threadPool.shutdown();
    try {
      JschSftpTransfertLayer.threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new org.ietr.maven.sftptransfert.SftpException("Awaiting termination failed.", e);
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
  public final boolean isDirectory(final String remoteDirPath) {
    boolean res;
    try {

      if (isSymlink(remoteDirPath)) {
        res = false;
      } else {
        // do not use this.ls
        @SuppressWarnings("unchecked")
        final List<LsEntry> ls = new ArrayList<LsEntry>(this.mainSftpChannel.ls(remoteDirPath));
        // ls should list . and .. at least if it is a directory. If it is a file, it will list the filename itself only
        final int size = ls.size();
        res = size > 1;
      }
    } catch (final SftpException e) {
      res = false;
    }
    return res;
  }

  @Override
  public final boolean isSymlink(final String remotePath) {
    boolean res;
    try {
      this.mainSftpChannel.readlink(remotePath);
      res = true;
    } catch (final SftpException e) {
      res = false;
    }
    return res;
  }

  @Override
  public final List<String> ls(final String remoteDirPath) {
    final List<String> res = new ArrayList<>();

    try {
      @SuppressWarnings("unchecked")
      final List<LsEntry> ls = new ArrayList<LsEntry>(this.mainSftpChannel.ls(remoteDirPath));
      for (final LsEntry fileEntry : ls) {
        final String filename = fileEntry.getFilename();
        if (".".equals(filename) || "..".equals(filename) || filename.startsWith(".")) {
          continue;
        }
        res.add(remoteDirPath + "/" + filename);
      }
    } catch (final SftpException e) {
      throw new org.ietr.maven.sftptransfert.SftpException("Could not make dir", e);
    }

    return res;
  }

  @Override
  public final void mkdir(final String remoteDirPath) {
    try {
      this.mainSftpChannel.mkdir(remoteDirPath);
    } catch (final SftpException e) {
      throw new org.ietr.maven.sftptransfert.SftpException("Could not make dir", e);
    }
  }

  @Override
  public final void mkdirs(final String remoteDirPath) {
    final Path remoteDestinationDir = Paths.get(remoteDirPath);
    final Deque<String> parents = new ArrayDeque<>();
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
  public final String readSymlink(final String remotePath) {
    String readlink;
    try {
      readlink = this.mainSftpChannel.readlink(remotePath);
    } catch (final SftpException e) {
      throw new org.ietr.maven.sftptransfert.SftpException("Could not read link", e);
    }
    return readlink;
  }

  @Override
  public final void receive(final String remoteFilePath, final String localFilePath) {
    normalReceive(remoteFilePath, localFilePath);
  }

  final void normalReceive(final String remoteFilePath, final String localFilePath) {
    try {

      this.mainSftpChannel.get(remoteFilePath, localFilePath);

    } catch (final SftpException e) {
      throw new org.ietr.maven.sftptransfert.SftpException("Receive failed : " + e.getMessage(), e);
    }

  }

  final void threadedReceive(final String remoteFilePath, final String localFilePath) {
    JschSftpTransfertLayer.threadPool.execute(() -> {
      ChannelSftp sftpChannel = null;
      try {
        sftpChannel = (ChannelSftp) this.session.openChannel("sftp");
        if (sftpChannel == null) {
          throw new JSchException("Could not create channel", new NullPointerException());
        }
        sftpChannel.connect();

        sftpChannel.get(remoteFilePath, localFilePath);

      } catch (JSchException | SftpException e) {
        throw new org.ietr.maven.sftptransfert.SftpException("Receive failed : " + e.getMessage(), e);
      } finally {
        sftpChannel.exit();
        sftpChannel.disconnect();
      }
    });
  }

  @Override
  public final void send(final String localFilePath, final String remoteFilePath) {
    threadedSend(localFilePath, remoteFilePath);
  }

  final void normalSend(final String localFilePath, final String remoteFilePath) {
    try {

      this.mainSftpChannel.put(localFilePath, remoteFilePath);

    } catch (final SftpException e) {
      throw new org.ietr.maven.sftptransfert.SftpException("Send failed : " + e.getMessage(), e);
    }

  }

  final void threadedSend(final String localFilePath, final String remoteFilePath) {
    JschSftpTransfertLayer.threadPool.execute(() -> {
      ChannelSftp sftpChannel = null;
      try {
        sftpChannel = (ChannelSftp) this.session.openChannel("sftp");
        sftpChannel.connect();

        sftpChannel.put(localFilePath, remoteFilePath);

      } catch (JSchException | SftpException e) {
        throw new org.ietr.maven.sftptransfert.SftpException("Send failed : " + e.getMessage(), e);
      } finally {
        sftpChannel.exit();
        sftpChannel.disconnect();
      }
    });
  }

  @Override
  public String toString() {
    if (isConnected()) {
      return "SftpConnection (" + this.session.getUserName() + "@" + this.session.getHost() + ":" + this.session.getPort() + ")";
    } else {
      return "SftpConnection (disconneced)";
    }
  }

  @Override
  public final void writeSymlink(final String remotePath, final String linkPath) {
    try {
      final Path path = Paths.get(remotePath);
      final Path parent = path.getParent();
      final String linkParentDirPath = parent.toString();
      // Jsch implementation actually requires to CD first.
      this.mainSftpChannel.cd(linkParentDirPath);
      final String actualLinkName = path.getFileName().toString();

      if (isSymlink(remotePath)) {
        this.mainSftpChannel.rm(actualLinkName);
      }

      this.mainSftpChannel.symlink(linkPath, actualLinkName);

    } catch (final SftpException e) {
      throw new org.ietr.maven.sftptransfert.SftpException("Could not write link", e);
    }
  }

}
