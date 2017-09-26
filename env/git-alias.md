~/.gitconfig:

```
[include]
        path = path/to/alias.git
```

alias.git:

```
[alias]
  st = status -s -b
  ci = commit
  br = branch -v
  brr = branch -vr
  co = checkout
  df = diff
  lg = log -p
  fetch-all-purge = fetch --all -p
  hist = log --pretty=format:\"%h %ad | %s%d [%an]\" --graph --date=short
```
