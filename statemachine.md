# Vento State Machine

This is a model of the Vento state machine. It's intended for plugin development purposes only.

## State Diagram
```mermaid
---
title: Vento State Machine
config:
  theme: 'forest'
  themeVariables:
    darkmode: 'false'
    background: '#ffffff'
---
stateDiagram-v2
    direction LR
    state "{{" as open
    state "}}" as close
    state "#" as hash
    state "#" as hashclose
    state "#-" as hashdash
    state "-#" as dashhash
    state ">" as gt
    state "-" as dash
    state "[comment]" as comment
    state "[JavaScript]" as js
    state "[js expression]" as expression
    state "for" as for
    state "set" as set
%%
    state "if" as if
    state "else" as else
    state "else if" as elseif
    state "[js expression]" as ifExpression
%%
    state "include" as include
    state "layout" as layout
    state "function" as function
    state "import" as import
    state "export" as export
%%
    state "|>" as pipe
    state "[pipe expression]" as pipeExpression
%%   set [name] = [value]
    state "[name]" as name
    state "=" as equals
    state "[value]" as value
%%   for [value] of [collection]
    state "[value]" as forValue
    state "of" as of
    state "[collection]" as collection
%%

    state "/for|set|if|function|export" as closeExpression
%%

    [*] --> open
    open --> close
    open --> closeExpression
    closeExpression --> close
%%

    open --> hash
    hash --> comment
    comment --> hashclose
    hashclose --> close
%%
    state "comment" as trimComment
    open --> hashdash
    hashdash --> trimComment
    trimComment --> dashhash
    dashhash --> close
%%

    open --> gt
    gt --> js
    js --> close
    open --> dash
    open --> expression
%%

    dash --> expression
    expression --> pipe
    pipe --> pipeExpression
    pipeExpression --> close
    expression --> close
%%

    open --> for
    for --> forValue
    forValue --> of
    of --> collection
    collection --> close
    collection --> pipe
%%

    open --> set
    set --> name
    name --> equals
    equals --> value
    value --> close
    value --> pipe
%%

    open --> if
    if --> ifExpression
    open --> else
    else --> close
    open --> elseif
    elseif --> ifExpression
    ifExpression --> close
%%
    state "[filepath]" as file
    open --> include
    include --> file
    file --> data
    data --> close
    file --> pipe
    file --> close
%%
    state "[slot name]" as slotName
    open --> layout
    layout --> file
    open --> slot
    slot --> slotName
    slotName --> close
    slot --> close
%%

    state "[name]([args])" as functionSignature
    open --> function
    open --> async
    async --> function
    function --> functionSignature
    functionSignature --> close
%%
    state "[name]([args])" as functionCall
    open --> functionCall
    open --> await
    await --> functionCall
    functionCall --> close
%%
    state "[variable|{variable}]" as importVariable
    state "[file path]" as importFile
    open --> import
    import --> importVariable
    importVariable --> from
    from --> importFile
    importFile --> close
%%
    state "variable" as exportVar
    state "=" as exportEquals
    state "[value]" as exportValue
    open --> export
    export --> exportVar
    export --> function
    export --> async
    exportVar --> exportEquals
    exportVar --> pipe
    exportEquals --> exportValue
    exportValue --> close
%%
    close --> [*]

```

## Vento clause

- `{{`
- `}}`

## Vento Clause Prefix

- `for`
- `set`
- `include`
- layout
  - `layout "[file path]"`
- set
  - `set [name] = [value]`
- slot
    - `slot`
    - `slot name`
- if
    - `if`
    - `else`
    - `else if`
- comments
  - `#` - comment
  - `#-` - trimmed comment
- JavaScript
  - ` ` - JS expression
  - `-` - Trimmed JS expression
  - `>` - JS block

## Vento Expression suffix

- `#`
- `-#`

## Vento closing tags

- `/for`
- `/set`
- `/if`
- `/function`
- `/export`

## JS Clauses

- `[js expression]`
- `[name]([args])`
- for
  - `[value] of [collection]`
- export
  - `[variable]`
  - `async function [name]([args])`
  - `function [name]([args])`
  - `function [name]`
- function
  - `[name]([args])`
  - `[name]`
  - `async function [name]([args])`
  - `async function [name]`
  - `await [name]([args])`
  - `await [name]`
- import
  - `[variable] from "[file path]"`
  - `{[variable]} from "[file path]"`

## Vento Pipe suffix

- `|> [pipe expression]`
