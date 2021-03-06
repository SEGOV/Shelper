package com.server.model.ssh;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import static com.server.Constants.Session.*;

public class SSHManager {
    private static final SSHManager INSTANCE = new SSHManager();
    private String user;
    private String password;
    private String host;
    private int port;

    public static SSHManager getInstance() {
        return INSTANCE;
    }

    public ChannelSftp getSFTPChannel(String serverPath) throws JSchException {
        ChannelSftp sftpChannel = null;
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            sftpChannel = (ChannelSftp) session.openChannel(SFTP_FILE_PROTOCOL.toLowerCase());
            sftpChannel.connect(TIMEOUT);
            sftpChannel.cd(serverPath);
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return sftpChannel;
    }

    public void fetchSSHManager(com.server.model.ssh.Session session) {
        this.setHost(session.getHostName());
        this.setPort(session.getPortNumber());
        this.setUser(session.getUserName());
        this.setPassword(session.getPassword());
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
