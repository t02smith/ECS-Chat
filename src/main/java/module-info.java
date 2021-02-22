@SuppressWarnings("all")
module uk.ac.soton.comp1206 {
    requires java.scripting;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.apache.logging.log4j;
    requires nv.websocket.client;
    exports uk.ac.soton.comp1206;
    exports uk.ac.soton.comp1206.Network;
    exports uk.ac.soton.comp1206.UI;
    exports uk.ac.soton.comp1206.Utility;
    exports uk.ac.soton.comp1206.UI.Components;
    exports uk.ac.soton.comp1206.UI.Components.wbComponents;
    exports uk.ac.soton.comp1206.Utility.Listeners;
    exports uk.ac.soton.comp1206.Utility.Listeners.TopBar;
}