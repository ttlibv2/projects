package vn.conyeu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.util.ResourceUtils;
import vn.conyeu.commons.beans.ObjectMap;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EnableWebSecurity
@EnableMethodSecurity
@SpringBootApplication
@Slf4j
public class TsWeb {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(TsWeb.class, args);




    }

}