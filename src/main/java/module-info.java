module whitman.cs370proj.project3startercode {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;

    opens whitman.cs370proj.composer to javafx.fxml;
    opens whitman.cs370proj.composer.Models to com.google.gson;

    exports whitman.cs370proj.composer.Interfaces;
    exports whitman.cs370proj.composer.Models to com.google.gson;
    exports whitman.cs370proj.composer;
    exports whitman.cs370proj.composer.Helpers;
    exports whitman.cs370proj.composer.Controllers;
    opens whitman.cs370proj.composer.Controllers to javafx.fxml;
    opens whitman.cs370proj.composer.Helpers to javafx.fxml;
}