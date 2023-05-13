import java.io.*;
import java.net.*;
import java.net.http.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.InputSource;

/**
 * This approach uses the java.net.http.HttpClient classes, which
 * were introduced in Java11.
 */
public class Client {
    private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private static String url;

    public static void main(String... args) throws Exception {
        String port = args[1];
        String host = args[0];
        url = "http://" + host + ":" + port;
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
        return readBody(body);
    }
    public static int add(Integer... params) throws Exception {
        String xml = buildXml("add", (Object[]) params);
        String body = send(xml);
        return readBody(body);
    }
    public static int subtract(int lhs, int rhs) throws Exception {
        String xml = buildXml("subtract", lhs, rhs);
        String body = send(xml);
        return readBody(body);
    }
    public static int multiply(int lhs, int rhs) throws Exception {
        String xml = buildXml("multiply", lhs, rhs);
        String body = send(xml);
        return readBody(body);
    }
    public static int multiply(Integer... params) throws Exception {
        String xml = buildXml("multiply", (Object[]) params);
        String body = send(xml);
        return readBody(body);
    }
    public static int divide(int lhs, int rhs) throws Exception {
        String xml = buildXml("divide", lhs, rhs);
        String body = send(xml);
        return readBody(body);
    }
    public static int modulo(int lhs, int rhs) throws Exception {
        String xml = buildXml("modulo", lhs, rhs);
        String body = send(xml);
        return readBody(body);
    }


    private static String send(String xml){

        String body = "";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString(xml))
                    .header("Content-Type", "text/xml")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            body = response.body();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    private static int readBody(String xml) throws Exception {
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
        Document doc = db.parse(is);


        NodeList error = doc.getElementsByTagName("fault");

        if (error.getLength() != 0) {
            NodeList codeElement = doc.getElementsByTagName("code");
            NodeList errorElement = doc.getElementsByTagName("error");
            String code = codeElement.item(0).getTextContent();
            String fault = errorElement.item(0).getTextContent();

            throw new Exception("Error Code: " + code.trim() + ", Fault: " + fault.trim());
        }

        NodeList value = doc.getElementsByTagName("value");
        String answer = value.item(0).getTextContent().trim();
        return Integer.parseInt(answer);
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
