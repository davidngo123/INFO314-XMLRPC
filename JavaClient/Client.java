import java.io.*;
import java.net.*;
import java.net.http.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;

import org.w3c.dom.Node;

/**
 * This approach uses the java.net.http.HttpClient classes, which
 * were introduced in Java11.
 */
public class Client {
    private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    public static void main(String... args) throws Exception {
        System.out.println(add(1, 2, 3, 4, 5));
//        System.out.println(add() == 0);
//        System.out.println(add(1, 2, 3, 4, 5) == 15);
//        System.out.println(add(2, 4) == 6);
//        System.out.println(subtract(12, 6) == 6);
//        System.out.println(multiply(3, 4) == 12);
//        System.out.println(multiply(1, 2, 3, 4, 5) == 120);
//        System.out.println(divide(10, 5) == 2);
//        System.out.println(modulo(10, 5) == 0);
    }

    public static int add(int lhs, int rhs) throws Exception {
        String xml = buildXml("add", lhs, rhs);
        return -1;
    }
    public static int add(Integer... params) throws Exception {
        String xml = buildXml("add", (Object[]) params;
        return -1;
    }
    public static int subtract(int lhs, int rhs) throws Exception {
        return -1;
    }
    public static int multiply(int lhs, int rhs) throws Exception {
        return -1;
    }
    public static int multiply(Integer... params) throws Exception {
        return -1;
    }
    public static int divide(int lhs, int rhs) throws Exception {
        return -1;
    }
    public static int modulo(int lhs, int rhs) throws Exception {
        return -1;
    }




    private static String buildXml(String methodName, Object... params) {
//        StringBuilder xmlBuilder = new StringBuilder("<?xml version=\"1.0\"?>");
//        xmlBuilder.append("<methodCall>");
//        xmlBuilder.append("<methodName>").append(methodName).append("</methodName>").;
//        xmlBuilder.append("<params>");
//        for(Object param: params){
//            xmlBuilder.append("<param><value><i4>").append(param).append("</i4></value></param>");
//        }
//        xmlBuilder.append("</params>");
//        xmlBuilder.append("</methodCall>>");
//        xmlBuilder.toString();
//        System.out.println(xmlBuilder);
//        return xmlBuilder;

        DocumentBuilder docBuilder = dbf.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element methodCall= doc.createElement("methodCall");
        doc.appendChild(methodCall);

        doc.createElement("methodName");
        rootElement.appendChild(doc.createElement("staff"));

        return toString(doc);
    }
}
