# ybor
Simple interpreter for my *future* custom programming language (ybor)

## Info

Contains:
- [Lexer/Tokenizer](https://github.com/roby2014/ybor/blob/main/src/main/kotlin/Tokenizer.kt)
- [Parser](https://github.com/roby2014/ybor/blob/main/src/main/kotlin/Parser.kt)

For now, it can parse single line expressions, respecting grammar precedence.

```
> 1+2
(+ 1.0 2.0)

> 1+2*3
(+ 1.0 (* 2.0 3.0))

> (1+2)*3
(* (group (+ 1.0 2.0)) 3.0)

> 1+a
[line 1] ERROR at 'a': Expected valid expression

> (1*2
[line 1] ERROR at 'EOF': Expected ')'
```

## TODO
- Evaluate expressions (`1+2` should return `3`, etc..)
- Variables, functions, etc....

## why?
Lately I've been reading about Compilers / Interpreters, so this repository contains a basic project which
I've been developing while reading *Crafting Interpreters* by Robert Nystrom. The code structure follows the book's one.
Use this as educational purposes *if you can*.

In case of any suggestion/error, create a issue/pull request.