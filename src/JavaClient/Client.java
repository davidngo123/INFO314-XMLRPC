import java.io.*;
import java.net.*;
import java.net.http.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;

import org.w3c.dom.Node;


import java.io.*;
import java.net.*;
import java.net.http.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * This approach uses the java.net.http.HttpClient classes, which
 * were introduced in Java11.
 */
public class Client {
    private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    public static void main(String... args) throws Exception {
        System.out.println(add() == 0);
        System.out.println(add(1, 2, 3, 4, 5) == 15);
        System.out.println(add(2, 4) == 6);
        System.out.println(subtract(12, 6) == 6);
        System.out.println(multiply(3, 4) == 12);
        System.out.println(multiply(1, 2, 3, 4, 5) == 120);
        System.out.println(divide(10, 5) == 2);
        System.out.println(modulo(10, 5) == 0);
    }

    public static int add(int lhs, int rhs) throws Exception {
        String xml = buildXml("add", lhs, rhs);
        String body = send(xml);
        String answer = readBody(body);

        return parseInt(answer);
    }
    public static int add(Integer... params) throws Exception {
        String xml = buildXml("add", (Object[]) params);
        String body = send(xml);
        String answer = readBody(body);

        return parseInt(answer);
    }
    public static int subtract(int lhs, int rhs) throws Exception {
        String xml = buildXml("subtract", lhs, rhs);
        String body = send(xml);
        String answer = readBody(body);

        return parseInt(answer);
    }
    public static int multiply(int lhs, int rhs) throws Exception {
        String xml = buildXml("multiply", lhs, rhs);
        String body = send(xml);
        String answer = readBody(body);
        return parseInt(answer);
    }
    public static int multiply(Integer... params) throws Exception {
        String xml = buildXml("multiply", (Object[]) params);
        String body = send(xml);
        String answer = readBody(body);
        return parseInt(answer);
    }
    public static int divide(int lhs, int rhs) throws Exception {
        String xml = buildXml("divide", lhs, rhs);
        String body = send(xml);
        String answer = readBody(body);
        return parseInt(answer);
    }
    public static int modulo(int lhs, int rhs) throws Exception {
        String body = send(xml);
        String answer = readBody(body);
        return parseInt(answer);
    }


    private static String send(String xml){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + host + ":" + port))
                .POST(HttpRequest.BodyPublishers.ofString(xml))
                .header("Content-Type", "text/xml")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
        String body = response.body();

        return body;
    }

    private static String readBody(String xml) throws Exception {
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlRecords));
        Document doc = db.parse(is);


        NodeList error = doc.getElementsByTagName("fault");

        if (faultNode.getLength() != 0) {
            NodeList i4 = doc.getElementsByTagName("i4");
            NodeList errorString = doc.getElementsByTagName("string");
            String errorCode3 = i4.item(0).getTextContent().trim();
            String faultString = errorCode3.item(0).getTextContent().trim();

            throw new Exception("Error Code: " + code + ", Fault: " + str);
        }

        NodeList value = doc.getElementsByTagName("value");
        return value.item(0).getTextContent().trim();
    }

    private static String buildXml(String methodName, Object... args) throws Exception {
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element methodCall= doc.createElement("methodCall");
        doc.appendChild(methodCall);

        Element methodNameEle = doc.createElement("methodName");
        methodCall.appendChild(methodNameEle);
        methodNameEle.setTextContent(methodName);

        Element params = doc.createElement("params");
        methodNameEle.appendChild(params);
        for (Object arg : args) {
            Element param = doc.createElement("param");
            params.appendChild(param);
            Element value = doc.createElement("value");
            params.appendChild(value);
            Element i4 = doc.createElement("i4");
            i4.setTextContent(arg.toString());
            value.appendChild(i4);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        return writer.toString();
    }
}
