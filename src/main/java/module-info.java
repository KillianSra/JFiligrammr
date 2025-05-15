module io.github.killiansra.jfiligrammr {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.pdfbox;
    requires javafx.swing;

    opens io.github.killiansra.jfiligrammr to javafx.fxml;
    exports io.github.killiansra.jfiligrammr;
    exports io.github.killiansra.jfiligrammr.controller;
}