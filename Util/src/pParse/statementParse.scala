package ostrat
package pParse


/** Function object to parse a raw Statement of statement members, where sub blocks have already been parsed into Statement Blocks. */
object statementParse
{
  /** Parses a sequence of Statement members into a Statement. Statement members are either nonBracketTokens or parsed BracketBlocks.  */
  def apply(memsIn: Arr[StatementMember], optSemi: OptRef[SemicolonToken]): EMon[Statement] =
  {
    implicit val inp = memsIn
    val acc: Buff[Clause] = Buff()
    val subAcc: Buff[ClauseMember] = Buff()

    def loop(rem: ArrOff[StatementMember]): EMon[Statement] = rem match {
      case ArrOff0() if acc.isEmpty => {getExpr(subAcc.toRefs).map(g => MonoStatement(g, optSemi))}
      case ArrOff0() if subAcc.isEmpty => Good(ClausedStatement(acc.toRefs, optSemi))
      case ArrOff0() => getExpr(subAcc.toRefs).map(g => ClausedStatement(acc.append(Clause(g, NoRef)).toRefs, optSemi))
      case ArrOff1Tail(ct: CommaToken, tail) if subAcc.isEmpty => { acc.append(EmptyClause(ct)); loop(tail) }
      case ArrOff1Tail(ct: CommaToken, tail) => getExpr(subAcc.toRefs).flatMap{ g =>
        acc.append(Clause(g, OptRef(ct)))
        loop(tail)
      }
      case ArrOff1Tail(em: ClauseMember, tail) => { subAcc.append(em); loop(tail) }
    }

    loop(inp.offset0)
  }
}
