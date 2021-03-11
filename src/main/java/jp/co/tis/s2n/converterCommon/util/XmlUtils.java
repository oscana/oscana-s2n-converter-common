package jp.co.tis.s2n.converterCommon.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jp.co.tis.s2n.converterCommon.log.LogUtils;

/**
 * XML操作ユーティリティ。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class XmlUtils {

    /**
     * DTDを無視してXMLだけを読み込む。
     * @param xmlFile 読み込むファイル
     * @return 結果
     * @throws ParserConfigurationException ParserConfigurationException例外
     * @throws SAXException SAXException例外
     * @throws IOException IOException例外
     */
    public static Document loadXMLFile(File xmlFile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);

            factory.setFeature("http://xml.org/sax/features/namespaces", false);
            factory.setFeature("http://xml.org/sax/features/validation", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(xmlFile);
            return document;

        } catch (Exception e) {
            LogUtils.warn(xmlFile.getAbsolutePath() + " - 読み込み中にエラーが発生しました。.");
            throw new RuntimeException(e);
        }

    }

    /**
     * DocumentからXPathにてNodeListを取得する
     * @param expression XPath式
     * @param doc Document対象
     * @return NodeList
     * @throws XPathExpressionException XPathExpressionException例外
     */
    public static NodeList findNodeListFromDocument(String expression, Document doc) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList ret;
        try {
            ret = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    /**
     * 指定したElementからXPathにてNodeListを取得する
     * @param expression XPath式
     * @param src パラメータ
     * @return NodeList
     * @throws XPathExpressionException XPathExpressionException例外
     */
    public static NodeList findNodeListFromElement(String expression, Element src) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList ret;
        try {
            ret = (NodeList) xpath.evaluate(expression, src, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    /**
     * 指定したElementからXPathにてStringを取得する。
     * @param expression XPath式
     * @param src コンテキストソース
     * @return 処理結果
     */
    public static String findStringFromElement(String expression, Element src) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        String ret;
        try {
            ret = (String) xpath.evaluate(expression, src, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    /**
     * 指定したElementからXPathにてElementを取得する
     * @param expression XPath式
     * @param src パラメータ
     * @return Element
     * @throws XPathExpressionException XPathExpressionException例外
     */
    public static Element findElementFromElement(String expression, Element src) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        Element ret;
        try {
            ret = (Element) xpath.evaluate(expression, src, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    /**
     * DocumentからXPathにてElementを取得する
     * @param expression XPath式
     * @param doc Document対象
     * @return Element
     * @throws XPathExpressionException XPathExpressionException例外
     */
    public static Element findElementFromDocument(String expression, Document doc) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        Element ret;
        try {
            ret = (Element) xpath.evaluate(expression, doc, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    /**
     * XMLを文字列に変換する。
     * @param elm パラメータ
     * @return 変換後の文字列
     */
    public static String xmlToString(Element elm) {
        try {
            TransformerFactory tfFactory = TransformerFactory.newInstance();
            Transformer tf = tfFactory.newTransformer();

            tf.setOutputProperty("indent", "yes");
            tf.setOutputProperty("encoding", "UTF-8");

            StringWriter sw = new StringWriter();
            tf.transform(new DOMSource(elm), new StreamResult(sw));
            sw.flush();
            return sw.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
