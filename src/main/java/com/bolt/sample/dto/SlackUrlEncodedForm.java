package com.bolt.sample.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

/**
 * https://techblog.lotteon.com/slack-bot-%EC%83%81%ED%98%B8-%EC%9E%91%EC%9A%A9-66596c262616
 */
@Getter
public class SlackUrlEncodedForm {

    private final String channelId;
    private final String userId;
    private final String userName;
    private final String command;
    private final String text;

    private String json;
    private Map<String, String> result = new HashMap<>();

    /**
     * 키의 중복을 허용하도록 MultiValueMap 사용
     * @param data
     */
    public SlackUrlEncodedForm( MultiValueMap<String, String> data ) {
        this.channelId = String.valueOf( data.getFirst( "channel_id" ) );
        this.userId = String.valueOf( data.getFirst( "user_id" ) );
        this.userName = String.valueOf( data.getFirst( "user_name" ) );
        this.command = String.valueOf( data.getFirst( "command" ) );
        this.text = String.valueOf( data.getFirst( "text" ) );
    }

    public void setResultText( String reponseText ) {
        result.put( "response_type", "in_channel" );
        result.put( "text", reponseText );
    }

    public void setJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.json = mapper.writeValueAsString( result );
    }
}
