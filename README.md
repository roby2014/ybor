# ybor
Simple interpreter for my *future* custom programming language (ybor)

## Info

Contains:
- [Lexer/Tokenizer](https://github.com/roby2014/ybor/blob/main/src/main/kotlin/Tokenizer.kt) // Transforms source input into list of tokens
- [Parser](https://github.com/roby2014/ybor/blob/main/src/main/kotlin/Parser.kt) // Parses list of tokens into an AST (Abstract Syntax Tree)
- [Interpreter](https://github.com/roby2014/ybor/blob/main/src/main/kotlin/Interpreter.kt) // Evaluates the expressions

Some examples of what it can do at the moment:

```shell
> 1+2 ;
3.0

> print 1+2 ;
3.0

> print 1+2*3 ;
7.0

> print (1+2)*3 ;
9.0

> 1+a
[line 1] ERROR at 'a': Expected valid expression

> print (1*2 ;
[line 1] ERROR at 'EOF': Expected ')'

> "hello" + "world" ;
AST: (+ hello world)
Result: helloworld

> "cool interpreter" - "cool " ;
AST: (- cool interpreter cool)
Result: interpreter

> -5 ;
AST: (- 5.0)
Result: -5.0

> -"invalid_unary" ;
AST: (- invalid_unary)
[line 1] ERROR at '-': Operand must be a number
Result: null

> 2 > 2 ;
AST: (> 2.0 2.0)
Result: false

> 2 >= 2 ;
AST: (>= 2.0 2.0)
Result: true

> 2 == 2 ;
AST: (== 2.0 2.0)
Result: true

> "abc" == "abc" ;
AST: (== abc abc)
Result: true

> "abc" != "cba" ;
AST: (!= abc cba)
Result: true

> var a = 3;
> print a;
3.0

> var b;
> b = 5;
5.0

> a == b;
false

> a = b;
5.0

> a == b;
true
```

## why?
Lately I've been reading about Compilers / Interpreters, so this repository contains a basic project which
I've been developing while reading *Crafting Interpreters* by Robert Nystrom. The code structure does not follow 100% the book's one.
Use this as educational purposes *if you can*.

In case of any suggestion/error, create a issue/pull request.