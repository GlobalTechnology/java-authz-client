package org.ccci.gto.authorization.processor;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.ccci.gto.authorization.AuthzNamespaceContext;
import org.ccci.gto.authorization.Command;
import org.ccci.gto.authorization.Commands;
import org.ccci.gto.authorization.Processor;
import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.ProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class RpcProcessor implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(RpcProcessor.class);

    private final static Set<String> supportedProtocols = new HashSet<String>(Arrays.asList("http", "https"));

    private final URL authzUrl;
    private DocumentBuilder xmlDocumentBuilder;
    private XPath xpathEngine;

    public RpcProcessor(final String authzUri) throws MalformedURLException {
        this.authzUrl = new URL(authzUri);
        if (!supportedProtocols.contains(this.authzUrl.getProtocol())) {
            throw new MalformedURLException("Unsupported protocol: " + this.authzUrl.getProtocol());
        }
    }

    /**
     * @return the XmlDocumentBuilder
     * @throws ParserConfigurationException
     */
    public DocumentBuilder getXmlDocumentBuilder() throws ParserConfigurationException {
        // create an XML DocumentBuilder if one isn't already defined
        if (this.xmlDocumentBuilder == null) {
            final DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
            xmlFactory.setNamespaceAware(true);
            this.xmlDocumentBuilder = xmlFactory.newDocumentBuilder();
        }

        return this.xmlDocumentBuilder;
    }

    /**
     * @return the xpathEngine
     */
    public XPath getXpathEngine() {
        // create the Xpath engine for use with the XML parser if one doesn't
        // exist yet
        if (this.xpathEngine == null) {
            final XPathFactory xpathFactory = XPathFactory.newInstance();
            this.xpathEngine = xpathFactory.newXPath();
            this.xpathEngine.setNamespaceContext(AuthzNamespaceContext.INSTANCE);
        }

        return this.xpathEngine;
    }

    public List<Response<? extends Command>> process(final Commands commands) throws ProcessingException {
        // get the commands and generate a responses ArrayList
        final List<Command> cmds = commands.getCommands();
        final ArrayList<Response<? extends Command>> responses = new ArrayList<Response<? extends Command>>(cmds.size());

        // wrap processing in a try block to catch any of the various exceptions
        // that could be thrown and wrap them in a ProcessingException
        try {
            // create xml serializer
            final Transformer serializer = TransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, (LOG.isDebugEnabled() ? "yes" : "no"));

            // generate xml for the specified commands object
            final DocumentBuilder documentBuilder = this.getXmlDocumentBuilder();
            final Document requestDom = documentBuilder.newDocument();
            requestDom.appendChild(commands.toXml(requestDom));

            // dump request xml
            if (LOG.isDebugEnabled()) {
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                serializer.transform(new DOMSource(requestDom), new StreamResult(baos));
                LOG.debug(baos.toString());
            }

            // issue authz request
            final HttpURLConnection conn = (HttpURLConnection) authzUrl.openConnection();
            conn.setDoOutput(true);
            final OutputStream out = conn.getOutputStream();
            serializer.transform(new DOMSource(requestDom), new StreamResult(out));
            out.flush();
            out.close();

            // parse the authz response
            final Document responseDom = documentBuilder.parse(conn.getInputStream());

            // dump response xml if debug is enabled
            if (LOG.isDebugEnabled()) {
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                serializer.transform(new DOMSource(responseDom), new StreamResult(baos));
                LOG.debug(baos.toString());
            }

            // extract all commands from the response XML
            final XPath xpathEngine = this.getXpathEngine();
            final NodeList commandsNL = (NodeList) xpathEngine.evaluate("/authz:commands/authz:command", responseDom,
                    XPathConstants.NODESET);

            // process all found commands in the response XML
            for (int x = 0; x < commandsNL.getLength(); x++) {
                // generate response object for the current command
                responses.add(cmds.get(x).newResponse((Element) commandsNL.item(x), xpathEngine));
            }
        } catch (final Exception e) {
            // wrap the thrown exception in a generic processing exception
            throw new ProcessingException(e);
        }

        // throw an error if the wrong number of responses were returned
        if (cmds.size() != responses.size()) {
            throw new ProcessingException("Wrong number of responses returned");
        }

        // return the found responses
        return responses;
    }

    /**
     * @param documentBuilder
     *            the XmlDocumentBuilder to use for processing authorization
     *            commands
     */
    public void setXmlDocumentBuilder(final DocumentBuilder documentBuilder) {
        this.xmlDocumentBuilder = documentBuilder;
    }

    /**
     * @param xpathEngine
     *            the xpathEngine to set
     */
    public void setXpathEngine(final XPath xpathEngine) {
        this.xpathEngine = xpathEngine;
    }
}
