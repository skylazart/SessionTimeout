package burp.controller;

import burp.IHttpRequestResponse;
import burp.IHttpService;
import burp.httpclient.HttpClient;
import burp.httpclient.HttpClientAdapter;
import burp.httpclient.HttpClientHandler;
import burp.message.BurpRequestMessage;
import burp.message.SessionTimeoutHttpMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.CharsetUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

import javax.net.ssl.SSLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/1/17.
 */
public class TabController implements HttpClientAdapter {
    private static final Logger logger = LogManager.getLogger();

    private HttpClient httpClient;

    private boolean testing = false;
    private final ObservableList<ResultData> tableResultData = FXCollections.observableArrayList();

    private DateTime begin;
    private DateTime lastTest;

    private List<Period> testingIntervals = Arrays.asList(
            new Period().withMinutes(1),
            new Period().withMinutes(6),
            new Period().withMinutes(12),
            new Period().withMinutes(17),
            new Period().withMinutes(22));

    private int currentInterval;

    private boolean requestSent;
    private boolean responseReceived;

    private Timeline singleSecond;
    private final Object lock = new Object();

    @FXML
    private TextArea txtRequest;

    @SuppressWarnings("unused")
    @FXML
    private TextField txtExpected;

    @FXML
    private TextField txtPort;

    @FXML
    private TextField txtHost;

    @FXML
    private CheckBox chkHttps;

    @FXML
    private Button buttonStart;

    @FXML
    private Button buttonCancel;

    @FXML
    private TableView<ResultData> tableResult;

    @FXML
    private TableColumn<ResultData, String> columnTime;

    @FXML
    private TableColumn<ResultData, String> columnResult;

    @FXML
    private Label lblElapsed;

    @FXML
    void btnCancel(@SuppressWarnings("unused") MouseEvent event) {
        testCancelled();
        singleSecond.stop();
    }

    @FXML
    void btnStart(@SuppressWarnings("unused") MouseEvent event) {
        if (txtRequest.getLength() == 0 || txtExpected.getLength() == 0 || txtPort.getLength() == 0) {
            return;
        }

        begin = new DateTime();
        lastTest = begin;
        currentInterval = 0;

        testStarted();
        sendRequest();
        updateElapsedTime(new Duration(begin, new DateTime()));

        if (singleSecond != null) {
            singleSecond.play();
        } else {
            singleSecond = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), event1 -> runScheduledTasks()));
            singleSecond.setCycleCount(Timeline.INDEFINITE);
            singleSecond.play();
        }
    }

    private void runScheduledTasks() {
        if (currentInterval >= testingIntervals.size()) {
            synchronized (lock) {
                if (!requestSent && responseReceived) {
                    testFinished();
                }
            }
            return;
        }

        Duration d = new Duration(begin, new DateTime());
        updateElapsedTime(d);
        DateTime dateTime = lastTest.plus(testingIntervals.get(currentInterval));
        if (dateTime.isEqualNow() || dateTime.isBeforeNow()) {
            sendRequest();
            currentInterval++;
        }
    }

    private String[] beauty = {".", "..", "..."};
    private int beautyIdx = 0;

    private void updateElapsedTime(Duration d) {
        String sb = String.valueOf(d.getStandardMinutes()) + " minute(s)" +
                " " + beauty[(beautyIdx++) % beauty.length];
        lblElapsed.setText(sb);
    }

    private void sendRequest() {
        try {
            if (this.httpClient == null)
                this.httpClient = HttpClient.getInstance();

            String protocol;
            if (chkHttps.isSelected()) {
                protocol = "HTTPS";
            } else {
                protocol = "HTTP";
            }

            String req = txtRequest.getText();
            req = req.replace("\r\n", "\n");
            req = req.replace("\n", "\r\n");
            req += "\r\n";

            SessionTimeoutHttpMessage sessionTimeoutHttpMessage = new SessionTimeoutHttpMessage(
                    req.getBytes(),
                    protocol,
                    txtHost.getText(),
                    Integer.valueOf(txtPort.getText()));

            httpClient.sendRequest(sessionTimeoutHttpMessage, new HttpClientHandler(this));
            lastTest = new DateTime();

            synchronized (lock) {
                requestSent = true;
                responseReceived = false;
            }
        } catch (SSLException e) {
            logger.error("Error opening connection {}", e.getMessage(), e);
            tableResultData.add(new ResultData("Error while connecting..."));
            testCancelled();
        }
    }

    @SuppressWarnings("unused")
    @FXML
    void initialize() {
        logger.debug("Loading tab new tab");
        buttonCancel.setDisable(true);
        buttonCancel.setCancelButton(true);

        columnTime.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getTime()));
        columnResult.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getMsg()));

        tableResult.setItems(tableResultData);
    }

    void setBurpRequestMessage(BurpRequestMessage burpRequestMessage) {
        byte[] b = burpRequestMessage.getHttpRequest().getRequest();

        String msg = new String(b);
        this.txtRequest.setText(msg);
        this.txtPort.setText(String.valueOf(burpRequestMessage.getHttpRequest().getPort()));
        this.txtHost.setText(burpRequestMessage.getHttpRequest().getHost());

        if (burpRequestMessage.getHttpRequest().getProtocol().compareToIgnoreCase("HTTPS") == 0)
            this.chkHttps.setSelected(true);

        logger.debug("Logging msg {}", msg);
    }

    @Override
    public void onDataRead(HttpResponse httpResponse, List<HttpContent> httpContentList) {
        if (!testing)
            return;

        if (httpResponse == null || httpContentList == null) {
            logger.error("Invalid response");
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append(httpResponse.status().codeAsText()).append(" ").append(httpResponse.status().reasonPhrase())
                .append("\n");

        HttpHeaders httpHeaders = httpResponse.headers();
        for (Map.Entry<String, String> h : httpHeaders) {
            sb.append(h.getKey()).append(": ").append(h.getValue()).append("\n");
        }

        for (HttpContent httpContent : httpContentList)
            sb.append(httpContent.content().toString(CharsetUtil.UTF_8));

        String strResponse = sb.toString();
        String responseNoLineBreaks = strResponse.replace('\n', ' ').replace("\r", "");
        String status = String.valueOf(httpResponse.status().code()) + " " + httpResponse.status().reasonPhrase();

        int idx = responseNoLineBreaks.indexOf(txtExpected.getText());
        if (idx > 0) {
            int beginIdx = Math.max(0, idx - 20);
            int endIdx = Math.min(responseNoLineBreaks.length(), idx + txtExpected.getLength() + 20);

            tableResultData.add(new ResultData("Session is still valid.", status,
                    responseNoLineBreaks.substring(beginIdx, endIdx)));
        } else {
            tableResultData.add(new ResultData("Session expired.", status, responseNoLineBreaks.substring(0, 20)));
            testFinished();
        }

        synchronized (lock) {
            responseReceived = true;
            requestSent = false;
        }
    }

    @Override
    public void onError(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Error {}", cause.getMessage(), cause);
        testCancelled();
    }

    @Override
    public void onErrorConnecting() {
        Platform.runLater(() -> {
            logger.error("Error connecting...");
            tableResultData.add(new ResultData("Error while connecting..."));
            testCancelled();
        });
    }

    private void testStarted() {
        testing = true;
        tableResultData.clear();
        buttonCancel.setDisable(false);
        buttonStart.setDisable(true);
        txtExpected.setEditable(false);
        txtRequest.setEditable(false);
        txtPort.setEditable(false);
        txtHost.setEditable(false);

        tableResultData.add(new ResultData("Starting the test..."));
    }

    private void testCancelled() {
        testing = false;
        buttonCancel.setDisable(true);
        buttonStart.setDisable(false);
        txtExpected.setEditable(true);
        txtRequest.setEditable(true);
        txtPort.setEditable(true);
        txtHost.setEditable(true);

        tableResultData.add(new ResultData("Test cancelled."));
        lblElapsed.setText("canceled");

        if (singleSecond != null)
            singleSecond.stop();
    }

    private void testFinished() {
        testing = false;
        buttonCancel.setDisable(true);
        buttonStart.setDisable(false);
        txtExpected.setEditable(true);
        txtRequest.setEditable(true);
        tableResultData.add(new ResultData("Test finished."));

        if (singleSecond != null)
            singleSecond.stop();
    }
}
