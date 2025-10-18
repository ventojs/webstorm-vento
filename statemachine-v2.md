# Vento State Machine

This is a model of the Vento state machine. It's intended for plugin development purposes only.

## State Diagram
```mermaid
---
title: Comment
config:
  theme: 'forest'
  themeVariables:
    darkmode: 'false'
    background: '#ffffff'
---
stateDiagram-v2
    direction LR
    state "#" as ohash
    state "#" as chash

    state "-" as ocdash
    state "-" as ccdash
    state "[comment]" as comment

    [*] --> open1

    close1 --> [*]

state "{{" as open1
state "}}" as close1
    open1 --> ohash

state "Comment" as grComment {
    ocdash --> comment
    comment --> ccdash

    ohash --> ocdash

    ohash --> comment
    comment --> chash

    ccdash --> chash
}
    chash --> close1

```

```mermaid
---
title: Variable
config:
  theme: 'forest'
  themeVariables:
    darkmode: 'false'
    background: '#ffffff'
---
stateDiagram-v2
    direction LR

    state "|>" as pipe
    state "[pipe expression]" as pipeExpression

    [*] --> open2

    close2 --> [*]

%% variable
    state "{{" as open2
    state "}}" as close2
    state "-" as odash2
    state "-" as cdash2
    open2 --> odash2
    open2 --> expression
    open2 --> vawait
    odash2 --> expression
    odash2 --> vawait
    vawait --> expression

state "Variable" as grVariable {
    state "[js expression]" as expression
    state "await" as vawait
    state "echo" as echo
    state "/echo" as cecho
}
    expression --> pipe


    pipe --> pipeExpression

    cdash2 --> close2
    expression -->close2
    pipeExpression --> close2

%% echo

    open2 --> echo
    odash2 --> echo
    echo --> close2

    open2 --> cecho
    cecho --> cdash2
    cdash2 --> close2
    cecho --> close2


```

```mermaid
---
title: Javascript
config:
  theme: 'forest'
  themeVariables:
    darkmode: 'false'
    background: '#ffffff'
---
stateDiagram-v2
    direction LR
    state "[JavaScript]" as js

    [*] --> open3

    close3 --> [*]
%% Javascript
    state "{{" as open3
    state "}}" as close3
    open3 --> gt
state "JavaScript" as grJavaScript {
state ">" as gt
    gt --> js
}
    js --> close3


```


```mermaid
---
title: Variable
config:
  theme: 'forest'
  themeVariables:
    darkmode: 'false'
    background: '#ffffff'
---
stateDiagram-v2
    direction LR

    state "[js expression]" as setexp

    state "set" as set

    state "|>" as pipe
    state "[pipe expression]" as pipeExpression

    state "=" as equals

    [*] --> open8

    close8 --> [*]


%% set
state "{{" as open8
state "}}" as close8
    open8 --> set
state "Set" as grSet {
    state "/set" as cset
    set --> equals
    equals --> setexp
}
    setexp --> pipe
    pipe --> pipeExpression
    pipeExpression --> close8
    setexp --> close8

    open8 --> cset
    cset --> close8


```


```mermaid
---
title: For
config:
  theme: 'forest'
  themeVariables:
    darkmode: 'false'
    background: '#ffffff'
---
stateDiagram-v2
    direction LR

    state "|>" as pipe
    state "[pipe expression]" as pipeExpression

    state "[value]" as forValue
    state "of" as of
    state "[collection]" as collection

    [*] --> open9

    close9 --> [*]

%% for
state "{{" as open9
state "}}" as close9
    open9 --> for

state "For" as grFor {
    state "/for" as cfor
    for --> await
    await --> forValue
    for --> forValue
    forValue --> of
    of --> collection
}
    collection --> close9
    collection --> pipe
    pipe --> pipeExpression
    pipeExpression --> close9

    open9 --> cfor
    cfor --> close9


```



```mermaid
---
title: If /  If Else / Else
config:
  theme: 'forest'
  themeVariables:
    darkmode: 'false'
    background: '#ffffff'
---
stateDiagram-v2
    direction LR


    [*] --> open6

    close6 --> [*]

    state "[js expression]" as ifexp
%% if
    state "{{" as open6
    state "}}" as close6
    open6 --> if
state "If" as grIf {
    state "if" as if
    state "else" as else
    state "else if" as elseif
    state "/if" as cif
    if --> ifexp
    elseif --> ifexp
}
    open6 --> elseif

    open6 --> else
    else --> close6


    ifexp --> close6
    open6 --> cif
    cif --> close6



```

```mermaid
---
title: Include
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
    state "[js expression]" as includeexp
    state "include" as include

    state "|>" as pipe
    state "[pipe expression]" as pipeExpression

    [*] --> open
    close --> [*]

%% include

    open --> include
state "Include" as grInclude {
    include --> includeexp
    includeexp --> data
    }
    includeexp --> pipe
    pipe --> pipeExpression
    pipeExpression --> close
    data --> pipe
    data --> close

```


```mermaid
---
title: Layout and Slot
config:
  theme: 'forest'
  themeVariables:
    darkmode: 'false'
    background: '#ffffff'
---
stateDiagram-v2
    direction LR

    state "[js expression]" as layoutexp
    state "layout" as layout

    [*] --> open7

    close7 --> [*]

%% layout
    state "{{" as open7
    state "}}" as close7
    state "-" as odash7
    state "-" as cdash7
    open7 --> layout
state "Layout" as grLayout {
    state "/layout" as clayout
    layout --> layoutexp
    layoutexp --> data
}
    slot --> slotName
    data --> close7
    layoutexp --> close7

    open7 --> clayout
    clayout --> close7

state "Slot" as slotName {
    state "/slot" as cslot
    state "slot" as slot

 }
    open7 --> slot
    open7 --> odash7
    odash7 --> slot


    slotName --> close7
    slot --> close7

    open7 --> cslot
    cslot --> close7
    cslot --> cdash7
    cdash7 --> close7



```

```mermaid
---
title: Function
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
    state "function" as function
    state "function signature" as functionSignature

    [*] --> open
    close --> [*]

%% function
    open --> function
    open --> async
state "Function" as grFunction {
    state "/function" as cfunction
    async --> function
    function --> functionSignature
    }
    functionSignature --> close

    open --> cfunction
    cfunction --> close

```

```mermaid
---
title: Export
config:
  theme: 'forest'
  themeVariables:
    darkmode: 'false'
    background: '#ffffff'
---
stateDiagram-v2
    direction LR

    state "[js expression]" as exportexp
    state "function" as exportfunction
    state "function signature" as exportfunctionSignature
    state "export" as export
%%

    state "|>" as pipe
    state "[pipe expression]" as pipeExpression

    state "=" as equals


    [*] --> open5

    close5 --> [*]

%% export
state "{{" as open5
state "}}" as close5

    open5 --> export
state "Export" as grExport {
    state "/export" as cexport
    export --> identifier
    identifier --> equals
    equals --> exportexp
    export --> exportfunction
    exportfunction --> exportfunctionSignature
    }
    exportexp --> pipe
    pipe --> pipeExpression
    pipeExpression --> close5
    exportexp --> close5

    exportfunctionSignature --> close5

    open5 --> cexport
    cexport --> close5

```



```mermaid
---
title: Import
config:
  theme: 'forest'
  themeVariables:
    darkmode: 'false'
    background: '#ffffff'
---
stateDiagram-v2
    direction LR


    state "{ a, b }" as destructimport
    state "default" as defaultimport
    state "from" as from


    state "import" as import


    [*] --> open4

    close4 --> [*]

%% import
state "{{" as open4
state "}}" as close4
    open4 --> import
state "Import" as grImport {
    import --> destructimport
    destructimport --> from
    import --> defaultimport
    defaultimport --> from
    from --> file
    }
    file --> close4




```
