grammar Statements;

@header{package framework.parser;}


fragment A      : [aA];
fragment B      : [bB];
fragment C      : [cC];
fragment D      : [dD];
fragment E      : [eE];
fragment F      : [fF];
fragment G      : [gG];
fragment H      : [hH];
fragment I      : [iI];
fragment J      : [jJ];
fragment K      : [kK];
fragment L      : [lL];
fragment M      : [mM];
fragment N      : [nN];
fragment O      : [oO];
fragment P      : [pP];
fragment Q      : [qQ];
fragment R      : [rR];
fragment S      : [sS];
fragment T      : [tT];
fragment U      : [uU];
fragment V      : [vV];
fragment W      : [wW];
fragment X      : [xX];
fragment Y      : [yY];
fragment Z      : [zZ];
fragment HEX_DIGIT:                  [0-9A-F];
fragment DIGIT:                  [0-9];
fragment LETTER:                         [a-zA-Z];

COLON: ':';
SEMICOLON: ';';
QUIT: '!';
BACKGROUND: '&';
JOIN: '<';
BLANK: ' ';

WS : [ \t]+ -> skip ; // skip spaces, tabs
END_LINE    : '\r'? '\n';
M_COMMENT   : '/*' .*? '*/' -> channel(HIDDEN) ;      // Multiline comment
S_COMMENT   : ('--' |'//')  .*? (EOF | '\r'?  '\n') ; // Single line comment

SESSION_ID: '(' (LETTER | '_' | DIGIT )+ (':' DIGIT+)?')';

ENV_VARIABLE_NAME: '$' (LETTER | '_' | DIGIT )+;
ENV_SHELL_EXPR: '$' '(' (('\\' ';') | ~(';'))+ ')';

// test annotation
TEST_ANNOTATION: '@' (LETTER | '_' | DIGIT )+;
//TEST_BEFORE: '@' B E F O R E T E S T;
//TEST_AFTER: '@' A F T E R T E S T;

// SQL_STRING: A string that does not start with the following characters:
//    ' ': conflict with empty_stmt
//    '(': conflict with SESSION_ID
//    '&'/'<'/'!':  conflict with operator of session sql
//    ';': SQL delimiter
//    END_LINE: line delimiter
//    '-': conflict with S_COMMENT
//    '/': conflict with M_COMMENT
// and not contains the following characters:
//    ';': SQL delimiter
//    '$': conflict with ENV_VARIABLE_NAME
//    '=': sql can not start with '='
//    '@': confict with TEST_ANNOTATION
SQL_STRING: ~[ @=$(&<!;\r\n/-] (('\\' ';') | ~(';'))*;
// SHEEL_COMMAND: '!' (('\\' '\n') | ~('\n'))+ '!';
SHEEL_COMMAND: '!' (('\\' ';') | ~(';'))+ ';';

script:
    test_annotation_stmt? statement+ test_annotation_stmt?;

statement:
      sql_stmt sql_end
    | session_sql_stmt sql_end
    | single_comment
    | empty_stmt
    | shell_command
    | env_var_stmt
    | test_annotation_stmt
    ;

test_annotation_stmt:
    TEST_ANNOTATION;
//test_before_stmt:
//    TEST_BEFORE;
//test_after_stmt:
//    TEST_AFTER;

env_var_stmt:
    (env_var_value_stmt | env_var_shell_stmt) sql_end;

env_var_value_stmt:
    ENV_VARIABLE_NAME '=' SQL_STRING;
env_var_shell_stmt:
    ENV_VARIABLE_NAME '=' ENV_SHELL_EXPR;

session_sql_stmt:
    (  ident JOIN
    | ident QUIT
    | ident BACKGROUND? sql_stmt
    );


sql_stmt:
    blanks SQL_STRING;

sql_end:
    SEMICOLON;

empty_stmt:
    BLANK* END_LINE;

shell_command:
    SHEEL_COMMAND;

blanks:
    BLANK*;

ident:
    SESSION_ID
    ;

single_comment:
    S_COMMENT;
