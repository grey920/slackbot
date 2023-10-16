package com.bolt.sample;

import com.slack.api.bolt.App;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class SlackAppService {

   private final App app;

    private final Logger log = LoggerFactory.getLogger( SlackAppService.class );


    /**
     * https://slack.dev/java-slack-sdk/guides/events-api
     * @param text
     * @param channel
     * @param user
     */
    /*public  appMentionResponse(String text, String channel, String user ) {

        app.event( AppMentionEvent.class, ( payload, ctx ) -> {

            AppMentionEvent event = payload.getEvent();

            if ( event.get)

        } );
    }*/
    public String slachCommandResponse(String command, String channel, String user) {
        log.info("[SlackAppService] slachCommandResponse called >>> {}, {}, {} ", command, channel, user);
        log.info("[SlackAppService] app >>> {}", app );

        String returnText = "";


        /*
        app.command(command, (req, ctx) -> {
            log.info("[SlackAppConfig] app.command called >>> {} ", command);
            log.info("[SlackAppConfig] app.command called >>> {} ", req.getPayload().getUserId() );
            log.info("[SlackAppConfig] app.command called >>> {} ", req.getPayload().getChannelId() );
//                   SlashCommandPayload payload = req.getPayload(); // 사용할 일이 없어서 주석
            if ("/checkin".equals(command)) {
                returnText.set(":wave: 안녕하세요! <@" + user + "> 님이 출근했습니다. 현재 채널명 #" + channel + " 입니다.");
            }
            else {
                returnText.set(":wave: 수고하셨어요! <@" + user + "> 님이 퇴근했습니다. 현재 채널명 #" + channel + " 입니다.");
            }

            return ctx.ack(returnText.get());
        });*/

            log.info("[SlackAppConfig] app.command called >>> {} ", command);
            if ("/checkin".equals(command)) {
                returnText = ":wave: 안녕하세요! <@" + user + "> 님이 출근했습니다. 현재 채널명 #" + channel + " 입니다.";
            }
            else {
                returnText = ":wave: 수고하셨어요! <@" + user + "> 님이 퇴근했습니다. 현재 채널명 #" + channel + " 입니다.";
            }

            return returnText;
    }

}
