/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat

/** Extension methods for String. Brought into scope by the stringToImplicit method in the package object. */
class StringImplicit(val thisString: String) extends AnyVal //extends PersistStr
{
  def parseTokens: ERefs[pParse.Token] = pParse.srcToETokens(thisString.toCharArray, "String")
  def parseStatements: ERefs[pParse.Statement] = parseTokens.flatMap(pParse.astParse(_))//.toOld
  //def asType[A](implicit ev: Persist[A]): EMon[A] = thisString.parseToStatements.flatMap(ev.fromStatements)

  /** Searches for Statement of type A. Can be a value of type A or a setting of a type A. */
  def findType[A: Persist]: EMon[A] = thisString.parseStatements.flatMap(_.findType[A])

  /** Finds Statement of type A. */
  def findTypeElse[A: Persist](elseValue: => A): A = findType[A].getElse(elseValue)
  def findInt: EMon[Int] = thisString.parseStatements.flatMap(_.findInt)
  def findDouble: EMon[Double] = thisString.parseStatements.flatMap(_.findDouble)
  def findBoolean: EMon[Boolean] = thisString.parseStatements.flatMap(_.findBoolean)
  def findTypeIndex[A: Persist](index: Int): EMon[A] = thisString.parseStatements.flatMap(_.findTypeIndex[A](index))
  def findTypeDo[A: Persist](f: A => Unit): Unit = findType[A].forGood(f)

  def asType[A](implicit ev: Persist[A]): EMon[A] = ???
    /*parseStatements.flatMap(sts => sts match
    { case Refs1(h) => ev.fromStatement(h).elseTry(ev.fromStatements(sts))
      case sts => ev.fromStatements(sts)
    })*/

  def asInt: EMon[Int] = asType[Int]

  def findIntArray: EMon[Array[Int]] = thisString.parseStatements.flatMap(_.findIntArray)

  def findSett[A: Persist](settingStr: String): EMon[A] = thisString.parseStatements.flatMap(_.findSett[A](settingStr))
  def findSettElse[A: Persist](settingStr: String, elseValue: A): A = findSett[A](settingStr).getElse(elseValue)
  def findIntSett(settingStr: String): EMon[Int] = thisString.parseStatements.flatMap(_.findIntSett(settingStr))
  def findIntSettElse(settingStr: String, elseValue: Int): Int = findIntSett(settingStr).getElse(elseValue)
  def findDoubleSett(settingStr: String): EMon[Double] = thisString.parseStatements.flatMap(_.findDoubleSett(settingStr))
  def findDoubleSettElse(settingStr: String, elseValue: Double): Double = findDoubleSett(settingStr).getElse(elseValue)
  def findBooleanSett(settingStr: String): EMon[Boolean] = thisString.parseStatements.flatMap(_.findBooleanSett(settingStr))
  def findBooleanSettElse(settingStr: String, elseValue: Boolean): Boolean = findBooleanSett(settingStr).getElse(elseValue)
  
  /** Concatenates a space and then the other String. */
  def -- (other: String): String = thisString + " " + other
  
  /** Concaternates a newline character and then the other [String]]. */
  def --- (other: String): String = thisString + "\n" + other

  /** Concaternates 2 newline characters and then the other [String]]. */
  def ---- (other: String): String = thisString + "\n\n" + other
  
  /** Concatenates a newline special character followed by spaces to this string. */
  def nli(indent: Int): String = thisString + "\n" + indent.spaces  
  
  /** prepends a newline special character and spaces to this string */
  def preNl(indent: Int): String = thisString + "\n" + indent.spaces
  
  /** Prepends a newline special character to this String */
  def preNl: String = "\n" + thisString
  
  /** Prepends 2 spaces to string */   
  def ind2: String = "  " + thisString
  
  /** Prepends 4 spaces to string */
  def ind4: String = "    " + thisString
  
  /** Concatenates a '/' character and then the other String. Useful for constructing directory/ folder paths on the Web, Linux and Unix */      
  def -/-(other: String): String = thisString + "/" + other
  
  def -:-(other: String): String = thisString + ": " + other  
  def optAppend (optionOther: Option[String]): String = optionOther.fold(thisString)(string2 => thisString + " " + string2)
  def enquote: String = "\"" + thisString + "\""
  def enquote1: String = "'" + thisString + "'"
  def addEnqu(s2: String): String = thisString + s2.enquote
  
  /** encloses string in parentheses */
  def enParenth: String = "(" + thisString + ")"
  
  /** encloses string in Square brackets */
  def enSquare: String = "[" + thisString + "]"
  
  /** encloses string in Curly brackets */
  def enCurly: String = "{" + thisString + "}" 
  
  def words: Array[String] = thisString.split("\\s+")
  def toLowerWords: Array[String] = thisString.toLowerCase.words
  
  def remove2ndDot: String =
  { val (s1, s2) = thisString.span(_ != '.')         
    val (s2a, s2b) = s2.drop(1).span(_ != '.')
    s1 + "." + s2a
  }
  
  def toTokens: EMon[Arr[pParse.Token]] = pParse.stringToTokens(thisString)
  /** Appends strings with a comma and space seperator */
  def appendCommas(extraStrings: String*): String = extraStrings.foldLeft(thisString)(_ + ", " + _)

  /** Appends extra Strings to thisString separated by " ;". */
  def appendSemicolons(extraStrings: String*): String =
  { val v1 = extraStrings.foldLeft(thisString)(_ + "; " + _)
    extraStrings.length match
    { case 0 => ife(thisString == "", v1 + ";", v1)
      case _ => ife(extraStrings.last == "", v1 + ";", v1)
    }
  }

  def commaInts(ints: Int*): String = ints.foldLeft(thisString)(_ + ", " + _.toString)

  def dotAppend(extraStrings: String*): String = extraStrings.foldLeft(thisString)(_ + "." + _)  
  def appendParenthSemis(innerStrs: String*): String = thisString + innerStrs.semiParenth

  def prependIndefiniteArticle = thisString.find(!_.isWhitespace) match
  { case Some(ch) => ch.toLower match
    { case 'a' | 'e' | 'i' | 'o' | 'u' => "an " + thisString
      case _ => "a " + thisString
    }
    case _ => "a " + thisString
  }

  def lengthFix(newLenIn: Int = 3, packChar: Char = ' '): String = 
  { val newLen = newLenIn.min(1).max(9)
    (newLen - thisString.length) match {
      case l if l < 0 => thisString.take(newLen)
      case 0 => thisString
      case l if l.isEven => packChar.timesString(l / 2) + thisString + packChar.timesString(l / 2)
      case l => packChar.timesString(l / 2) + thisString + packChar.timesString(l / 2 + 1)
    }
  }
  
  /** Converts this String to an immutable Array based collection of [[Char]]s. */
  def toChars: Chars = new Chars(thisString.toCharArray)
  
  /** Replaces the reserved HTML characters with their corresponding entities, in order to display XML code as text. Eg '>' is replaced by "&gt;". */
  def htmlReservedSubstitute: String = toChars.foldLeft(""){ (acc, el) => acc + el.htmlReservedSubstituion }
}