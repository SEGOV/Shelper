package com.server;

import com.server.util.PropertiesReader;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public interface Icon {
        String PROJECT_ICON_IMAGE_PATH = "/icons/plus.PNG";
    }

    public interface Session {
        String SFTP_FILE_PROTOCOL = "SFTP";
        Integer PORT = 22;
        Integer TIMEOUT = 3000;
    }

    public interface Server {
        String SERVER_TOOLS_PATH = PropertiesReader.getInstance().getPropertyValue("SERVER_TOOLS_PATH");
        String SERVER_IMPL_LIB_PATH = PropertiesReader.getInstance().getPropertyValue("SERVER_IMPL_LIB_PATH");
        String SERVER_WEB_LIB_PATH = PropertiesReader.getInstance().getPropertyValue("SERVER_WEB_LIB_PATH");
        String SERVER_JSP_PATH = PropertiesReader.getInstance().getPropertyValue("SERVER_JSP_PATH");
    }

    public interface Boiler {
        String CLEAN_BOILER_COMMAND_NAME = "Clean Boiler";
        String CLEAN_BOILER_SCRIPT_NAME = "clean_boiler.sh";
        String CLEAN_BOILER_COMMAND = "sh " + CLEAN_BOILER_SCRIPT_NAME;
        String CLEAN_BOILER_SCRIPT = "scripts/clean_boiler.sh";
        String SCRIPT_MISSED_MESSAGE = "script missed on the program, clean boiler function is not supported";
    }

    public interface JSP {
        String JSP = "jsp/";
        String SHOW_CLASS = "RBMExecutorLite.jsp";
        String RBM_EXECUTOR = "RBMExecutorLite.jsp";
        String SHOW_GRANTS_EXTENDED_LIST = "showGrantsExtendedList.jsp";

        List<String> jspList = Arrays.asList(SHOW_CLASS, RBM_EXECUTOR, SHOW_GRANTS_EXTENDED_LIST);
    }

    public interface Message {
        String CRLF = "\n"; //Carriage Return and Line Feed
        String EMDASH = "---------------------------------------------";
        String NO_VALID_FILE_PROTOCOL = "No valid File Protocol!";
        String NO_VALID_HOST_NAME = "No valid Host Name!";
        String NO_VALID_PORT_NUMBER = "No valid Port Number!";
        String ONLY_DIGIT_VALID_FOR_PORT_NUMBER = "Only digit valid for Port Number!";
        String NO_VALID_USER_NAME = "No valid User Name!";
        String NO_VALID_PASSWORD = "No valid password!";

        String TO_EXECUTE_CONFIRM_QUESTION = "Are you sure to execute these functions? ";
        String DRAG_AND_DROP_CLASS_FILE = "-- Drug and Drop Class File --";
    }
}
