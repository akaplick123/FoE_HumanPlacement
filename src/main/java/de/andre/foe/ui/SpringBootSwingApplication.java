package de.andre.foe.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import de.andre.foe.ui.frame.MainFrame;

@SpringBootApplication
public class SpringBootSwingApplication implements CommandLineRunner {

  @Autowired
  private MainFrame mainFrame;

  public static void main(String[] args) throws Exception {
    // disabled banner, don't want to see the spring logo
    SpringApplication app = new SpringApplication(SpringBootSwingApplication.class);
    app.setBannerMode(Banner.Mode.OFF);
    app.setHeadless(false);
    app.run(args);
  }

  @Override
  public void run(String... args) throws Exception {
    mainFrame.createAndShowGUI();
  }
}
