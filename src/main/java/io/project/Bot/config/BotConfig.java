package io.project.Bot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

import java.nio.charset.StandardCharsets;

@Configuration
@Data
@PropertySource("file:./config")
public class BotConfig {
    //PropertySourcesPlaceholderConfigurer p = propertySourcesPlaceholderConfigurer();
    @Value("${bot.name}")
    String botName;
    @Value("${bot.token}")
    String token;
    @Value("${girls.path}")
    String girlsPath;
    @Value("${man.path}")
    String manPath;
    @Value("${captionGirl.path}")
    String captionGirlPath;
    @Value("${captionMan.path}")
    String captionManPath;
    @Value("${photo.path}")
    String photoPath;
    @Value("${max.photo.value}")
    int maxPhotoVal;

//    public void init (){
//        photoPath = new String(getPhotoPath().getBytes(StandardCharsets.ISO_8859_1));
//    }



//    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
//        PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
//        properties.setLocation(new FileSystemResource("./config.properties"));
//        properties.setIgnoreResourceNotFound(false);
//        return properties;
//    }
}
