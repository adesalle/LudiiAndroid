package androidUtils.imageio.metadata;

import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IIOMetadataNode implements Element {
    private final String nodeName;
    private String nodeValue;
    private IIOMetadataNode parent;
    private final List<IIOMetadataNode> children = new ArrayList<>();
    private final Map<String, String> attributes = new HashMap<>();
    private Object userObject;

    public IIOMetadataNode() {
        this("");
    }

    public IIOMetadataNode(String nodeName) {
        this.nodeName = nodeName;
    }

    // Méthodes de base
    @Override
    public String getNodeName() {
        return nodeName;
    }

    @Override
    public String getNodeValue() throws DOMException {
        return nodeValue;
    }

    @Override
    public void setNodeValue(String nodeValue) throws DOMException {
        this.nodeValue = nodeValue;
    }

    @Override
    public short getNodeType() {
        return Node.ELEMENT_NODE;
    }

    // Méthodes de hiérarchie
    @Override
    public Node getParentNode() {
        return parent;
    }

    @Override
    public NodeList getChildNodes() {
        return new NodeList() {
            @Override
            public Node item(int index) {
                return children.get(index);
            }

            @Override
            public int getLength() {
                return children.size();
            }
        };
    }

    @Override
    public Node getFirstChild() {
        return children.isEmpty() ? null : children.get(0);
    }

    @Override
    public Node getLastChild() {
        return children.isEmpty() ? null : children.get(children.size() - 1);
    }

    @Override
    public Node getPreviousSibling() {
        if (parent == null) return null;
        int index = parent.children.indexOf(this);
        return index > 0 ? parent.children.get(index - 1) : null;
    }

    @Override
    public Node getNextSibling() {
        if (parent == null) return null;
        int index = parent.children.indexOf(this);
        return index < parent.children.size() - 1 ? parent.children.get(index + 1) : null;
    }

    // Méthodes de manipulation
    @Override
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        IIOMetadataNode ref = (IIOMetadataNode) refChild;
        int index = ref != null ? children.indexOf(ref) : children.size();
        children.add(index, (IIOMetadataNode) newChild);
        ((IIOMetadataNode) newChild).parent = this;
        return newChild;
    }

    @Override
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        int index = children.indexOf(oldChild);
        children.set(index, (IIOMetadataNode) newChild);
        ((IIOMetadataNode) newChild).parent = this;
        ((IIOMetadataNode) oldChild).parent = null;
        return oldChild;
    }

    @Override
    public Node removeChild(Node oldChild) throws DOMException {
        children.remove(oldChild);
        ((IIOMetadataNode) oldChild).parent = null;
        return oldChild;
    }

    @Override
    public Node appendChild(Node newChild) throws DOMException {
        children.add((IIOMetadataNode) newChild);
        ((IIOMetadataNode) newChild).parent = this;
        return newChild;
    }

    @Override
    public boolean hasChildNodes() {
        return !children.isEmpty();
    }

    @Override
    public Node cloneNode(boolean deep) {
        IIOMetadataNode clone = new IIOMetadataNode(nodeName);
        clone.nodeValue = nodeValue;
        clone.attributes.putAll(attributes);

        if (deep) {
            for (IIOMetadataNode child : children) {
                clone.appendChild(child.cloneNode(true));
            }
        }
        return clone;
    }

    // Méthodes d'attribut
    @Override
    public NamedNodeMap getAttributes() {
        return new NamedNodeMap() {
            @Override
            public Node getNamedItem(String name) {
                return createAttrNode(name);
            }

            @Override
            public Node setNamedItem(Node arg) throws DOMException {
                attributes.put(arg.getNodeName(), arg.getNodeValue());
                return arg;
            }

            @Override
            public Node removeNamedItem(String name) throws DOMException {
                return createAttrNode(name);
            }

            @Override
            public Node item(int index) {
                String name = (String) attributes.keySet().toArray()[index];
                return createAttrNode(name);
            }

            @Override
            public int getLength() {
                return attributes.size();
            }

            @Override
            public Node getNamedItemNS(String namespaceURI, String localName) throws DOMException {
                return getNamedItem(localName);
            }

            @Override
            public Node setNamedItemNS(Node arg) throws DOMException {
                return setNamedItem(arg);
            }

            @Override
            public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
                return removeNamedItem(localName);
            }
        };
    }

    private Node createAttrNode(String name) {
        if (!attributes.containsKey(name)) return null;
        return new Attr() {
            @Override
            public String getNodeName() {
                return "";
            }

            @Override
            public String getNodeValue() throws DOMException {
                return "";
            }

            @Override
            public void setNodeValue(String nodeValue) throws DOMException {

            }

            @Override
            public short getNodeType() {
                return 0;
            }

            @Override
            public Node getParentNode() {
                return null;
            }

            @Override
            public NodeList getChildNodes() {
                return null;
            }

            @Override
            public Node getFirstChild() {
                return null;
            }

            @Override
            public Node getLastChild() {
                return null;
            }

            @Override
            public Node getPreviousSibling() {
                return null;
            }

            @Override
            public Node getNextSibling() {
                return null;
            }

            @Override
            public NamedNodeMap getAttributes() {
                return null;
            }

            @Override
            public Document getOwnerDocument() {
                return null;
            }

            @Override
            public Node insertBefore(Node newChild, Node refChild) throws DOMException {
                return null;
            }

            @Override
            public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
                return null;
            }

            @Override
            public Node removeChild(Node oldChild) throws DOMException {
                return null;
            }

            @Override
            public Node appendChild(Node newChild) throws DOMException {
                return null;
            }

            @Override
            public boolean hasChildNodes() {
                return false;
            }

            @Override
            public Node cloneNode(boolean deep) {
                return null;
            }

            @Override
            public void normalize() {

            }

            @Override
            public boolean isSupported(String feature, String version) {
                return false;
            }

            @Override
            public String getNamespaceURI() {
                return "";
            }

            @Override
            public String getPrefix() {
                return "";
            }

            @Override
            public void setPrefix(String prefix) throws DOMException {

            }

            @Override
            public String getLocalName() {
                return "";
            }

            @Override
            public boolean hasAttributes() {
                return false;
            }

            @Override
            public String getBaseURI() {
                return "";
            }

            @Override
            public short compareDocumentPosition(Node other) throws DOMException {
                return 0;
            }

            @Override
            public String getTextContent() throws DOMException {
                return "";
            }

            @Override
            public void setTextContent(String textContent) throws DOMException {

            }

            @Override
            public boolean isSameNode(Node other) {
                return false;
            }

            @Override
            public String lookupPrefix(String namespaceURI) {
                return "";
            }

            @Override
            public boolean isDefaultNamespace(String namespaceURI) {
                return false;
            }

            @Override
            public String lookupNamespaceURI(String prefix) {
                return "";
            }

            @Override
            public boolean isEqualNode(Node arg) {
                return false;
            }

            @Override
            public Object getFeature(String feature, String version) {
                return null;
            }

            @Override
            public Object setUserData(String key, Object data, UserDataHandler handler) {
                return null;
            }

            @Override
            public Object getUserData(String key) {
                return null;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public boolean getSpecified() {
                return true;
            }

            @Override
            public String getValue() {
                return attributes.get(name);
            }

            @Override
            public void setValue(String value) throws DOMException {
                attributes.put(name, value);
            }

            @Override
            public Element getOwnerElement() {
                return null;
            }

            @Override
            public TypeInfo getSchemaTypeInfo() {
                return null;
            }

            @Override
            public boolean isId() {
                return false;
            }


        };
    }

    @Override
    public String getTagName() {
        return nodeName;
    }

    @Override
    public String getAttribute(String name) {
        return attributes.getOrDefault(name, "");
    }

    @Override
    public void setAttribute(String name, String value) throws DOMException {
        attributes.put(name, value);
    }

    @Override
    public void removeAttribute(String name) throws DOMException {
        attributes.remove(name);
    }

    @Override
    public Attr getAttributeNode(String name) {
        return (Attr) getAttributes().getNamedItem(name);
    }

    @Override
    public Attr setAttributeNode(Attr newAttr) throws DOMException {
        return (Attr) getAttributes().setNamedItem(newAttr);
    }

    @Override
    public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        return (Attr) getAttributes().removeNamedItem(oldAttr.getName());
    }

    @Override
    public NodeList getElementsByTagName(String name) {
        List<Node> nodes = new ArrayList<>();
        collectElementsByName(name, nodes);
        return createNodeList(nodes);
    }

    private void collectElementsByName(String name, List<Node> nodes) {
        if (nodeName.equals(name)) {
            nodes.add(this);
        }
        for (IIOMetadataNode child : children) {
            child.collectElementsByName(name, nodes);
        }
    }

    // Méthodes NS (simplifiées)
    @Override
    public String getAttributeNS(String namespaceURI, String localName) throws DOMException {
        return getAttribute(localName);
    }

    @Override
    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
        setAttribute(qualifiedName, value);
    }

    @Override
    public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
        removeAttribute(localName);
    }

    @Override
    public Attr getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
        return getAttributeNode(localName);
    }

    @Override
    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
        return setAttributeNode(newAttr);
    }

    @Override
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) throws DOMException {
        return getElementsByTagName(localName);
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    @Override
    public boolean hasAttributeNS(String namespaceURI, String localName) throws DOMException {
        return hasAttribute(localName);
    }

    // Méthodes utilitaires
    private NodeList createNodeList(List<Node> nodes) {
        return new NodeList() {
            @Override
            public Node item(int index) {
                return nodes.get(index);
            }

            @Override
            public int getLength() {
                return nodes.size();
            }
        };
    }

    // Méthodes non implémentées (retour null/false par défaut)
    @Override public Document getOwnerDocument() { return null; }
    @Override public void normalize() {}
    @Override public boolean isSupported(String feature, String version) { return false; }
    @Override public String getNamespaceURI() { return null; }
    @Override public String getPrefix() { return null; }
    @Override public void setPrefix(String prefix) throws DOMException {}
    @Override public String getLocalName() { return nodeName; }
    @Override public boolean hasAttributes() { return !attributes.isEmpty(); }
    @Override public String getBaseURI() { return null; }
    @Override public short compareDocumentPosition(Node other) throws DOMException { return 0; }
    @Override public String getTextContent() throws DOMException { return nodeValue; }
    @Override public void setTextContent(String textContent) throws DOMException { nodeValue = textContent; }
    @Override public boolean isSameNode(Node other) { return this == other; }
    @Override public String lookupPrefix(String namespaceURI) { return null; }
    @Override public boolean isDefaultNamespace(String namespaceURI) { return false; }
    @Override public String lookupNamespaceURI(String prefix) { return null; }
    @Override public boolean isEqualNode(Node arg) { return false; }
    @Override public Object getFeature(String feature, String version) { return null; }
    @Override public Object setUserData(String key, Object data, UserDataHandler handler) { return null; }
    @Override public Object getUserData(String key) { return null; }
    @Override public TypeInfo getSchemaTypeInfo() { return null; }
    @Override public void setIdAttribute(String name, boolean isId) throws DOMException {}
    @Override public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {}
    @Override public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {}

    // Méthodes spécifiques à IIOMetadataNode
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    public Object getUserObject() {
        return userObject;
    }

    public int getLength() {
        return children.size();
    }

    public Node item(int i) {
        return children.get(i);
    }
}