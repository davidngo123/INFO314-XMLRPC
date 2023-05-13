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


class Call {
    public String name;
    public List<Object> args = new ArrayList<Object>();

    public Call(String setName, List<Objects> setArgs){
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

    public static void main(String[] args) {
        LOG.info("Starting up on port 8080");
        port(8080);
        before((request, response) -> {
            if (!request.requestMethod().equals("POST")) {
=               halt(405, "Only POST Request allowed");
            }
        });

        notFound((request, response) -> {
            halt(404, "URL not found");
        });

        // This is the mapping for POST requests to "/RPC";
        // this is where you will want to handle incoming XML-RPC requests
        post("/RPC", (request, response) -> {
            response.status(200);
            response.type("text/xml");


            Call call = extractXMl(request.body());
            int[] args = int[call.getArgs.size()];
            for(int i = 0; i < call.getArgs.size(); i++){
                args[i] = (int) call.getArgs.get(i);
            }
            Calc calc = new Calc();

            String xmlResponse = "";
            if(call.getName().equals("add")) {
                int answer = add(args);
                xmlResponse = buildXml(answer);

            } else if (call.getName.equals("subtract")) {
                int answer = subtract(args[0], args[1]);
                xmlResponse = buildXml(answer);

            } else if (call.getName.equals("multiply")) {
                int answer = multiply(args);
                xmlResponse = buildXml(answer);

            } else if (call.getName.equals("divide")) {
                if(args[1] == 0) {
                    xmlResponse = buildFault("1", "Divide by Zero");
                } else {
                    int answer = divide(args[0], args[1]);
                    xmlResponse = buildXml(answer);
                }

            } else if (call.getName.equals("modulo")) {
                if(args[1] == 0) {
                    xmlResponse = buildFault("1", "Divide by Zero");
                } else {
                    int answer = modulo(args[0], args[1]);
                    xmlResponse = buildXml(answer);
                }

            }
            return xmlResponse;


        });

        // Each of the verbs has a similar format: get() for GET,
        // put() for PUT, delete() for DELETE. There's also an exception()
        // for dealing with exceptions thrown from handlers.
        // All of this is documented on the SparkJava website (https://sparkjava.com/).
    }

    private static Call extractXML(String xml) {
        String method = "";
        List<Object> items = new ArrayList<>();

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
