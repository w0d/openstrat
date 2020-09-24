/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package pWeb

/** An XML or an HTML element */
trait XmlishElem extends XCon
{ def tag: String
  def attribs: Arr[XmlAtt]
  def contents: Arr[XCon]
 
  def attribsOut: String = ife(attribs.empty, "", " " + attribs.toStrsFold(" ", _.str) + " ")
  def openAtts: String = "<" + tag + attribsOut 
  def openUnclosed: String = openAtts + ">"
 
  def closeTag: String = "</" + tag + ">"
  def n1CloseTag: String = "\n" + closeTag
  def n2CloseTag: String = "\n\n" + closeTag
  //def openVoid: String
}

/** An XML element. */
trait XmlElem extends XmlishElem
{
 // override def openVoid: String = openAtts + "/>"
  override def out(indent: Int = 0, linePosn: Int = 0, lineLen: Int = 150): String = if (contents.empty) openAtts + "/>"
    else openUnclosed.nli(indent + 2) + contents.toStrsFold("\n" + (indent + 2).spaces, _.out(indent + 2, 0, 150)).nli(indent) + closeTag
}

/** Content for XML and HTML. */
trait XCon
{ /** Returns the XML source code, formatted according to the input. */
  def out(indent: Int, linePosn: Int, lineLen: Int): String
}

/** XConStr is a wrapper to convert [[String]]s to XCon, XML Element content. */
case class XConStr(value: String) extends XCon
{ override def out(indent: Int, linePosn: Int, lineLen: Int): String = value
}

object XConStr
{ implicit def StringToXConStr(value: String): XConStr = new XConStr(value)
}