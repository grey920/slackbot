package com.bolt.sample;

import com.bolt.sample.dto.SlackJson;
import com.bolt.sample.dto.SlackUrlEncodedForm;
import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackAppServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class SlackAppController extends SlackAppServlet {
    private final Logger log = LoggerFactory.getLogger( SlackAppController.class );

    private final SlackAppService slackAppService;

    public SlackAppController(App app, SlackAppService slackAppService) {
        super(app);
        this.slackAppService = slackAppService;
    }

    /*public SlackAppController(App app) {
        super(app);
    }*/

    @RequestMapping(value = "/slack/events", produces = APPLICATION_JSON_VALUE )
    public ResponseEntity<String> index(@RequestBody MultiValueMap<String, String> data, HttpServletRequest request ) {
        Enumeration<String> headers = request.getHeaderNames();
        for ( Enumeration<String> e = headers; e.hasMoreElements(); ) {
            String headerName = e.nextElement();
            log.info( "[SlackAppController] headerName : {}, value : {}", headerName, request.getHeader( headerName ) );
        }


        log.info( "[SlackAppController] index called!!!!!!! ");

        try {
            log.info("[SlackAppController] slackJson EventType : {}", data );

            // 멘션 이벤트
//            SlackJson slackJson = new SlackJson( data );
//            if ("app_mention".equals( slackJson.getEventType() ) ) {
//                String responseText = slackAppService.appMentionResponse( slackJson.getText(), slackJson.getChannel(), slackJson.getUser() );//.getMessage().getText();
//                slackJson.setResultText(responseText);
//            }
            SlackUrlEncodedForm encodedForm = new SlackUrlEncodedForm( data );
            String responseText = slackAppService.slachCommandResponse( encodedForm.getCommand(), encodedForm.getChannelId(), encodedForm.getUserId() );

            encodedForm.setResultText( responseText );

            encodedForm.setJson();
            return new ResponseEntity<>(encodedForm.getJson(), org.springframework.http.HttpStatus.OK);
        }
        catch ( Exception e ) {
            log.error( e.getMessage() );
            return new ResponseEntity<>( e.getMessage(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }

}
