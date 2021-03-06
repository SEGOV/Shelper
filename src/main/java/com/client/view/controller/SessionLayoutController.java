package com.client.view.controller;

import com.MainApp;
import com.client.ClientEntryPoint;
import com.client.alert.SessionAlert;
import com.server.util.PropertiesReader;
import com.server.util.ViewDisableUpdater;
import com.server.model.ssh.LogInfo;
import com.server.model.ssh.Session;
import com.server.service.LogInfoService;
import com.server.service.SessionService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import static com.server.Constants.Session.PORT;
import static com.server.Constants.Session.SFTP_FILE_PROTOCOL;

public class SessionLayoutController extends SessionController {
    private MainApp mainApp;
    private ViewDisableUpdater viewDisableUpdater;

    @FXML
    private TableView<Session> sessionTable;
    @FXML
    private TableColumn<Session, String> hostNameColumn;
    @FXML
    private TableColumn<Session, String> userNameColumn;
    @FXML
    private Label fileProtocolLabel;
    @FXML
    private Label hostNameLabel;
    @FXML
    private Label portNumberLabel;
    @FXML
    private Label userNameLabel;

    @FXML
    private void initialize() {
        viewDisableUpdater = ViewDisableUpdater.getInstance();

        hostNameColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getHostName()));
        userNameColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getUserName()));

        showSessionDetails(null);
        sessionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showSessionDetails(newValue));

        sessionTable.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    handleSessionFunctions();
                }
            }
        });
    }

    private void handleSessionFunctions() {
        viewDisableUpdater.setDisable(sessionTable, Boolean.TRUE);

        Session selectedSession = sessionTable.getSelectionModel().getSelectedItem();
        if (Objects.nonNull(selectedSession)) {
            ClientEntryPoint clientEntryPoint = new ClientEntryPoint();
            boolean okClicked = clientEntryPoint.showSessionFunctionDialog(selectedSession);
            if (okClicked) {
                showSessionDetails(selectedSession);
                sessionTable.refresh();
            }
        } else {
            SessionAlert.getInstance().showNoSessionSelectedAlert(mainApp);
        }
        viewDisableUpdater.setDisable(sessionTable, Boolean.FALSE);
    }


    @FXML
    private void handleDeleteSession() {
        int selectedIndex = sessionTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex >= 0) {
            Session selectedSession = sessionTable.getSelectionModel().getSelectedItem();
            Integer sessionId = selectedSession.getId();
            SessionService.getInstance().removeSessionById(sessionId);
            sessionTable.getItems().remove(selectedIndex);
        } else {
            SessionAlert.getInstance().showNoSessionSelectedAlert(mainApp);
        }
    }

    @FXML
    private void handleEditSession() {
        viewDisableUpdater.setDisable(sessionTable, Boolean.TRUE);

        Session selectedSession = sessionTable.getSelectionModel().getSelectedItem();
        if (selectedSession != null) {
            ClientEntryPoint clientEntryPoint = new ClientEntryPoint();
            boolean okClicked = clientEntryPoint.showPersonEditDialog(selectedSession);
            if (okClicked) {
                showSessionDetails(selectedSession);
                sessionTable.refresh();
            }
        } else {
            SessionAlert.getInstance().showNoSessionSelectedAlert(mainApp);
        }
        viewDisableUpdater.setDisable(sessionTable, Boolean.FALSE);
    }

    @FXML
    private void handleNewSession() {
        viewDisableUpdater.setDisable(sessionTable, Boolean.TRUE);

        Session session = new Session();
        session.setFileProtocol(SFTP_FILE_PROTOCOL);
        session.setPortNumber(PORT);

        LogInfoService logInfoService = LogInfoService.getInstance();
        logInfoService.createLogInfoTable();
        PropertiesReader propertiesReader = PropertiesReader.getInstance();
        String login = propertiesReader.getPropertyValue("LOGIN");
        String password = propertiesReader.getPropertyValue("PASSWORD");
        logInfoService.createRow(login, password);
        LogInfo logInfo = logInfoService.getById(1);
        session.setUserName(logInfo.getLogin());
        session.setPassword(logInfo.getPassword());

        ClientEntryPoint clientEntryPoint = new ClientEntryPoint();
        boolean okClicked = clientEntryPoint.showPersonNewDialog(session);
        if (okClicked) {
            mainApp.getSessionData().add(session);
            sessionTable.getSelectionModel().select(session);
        }
        viewDisableUpdater.setDisable(sessionTable, Boolean.FALSE);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        ObservableList<Session> sessionData = mainApp.getSessionData();
        sessionTable.setItems(sessionData);
    }

    private void showSessionDetails(Session session) {
        if (Objects.nonNull(session)) {
            fileProtocolLabel.setText(session.getFileProtocol());
            hostNameLabel.setText(session.getHostName());
            portNumberLabel.setText(String.valueOf(session.getPortNumber()));
            userNameLabel.setText(session.getUserName());
        } else {
            fileProtocolLabel.setText(StringUtils.EMPTY);
            hostNameLabel.setText(StringUtils.EMPTY);
            portNumberLabel.setText(StringUtils.EMPTY);
            userNameLabel.setText(StringUtils.EMPTY);
        }
    }
}
