# Agent instructions

## Commit conventions

- One commit per issue. Do not bundle fixes for multiple issues into a single commit, even when they're small and related — each issue needs its own entry in history so the changelog can map commits to issues.
- Use conventional commit format for the subject line: `type(scope): description`. Common types in this repo: `fix`, `feat`, `chore`.
- End the subject line with the issue number in parentheses, e.g. `fix(lexer): support single quotes in include/import/layout paths (#220)`.
- Include a `Fixes #NNN` (or `Closes #NNN`) line in the commit body so the issue is auto-closed when merged.
