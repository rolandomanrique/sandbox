Download [auto complete](https://github.com/git/git/blob/master/contrib/completion/git-completion.bash) or `brew install git && brew install bash-completion`


~/.bash_profile

```
if [ -f path/to/git-completion.bash ]; then
  . path/to/git-completion.bash
fi

OR

if [ -f $(brew --prefix)/etc/bash_completion ]; then
  . $(brew --prefix)/etc/bash_completion
fi

function parse_git_branch {
  git branch --no-color 2> /dev/null | sed -e '/^[^*]/d' -e 's/* \(.*\)/(\1)/'
}


export PS1="\u@[\W]\$(parse_git_branch): "
export CLICOLOR=1
export CDPATH=.:$HOME/dev
export SHELL_SESSION_HISTORY=0
```

~/.gitconfig:

```
[include]
        path = ~/.alias.git
```

~/.alias.git:

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

~/.inputrc:

```
set completion-ignore-case on
```