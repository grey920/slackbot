package com.bolt.sample;

import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackOAuthAppServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet({"/slack/install", "/slack/oauth_redirect"})
public class SlackAppOAuthController extends SlackOAuthAppServlet {
    public SlackAppOAuthController(App app) {
        super(app);
    }
}
