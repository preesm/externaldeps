package org.ietr.externaldeps.sftp_maven_plugin;

import java.util.List;

public interface ISftpTransfertLayer {
  public void connectTo(final String host, final int port, final String user, final String password, final boolean strict) throws Exception;

  public void disconnect();

  public boolean isConnected();

  public void send(final String localFilePath, final String remoteFilePath) throws Exception;

  public void receive(final String remoteFilePath, final String localFilePath) throws Exception;

  public void mkdir(final String remoteDirPath) throws Exception;

  public void mkdirs(final String remoteDirPath) throws Exception;

  /**
   * Returns false if the path points to a symlink
   */
  public boolean isDirectory(final String dirPath) throws Exception;

  public List<String> ls(final String remoteDirPath) throws Exception;

  public boolean isSymlink(final String remotePath) throws Exception;

  public String readSymlink(final String remotePath) throws Exception;

  public void writeSymlink(final String remotePath, final String linkPath) throws Exception;

}
