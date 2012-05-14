package org.ccci.gto.authorization;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

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

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.ccci.gto.authorization.exception.ProcessingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class RpcProcessor implements Processor {
    private final String authzUri;
    private DocumentBuilder xmlDocumentBuilder;
    private HttpClient httpClient;
    private XPath xpathEngine;

    public RpcProcessor(final String authzUri) {
	this.authzUri = authzUri;
    }

    /**
     * @return the httpClient
     */
    public HttpClient getHttpClient() {
	// Create HTTP client if client doesn't exist yet
	if (this.httpClient == null) {
	    final HttpParams params = new BasicHttpParams();
	    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	    HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	    HttpProtocolParams.setUseExpectContinue(params, true);
	    ConnManagerParams.setMaxTotalConnections(params, 100);
	    final SchemeRegistry registry = new SchemeRegistry();
	    registry.register(new Scheme("http", PlainSocketFactory
		    .getSocketFactory(), 80));
	    registry.register(new Scheme("https", SSLSocketFactory
		    .getSocketFactory(), 443));
	    final ClientConnectionManager cm = new ThreadSafeClientConnManager(
		    params, registry);
	    this.httpClient = new DefaultHttpClient(cm, params);
	}

	return this.httpClient;
    }

    /**
     * @return the XmlDocumentBuilder
     * @throws ParserConfigurationException
     */
    public DocumentBuilder getXmlDocumentBuilder()
	    throws ParserConfigurationException {
	// create an XML DocumentBuilder if one isn't already defined
	if (this.xmlDocumentBuilder == null) {
	    final DocumentBuilderFactory xmlFactory = DocumentBuilderFactory
		    .newInstance();
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
	    this.xpathEngine
		    .setNamespaceContext(AuthzNamespaceContext.INSTANCE);
	}

	return this.xpathEngine;
    }

    public List<Response> process(final Commands commands) throws ProcessingException {
	// get the commands and generate a responses ArrayList
	final List<Command> cmds = commands.getCommands();
	final ArrayList<Response> responses = new ArrayList<Response>(cmds
		.size());

	// wrap processing in a try block to catch any of the various exceptions
	// that could be thrown and wrap them in a ProcessingException
	try {
	    // get the various objects used in processing the specified commands
	    final DocumentBuilder documentBuilder = this
		    .getXmlDocumentBuilder();
	    final HttpClient httpClient = this.getHttpClient();
	    final XPath xpathEngine = this.getXpathEngine();

	    // generate xml for the specified commands object
	    final Document requestDom = documentBuilder.newDocument();
	    requestDom.appendChild(commands.toXml(requestDom));

	    // generate a byte array for the xml document
	    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    final Transformer serializer = TransformerFactory.newInstance().newTransformer();
	    serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    serializer.transform(new DOMSource(requestDom), new StreamResult(
		    baos));

	    // generate POST request
	    final HttpPost request = new HttpPost(this.authzUri);
	    final AbstractHttpEntity requestEntity = new ByteArrayEntity(baos
		    .toByteArray());
	    requestEntity.setContentType("application/gcxauthz+xml");
	    request.setEntity(requestEntity);

	    // send the POST request
	    final HttpResponse response = httpClient.execute(request);

	    // parse the XML response to an XML Document object
	    final Document responseDom = documentBuilder.parse(response
		    .getEntity().getContent());

	    // extract all commands from the response XML
	    final NodeList commandsNL = (NodeList) xpathEngine.evaluate(
		    "/authz:commands/authz:command", responseDom,
		    XPathConstants.NODESET);

	    // process all found commands in the response XML
	    for (int x = 0; x < commandsNL.getLength(); x++) {
		// generate response object for the current command
		responses.add(cmds.get(x).newResponse(
			(Element) commandsNL.item(x), xpathEngine));
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
     * @param httpClient the httpClient to set
     */
    public void setHttpClient(final HttpClient httpClient) {
	this.httpClient = httpClient;
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
     * @param xpathEngine the xpathEngine to set
     */
    public void setXpathEngine(final XPath xpathEngine) {
	this.xpathEngine = xpathEngine;
    }

}
