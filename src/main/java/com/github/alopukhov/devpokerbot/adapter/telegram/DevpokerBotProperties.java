package com.github.alopukhov.devpokerbot.adapter.telegram;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions.ProxyType;

@Component
@ConfigurationProperties(prefix = "bot")
@Data
public class DevpokerBotProperties {
    private String name;
    private String token;
    private ProxyConfig proxy = new ProxyConfig();
    private boolean start = true;

    @Data
    public static class ProxyConfig {
        private boolean enabled = false;
        private String host;
        private int port;
        private ProxyType type = ProxyType.SOCKS5;
    }
}