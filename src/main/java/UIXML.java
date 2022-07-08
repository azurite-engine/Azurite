package ui;

import io.xml.XML;
import io.xml.XMLElement;
import util.Logger;

public class UIXML extends XML {
    public UIXML(String path) {
        super(path);
        this.build();
    }

    private void build () {
        for (XMLElement i : this.root.getChildren()) {
            Logger.debugLog(i.getAttributes().toString());
        }
    }
}
