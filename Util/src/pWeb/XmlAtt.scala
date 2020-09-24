/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package pWeb

/** An XML attribute. */
trait XmlAtt
{ def name: String
  def valueStr: String
  def str: String = name + "=" + valueStr.enquote
}

/** An Xml numeric attribute. */
trait XANumeric extends XmlAtt
{ def value: Double
  override def valueStr: String = value.toString
}

object XANumeric
{
  def apply(nameIn: String, valueIn: Double): XANumeric = new XANumeric
  { override def name: String = nameIn
    override def value: Double = valueIn    
  }
}