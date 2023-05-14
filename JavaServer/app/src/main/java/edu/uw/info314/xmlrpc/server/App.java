package edu.uw.info314.xmlrpc.server;

import java.util.*;
import java.util.logging.*;

import static spark.Spark.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


class Call {
    public String name;
    public List<Object> args = new ArrayList<Object>();

    public Call(String setName, List<Object> setArgs){
        name = setName;
        args = setArgs;
    }
    public String getName() {
        return name;
    }

    public List<Object> getArgs() {
        return args;
    }


}

public class App {
    public static final Logger LOG = Logger.getLogger(App.class.getCanonicalName());
    public static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    public static String xmlResponse;
    private static Set<String> acceptedPaths = new HashSet<>(Arrays.asList("/RPC"));
    private static Set<String> acceptedMethods = new HashSet<>(Arrays.asList("post"));

    public static void main(String[] args) {
        LOG.info("Starting up on port 8080");
        port(8080);
        before("/*", (request, response) -> {

            if (!request.requestMethod().contains("POST")) {
                halt(405, "Method not supported");
            }
        });

        notFound((request, response) -> {
            halt(404, "URL Not Found");
            return "404 Not Found";
        });
        // This is the mapping for POST requests to "/RPC";
        // this is where you will want to handle incoming XML-RPC requests
        post("/RPC", (request, response) -> {
            response.status(200);
            response.type("text/xml");


            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(request.body()));
            Document doc = db.parse(is);


            NodeList i4 = doc.getElementsByTagName("i4");
            if(doc.getElementsByTagName("i4").getLength() == 0) {
                return buildFault("3", "Illegal Argument Exception");
            }
            Call call = extractXMLRPCCall(request.body());
            int[] nums = new int [call.getArgs().size()];
            for(int i = 0; i < call.getArgs().size(); i++){
                nums[i] = (int) call.getArgs().get(i);
            }
            Calc calc = new Calc();


            if(call.getName().equals("add")) {
                int answer = -1;
                try {
                    answer = calc.add(nums);
                } catch (Exception e) {
                    throw new ArithmeticException("Overflow!");
                }
                xmlResponse = buildXml("add", answer);

            } else if (call.getName().equals("subtract")) {
                int answer = calc.subtract(nums[0], nums[1]);
                xmlResponse = buildXml("subtract", answer);

            } else if (call.getName().equals("multiply")) {
                int answer = -1;
                try {
                    answer = calc.add(nums);
                } catch (Exception e) {
                    throw new ArithmeticException("Overflow!");
                }
                xmlResponse = buildXml("multiply", answer);

            } else if (call.getName().equals("divide")) {
                if(nums[1] == 0) {
                    xmlResponse = buildFault("1", "Divide by Zero");
                } else {
                    int answer = calc.divide(nums[0], nums[1]);
                    xmlResponse = buildXml("divide", answer);
                }

            } else if (call.getName().equals("modulo")) {
                if(nums[1] == 0) {
                    xmlResponse = buildFault("1", "Divide by Zero");
                } else {
                    int answer = calc.modulo(nums[0], nums[1]);
                    xmlResponse = buildXml("modulo", answer);
                }

            }
            return xmlResponse;


        });

        // Each of the verbs has a similar format: get() for GET,
        // put() for PUT, delete() for DELETE. There's also an exception()
        // for dealing with exceptions thrown from handlers.
        // All of this is documented on the SparkJava website (https://sparkjava.com/).
    }

    public static Call extractXMLRPCCall(String xml) throws ParserConfigurationException, IOException, SAXException {
        String method = "";
        List<Object> items = new ArrayList<>();



        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
        Document doc = db.parse(is);

        method  = doc.getElementsByTagName("methodName").item(0).getTextContent();
        NodeList tagNodes = doc.getElementsByTagName("i4");

        for (int i = 0; i < tagNodes.getLength(); i++ ){
            items.add(Integer.parseInt(tagNodes.item(i).getTextContent()));
        }
        System.out.println(items);
        Call call = new Call(method, items);

        return call;

    }

    private static String buildFault(String faultCode, String error) throws Exception {
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element methodCall= doc.createElement("methodResponse");
        doc.appendChild(methodCall);
        Element params = doc.createElement("fault");
        Element param = doc.createElement("code");
        param.setTextContent(faultCode);
        params.appendChild(param);
        Element value = doc.createElement("error");
        param.setTextContent(error);
        params.appendChild(value);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        return writer.toString();
    }
    private static String buildXml(String methodName, int answer) throws Exception {
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element methodCall= doc.createElement("methodResponse");
        doc.appendChild(methodCall);
        Element params = doc.createElement("params");
        Element param = doc.createElement("param");
        params.appendChild(param);
        Element value = doc.createElement("value");
        params.appendChild(value);
        Element i4 = doc.createElement("i4");
        i4.setTextContent(Integer.toString(answer));
        value.appendChild(i4);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        return writer.toString();
    }


}
