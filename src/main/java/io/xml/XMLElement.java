package io.xml;

import java.util.*;

/**
 * @version 09.12.2021
 * @since 09.12.2021
 */
public class XMLElement {

    private static final String COMMENT_TAG = "0";

    private final Map<String, String> attributes;
    private final String tag;
    private final List<XMLElement> subElements;
    private String value;

    public XMLElement(String tag, Map<String, String> attributes, List<XMLElement> subElements) {
        this.attributes = attributes;
        this.tag = tag;
        this.subElements = subElements;
        this.value = null;
    }

    public XMLElement(String tag, List<XMLElement> subElements) {
        this(tag, new HashMap<>(), subElements);
    }

    public XMLElement(String tag, Map<String, String> attributes, String value) {
        this.attributes = attributes;
        this.subElements = new ArrayList<>();
        this.tag = tag;
        this.value = value;
    }

    public XMLElement(String tag, String value) {
        this(tag, new HashMap<>(), value);
    }

    public XMLElement(String comment) {
        this.attributes = null;
        this.subElements = null;
        this.value = comment;
        this.tag = COMMENT_TAG;
    }

    public List<XMLElement> getChildren() {
        return this.subElements == null ? null : Collections.unmodifiableList(subElements);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getTag() {
        return tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (subElements.size() != 0)
            throw new XMLSyntaxException("There can be no value, if an element already has children.");
        this.value = value;
    }

    public void addAttribute(String name, String value) {
        this.attributes.put(name, value);
    }

    public void addSubElement(XMLElement element) {
        if (value != null) throw new XMLSyntaxException("This element has already a value applied to it.");
        this.subElements.add(element);
    }

    public String toString(boolean fancy) {
        return toString(fancy, "");
    }

    private String toString(boolean fancy, String layer) {
        StringBuilder builder = new StringBuilder();
        if (tag.equals(COMMENT_TAG)) {
            //comment element
            builder.append(layer).append("<!--").append(value).append("-->");
            return builder.toString();
        }
        builder.append(layer).append('<').append(tag);
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            builder.append(' ').append(entry.getKey()).append('=').append('"').append(XML.transformValue(entry.getValue(), false)).append('"');
        }
        if (subElements.size() == 0 && value == null) {
            builder.append("/").append('>');
            return builder.toString();
        }
        builder.append('>');
        if (fancy && subElements.size() > 0) builder.append('\n');
        for (XMLElement element : subElements) {
            builder.append(element.toString(fancy, layer + "\t"));
            if (fancy) builder.append('\n');
        }
        if (value != null) {
            builder.append(XML.transformValue(value, false));
            if (value.contains("\n"))
                builder.append(layer);
        }
        builder.append('<').append('/').append(tag).append(">");
        return builder.toString();
    }

    @Override
    public String toString() {
        return toString(false);
    }
}