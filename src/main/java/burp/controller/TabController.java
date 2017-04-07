package burp.controller;

import burp.IHttpRequestResponse;
import burp.IHttpService;
import burp.httpclient.HttpClient;
import burp.httpclient.HttpClientAdapter;
import burp.httpclient.HttpClientHandler;
import burp.message.BurpRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.CharsetUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLException;
import java.net.URISyntaxException;
import java.util.List;


/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/1/17.
 */
public class TabController implements HttpClientAdapter {
    private static final Logger logger = LogManager.getLogger();
    private Double currentProgress = 0.0;
    private HttpClient httpClient;
    private BurpRequestMessage burpRequestMessage;

    @FXML
    private TextArea txtRequest;

    @FXML
    private TextField txtExpected;

    @FXML
    private Button buttonStart;

    @FXML
    private Button buttonCancel;

    @FXML
    private TableView<?> tableResult;

    @FXML
    private TableColumn<?, ?> columnTime;

    @FXML
    private TableColumn<?, ?> columnResult;

    @FXML
    private Label lblElapsed;

    @FXML
    void btnCancel(MouseEvent event) {

    }

    @FXML
    void btnStart(MouseEvent event) {
        if (txtRequest.getText().length() == 0 || txtExpected.getText().length() == 0 || burpRequestMessage == null) {
            logger.info("Session can not start before providing a request and an expected string");
            return;
        }

        IHttpRequestResponse request = burpRequestMessage.getHttpRequest();
        IHttpService service = request.getHttpService();
        logger.debug("Starting session testing {} {} {}", service.getHost(), service.getPort(),
                service.getProtocol());

        try {
            httpClient = new HttpClient("test");
            httpClient.sendRequest(burpRequestMessage, new HttpClientHandler(this));

            buttonCancel.setDisable(false);
            buttonStart.setDisable(true);
        } catch (URISyntaxException|SSLException e) {
            logger.error("Error opening connection {}", e.getMessage(), e);
            reset();
        }
    }

    @SuppressWarnings("unused")
    @FXML
    void initialize() {
        logger.debug("Loading tab new tab");
        buttonCancel.setDisable(true);
        buttonCancel.setCancelButton(true);
    }

    public void setBurpRequestMessage(BurpRequestMessage burpRequestMessage) {
        byte[] b = burpRequestMessage.getHttpRequest().getRequest();
        String msg = new String(b);
        this.burpRequestMessage = burpRequestMessage;
        this.txtRequest.setText(msg);
        logger.debug("Logging msg {}", msg);
    }

    @Override
    public void onDataRead(HttpResponse httpResponse, List<HttpContent> httpContentList) {
        if (httpResponse == null || httpContentList == null) {
            logger.error("Invalid response");
            return;
        }

        ByteBuf buf = UnpooledByteBufAllocator.DEFAULT.buffer();
        for (HttpContent httpContent: httpContentList)
            logger.debug("Content: {}", httpContent.content().toString(CharsetUtil.UTF_8));
    }

    @Override
    public void onError(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Error connecting {}", cause.getMessage(), cause);

        reset();
    }

    private void reset() {
        buttonCancel.setDisable(true);
        buttonCancel.setCancelButton(true);
    }
}
