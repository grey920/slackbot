package com.bolt.sample.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * https://techblog.lotteon.com/slack-bot-%EC%83%81%ED%98%B8-%EC%9E%91%EC%9A%A9-66596c262616
 */
@Getter
public class SlackJson {
    private String TYPE_URL_VERIFICATION = "url_verification";
    private String TYPE_EVENT_CALLBACK = "event_callback";

    private Map<String, String> event = null;
    private String eventType = "N";
    private String user;
    private String text;
    private String channel;
     private String json;
     private Map<String, String> result = new HashMap<>();

     public SlackJson( Map<String, Object> data ) {
         String type = String.valueOf( data.get( "type" ) );

         // url 검증 -> 단순 인증용이므로 challenge 값만 그대로 리턴
         if ( TYPE_URL_VERIFICATION.equals( type ) ) {
             result.put( "challenge", String.valueOf( data.get( "challenge" ) ) );
         }
         else {
             // 이벤트 타입 -> event 객체안에 있는 값들을 파싱
             Object item = data.get( "event" );
             if ( item != null && TYPE_EVENT_CALLBACK.equals( type ) ) {
                 this.event = (Map<String, String>) item;
                 this.eventType = event.get( "type" );
                 this.user = event.get( "user" );
                 this.text = event.get( "text" );
                 this.channel = event.get( "channel" );

                 result.put( "channel", this.channel );
             }
         }

     }

     public void setResultText( String reponseText ) {
            result.put( "text", reponseText );
     }

     public void setJson() throws JsonProcessingException {
         ObjectMapper mapper = new ObjectMapper();
         this.json = mapper.writeValueAsString( result);
     }


}
