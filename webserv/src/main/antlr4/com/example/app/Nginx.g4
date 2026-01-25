grammar Nginx;

config: directive+;

directive: WORD (argument)* SEMICOLON;

argument: WORD;

WORD: [a-zA-Z0-9_/:.\\-]+ | QUOTED_STRING;

QUOTED_STRING: '"' (~["\r\n])* '"';

SEMICOLON: ';';

WS: [ \t\r\n]+ -> skip;

COMMENT: '#' ~[\r\n]* -> skip;
