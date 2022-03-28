Bash complete:

```
mkdir ~/.zsh
curl -o ~/.zsh/git-completion.bash https://raw.githubusercontent.com/git/git/master/contrib/completion/git-completion.bash
curl -o ~/.zsh/_git https://raw.githubusercontent.com/git/git/master/contrib/completion/git-completion.zsh
```

~/.zshrc

```
# Load Git completion
zstyle ':completion:*:*:git:*' script ~/.zsh/git-completion.bash
fpath=(~/.zsh $fpath)

autoload -Uz compinit && compinit

# Load version control information
autoload -Uz vcs_info
precmd() { vcs_info }

# Format the vcs_info_msg_0_ variable
zstyle ':vcs_info:git:*' formats '(%b)'
 
# Set up the prompt (with git branch name)
setopt PROMPT_SUBST
PROMPT='%n@${PWD/#$HOME/~} ${vcs_info_msg_0_}: '

# CDPATH
setopt auto_cd
cdpath=($HOME/dev)
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
