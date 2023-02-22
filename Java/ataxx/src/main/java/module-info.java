module com.ataxx {
    requires transitive javafx.controls;
    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires transitive javafx.media;
    requires transitive javafx.fxml;
    requires transitive com.google.gson;

    opens com.gui to javafx.fxml;
    opens com.shared;
    exports com.gui;
    exports com.shared;
}
