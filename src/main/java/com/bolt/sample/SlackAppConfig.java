package com.bolt.sample;

import com.slack.api.app_backend.slash_commands.payload.SlashCommandPayload;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.model.event.AppHomeOpenedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.asElements;
import static com.slack.api.model.block.element.BlockElements.button;
import static com.slack.api.model.view.Views.view;

@Configuration
@PropertySource( "classpath:/application.properties" )
public class SlackAppConfig {

    private final Logger log = LoggerFactory.getLogger( SlackAppConfig.class );

    private final String token;
    private final String signingSecret;
    private final String clientSecret;
    private final String clientId;

    public SlackAppConfig(Environment env ) {
        this.token = env.getProperty("slack.bot.token" );
        this.signingSecret = env.getProperty("slack.bot.signingSecret" );
        this.clientId = env.getProperty("slack.bot.clientId" );
        this.clientSecret = env.getProperty("slack.bot.clientSecret" );
    }

    /**
     * 워크스페이스 하나만 구동할 경우
     * @return
     */
    @Bean
    public AppConfig loadSingleWorkspaceAppConfig() {
        log.info("[SlackAppConfig] loadSingleWorkspaceAppConfig called");
        return AppConfig.builder()
                .singleTeamBotToken( token )
                .signingSecret( signingSecret )
                .build();
    }

    /**
     * 여러개의 워크스페이스를 구동해야 할 경우
     * @return
     */
    public AppConfig loadOAuthConfig() {
        return AppConfig.builder()
                .singleTeamBotToken( null )
                .clientId( clientId )
                .clientSecret( clientSecret)
                .signingSecret( signingSecret )
                .scope( "app_mentions:read,channels:history,channels:read,chat:write" )
                .oauthInstallPath( "/slack/install" )
                .oauthRedirectUriPath( "/slack/oauth_redirect" )
                .build();

    }

    @Bean
    public App initSlackApp( AppConfig config ) {
        log.info("[SlackAppConfig] initSlackApp called");

        App app = new App(config);
        if ( config.getClientId() != null ){
            app.asOAuthApp( true );
        }

        // https://api.slack.com/start/building/bolt-java
        app.event( AppHomeOpenedEvent.class, (payload, ctx) -> {

            var appHomeView = view(view -> view
               .type("home")
               .blocks(asBlocks(
                 section(section -> section.text(markdownText(mt -> mt.text("*Welcome to your _App's Home_* :tada:")))),
                 divider(),
                 section(section -> section.text(markdownText(mt -> mt.text("This button won't do much for now but you can set up a listener for it using the `actions()` method and passing its unique `action_id`. See an example on <https://slack.dev/java-slack-sdk/guides/interactive-components|slack.dev/java-slack-sdk>.")))),
                 actions(actions -> actions
                   .elements(asElements(
                     button(b -> b.text(plainText(pt -> pt.text("Click me!"))).value("button1").actionId("button_1"))
                   ))
                 )
               ))
             );

             var res = ctx.client().viewsPublish(r -> r
               .userId(payload.getEvent().getUser())
               .view(appHomeView)
             );

             return ctx.ack();
        });

        /* /hello 로 명령어 호출 -> 응답 출력 */
        app.command( "/hello", ( req, ctx ) -> {
            log.info("[SlackAppConfig] app.command called >>> /hello ");
            SlashCommandPayload payload = req.getPayload();

            String commandArgText = payload.getText();
            String channelId = payload.getChannelId();
            String channelName = payload.getChannelName();
            String userId = payload.getUserId();

            String returnText = "<@" + userId + "> said " + commandArgText + " at <#" + channelId + "|" + channelName + ">";

            return ctx.ack( returnText );
        });

        /* /checkin 로 명령어 호출 -> 응답 출력 */
       /* app.command( "/checkin", ( req, ctx ) -> {
            log.info("[SlackAppConfig] app.command called >>> /checkin ");
            SlashCommandPayload payload = req.getPayload();

            String userId = payload.getUserId();

            String returnText = "<@" + userId + "> 님이 출근했습니다.";

            return ctx.ack( returnText );
        });*/




        return app;
    }
}
