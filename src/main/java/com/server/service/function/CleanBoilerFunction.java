package com.server.service.function;

import com.client.view.controller.SessionFunctionController;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.server.model.ssh.SSHManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.server.Constants.Boiler.*;
import static com.server.Constants.Server.SERVER_HOME_PATH;

public class CleanBoilerFunction implements Function {
    private SessionFunctionController sessionFunctionController;

    @Override
    public void execute(SessionFunctionController sessionFunctionController) {
        this.sessionFunctionController = sessionFunctionController;
        SSHManager sshManager = SSHManager.getInstance();
        com.server.model.ssh.Session session = sessionFunctionController.getSession();
        sshManager.fetchSSHManager(session);

        uploadScriptIfNotExist();
        executeCleanBoilerScript(sessionFunctionController);
    }

    private void uploadScriptIfNotExist() {
        ChannelSftp sftpChannel;
        File script;
        try {
            sftpChannel = SSHManager.getInstance().getSFTPChannel(SERVER_HOME_PATH);
            if (isScriptExist(sftpChannel)) {
                return;
            }
            ClassLoader classLoader = CleanBoilerFunction.class.getClassLoader();
            URL resource = classLoader.getResource(CLEAN_BOILER_SCRIPT_PATH);
            if (Objects.isNull(resource)) {
                sessionFunctionController.consoleAppendText("clean_boiler.sh script missed on the program, clean boiler function is not supported");
                new RuntimeException(CLEAN_BOILER_SCRIPT_NAME + " script missed on the program, clean boiler function is not supported", new Throwable());
            }
            script = new File(resource.getFile());
            sftpChannel.put(new FileInputStream(script), script.getName());
        } catch (JSchException | SftpException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isScriptExist(ChannelSftp sftpChannel) {
        boolean isExist = true;
        try {
            sftpChannel.lstat(CLEAN_BOILER_SCRIPT_NAME);
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                isExist = false;
            }
        }
        if (isExist) {
            sessionFunctionController.consoleAppendText("File " + CLEAN_BOILER_SCRIPT_NAME + " exist on the server.");
        } else {
            sessionFunctionController.consoleAppendText("File " + CLEAN_BOILER_SCRIPT_NAME + " is not exist on the server!");
        }
        return isExist;
    }

    private void executeCleanBoilerScript(SessionFunctionController sessionFunctionController) {
        List<String> commands = new ArrayList<>();
        commands.add("cd " + SERVER_HOME_PATH);
        commands.add(CLEAN_BOILER_COMMAND);

        ScriptShExecutor scriptShExecutor = new ScriptShExecutor(sessionFunctionController);
        scriptShExecutor.executeCommands(CLEAN_BOILER_COMMAND_NAME, commands);
    }
}
