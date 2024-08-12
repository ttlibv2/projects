package vn.conyeu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@EnableWebSecurity
//@EnableMethodSecurity
@SpringBootApplication
public class TsWeb {

    public static void main(String[] args) {
        SpringApplication.run(TsWeb.class, args);
    }

}