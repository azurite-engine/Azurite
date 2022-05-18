package io.xml;

import util.Assets;

/**
 * Takes file path and creates a XMLElement.
 */
public class XML {

    private XMLTokenizer tokenizer = new XMLTokenizer();
    private XMLParser parser = new XMLParser();

    protected XMLElement root;

    private String xml;

    public XML (String path) {
        this.xml = Assets.getDataFile(path);
        root = parser.parse(tokenizer.tokenize(xml));
    }

    public XMLElement getRoot () {
        return this.root;
    }

    public String getXml () {
        return this.xml;
    }
}
