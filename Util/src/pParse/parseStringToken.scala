package ostrat
package pParse

object parseStringToken
{ /** Parses String (with RSON syntax) searching for the String terminator. Returns error if end of file found first.*/
  def apply(rem: CharsOff, tp: TextPosn)(implicit charArr: Chars): EMon3[CharsOff, TextPosn, StringToken] =
  {
    val strAcc: StringBuilder = new StringBuilder()

    def loop(rem: CharsOff): EMon3[CharsOff, TextPosn, StringToken] = rem match
    { case CharsOff0() => tp.bad("Unclosed String")
      case CharsOff1Tail('\"', tail2) => Good3(tail2, tp.right(strAcc.length + 2),  StringToken(tp, strAcc.mkString))
      case CharsOff1('\\') =>  tp.bad("Unclosed String ending with unclosed escape Sequence")
      case CharsOff2Tail('\\', c2, tail) if Array('\"', '\n', '\b', '\t', '\f', '\r', '\'', '\\').contains(c2) => { strAcc.append(c2); loop(tail) }
      case CharsOffHead2('\\', c2) => tp.bad("Unrecognised escape Sequence \\" + c2.toString)
      case CharsOff1Tail(h, tail2) => { strAcc.append(h); loop(tail2) }
    }
    rem match
    {
      case CharsOff1Tail('\"', tail) => loop(tail)
      case _ => tp.bad("These characters do not begin with a String opening delimitter.")
    }

  }
}