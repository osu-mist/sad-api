/* CodeNarc RuleSet
   http://codenarc.sourceforge.net/codenarc-rule-index.html */

ruleset {
    description 'MIST Groovy RuleSet'

    /* class name starts with an uppercase letter and is followed by zero or
       more word characters or dollar signs */
    ClassName

    /* method name starts with a lowercase letter */
    MethodName

    /* package name consists only of lowercase letters and numbers, separated
       by periods */
    PackageName

    /* semicolons as line terminators are not required in Groovy */
    UnnecessarySemicolon

    /* If a statement is the last line in a method or closure then you do not need to have the return keyword. */
    UnnecessaryReturnKeyword

    /* Checks that if statements use braces, even for a single statement. */
    IfStatementBraces

    /* Checks that else blocks use braces, even for a single statement. */
    ElseBlockBraces

    /* Checks that for statements use braces, even for a single statement. */
    ForStatementBraces

    /* Checks that while statements use braces, even for a single statement. */
    WhileStatementBraces
}
