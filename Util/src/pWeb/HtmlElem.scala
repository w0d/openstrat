/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package pWeb

/** HTML element. */
trait HtmlElem extends XmlishElem

case class HtmlPage(head: HtmlHead, body: HtmlBody)
{ def out: String = "<!doctype html>\n" + HtmlHtml(head, body).out(0, 0, 150)
}

object HtmlPage
{ /** Creates an HTML page object form the title String and the HTML body contents String. */
  def title(title: String, bodyContent: String): HtmlPage = HtmlPage(HtmlHead(title), HtmlBody(bodyContent))
}

case class HtmlHead(titleStr: String, attribs: Arr[XmlAtt] = Arr()) extends HtmlElem
{ override def tag: String = "head"
  override def content: Arr[XCon] = Arr[XCon](HtmlTitle(titleStr))
  def out(indent: Int, linePosn: Int, lineLen: Int): String = openTag1 + content.toStrsFold("\n", _.out(0, 0, 150)) + "\n" + closeTag
}

case class HtmlTitle(str: String) extends HtmlElem
{
  def tag = "title"

  override def attribs: Arr[XmlAtt] = Arr()

  override def content: Arr[XCon] = ???
  override def out(indent: Int, linePosn: Int, lineLen: Int): String = "<title>" + str + "</title>"
}

/** The "html" HTML element */
case class HtmlHtml(head: HtmlHead, body: HtmlBody, attribs: Arr[XmlAtt] = Arr()) extends HtmlElem
{ def tag: String = "html"
  override def content = Arr(head, body)
  def out(indent: Int, linePosn: Int, lineLen: Int): String = openTag2 + head.out(0, 0, 150) + "\n\n" + body.out(0, 0, 150) + n2CloseTag
}

case class HtmlBody(contentStr: String) extends HtmlElem
{ override def tag: String = "body"
  override def content: Arr[XCon] = Arr(contentStr.xCon)
  def out(indent: Int, linePosn: Int, lineLen: Int): String = openTag1 + content.toStrsFold("\n", _.out(0, 0, 150)) + n1CloseTag
  override def attribs: Arr[XmlAtt] = Arr()
  //def out: String = "<body>\n" + content + "\n</body>"
}