
grammar Nginx;
/* Lexical rules */
LBRACE  : '{' ;

RBRACE  : '}' ;

SEMI    : ';' ;

IDENT
    : [a-zA-Z_./] [a-zA-Z0-9_./:-]*
    ;

NUMBER
    : [0-9]+
    ;
STRING
    : '"' (~["\r\n])* '"'
    ;

COMMENT: '#' ~[\r\n]* -> skip;

/*Whitespace*/
WS
    : [ \t\r\n]+ -> skip
    ;



// ---------- PARSER RULES ----------

config
    : statement* EOF
    ;

simpleDirective
    : IDENT arguments? SEMI
    ;

statement
    : simpleDirective
    | blockDirective
    ;

blockDirective
    : IDENT arguments? LBRACE statement* RBRACE
    ;

arguments
    : (IDENT | STRING | NUMBER)+
    ;
