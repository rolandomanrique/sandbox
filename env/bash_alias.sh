#!/bin/bash
NOCOLOR=$(tput sgr0)
COLOR=$(tput setaf 2)

export GIT_USER=rolandomanrique

function assert_git {
  git rev-parse --git-dir > /dev/null 2>&1
  if [ "$?" -eq "0"  ]; then echo -n ""; else echo -n "1" ; fi
}


function parse_git_branch {
  git branch --no-color 2> /dev/null | sed -e '/^[^*]/d' -e 's/* \(.*\)/(\1)/'
}

function git-reset-origin {
  if [ ! -z "$(assert_git)" ] ; then echo "Not a git repo!" ; return 1; fi
  if [ -z "$1" ] ; then echo "Usage: git-reset-origin <git-org-name>" ; return 1; fi

  GIT_ORG=$1
  git checkout master && git fetch --all -p && git reset --hard $GIT_ORG/master && git push origin +master
}

function git-add-remote {
  if [ ! -z "$(assert_git)" ] ; then echo "Not a git repo!" ; return 1; fi
  if [ -z "$1" ] ; then echo "Usage: git-add-remote <git-org-name>" ; return 1; fi

  GIT_ORG=$1
  ORIGIN_ORG=`git remote -v |grep origin |grep '(fetch)'|awk -F ':' '{print $2}' |awk -F '/' '{print $1}'`
  ORIGIN_REP=`git remote -v |grep origin |grep '(fetch)'|awk -F ':' '{print $2}' |awk -F '[/ ]' '{print $2}'`
  GIT_URL=`git remote -v |grep origin |grep '(fetch)'|awk '{print $2}' |awk -F ':' '{print $1}'`
  if [ "$ORIGIN_ORG" == "$GIT_USER" ]; then
    UPSTREAM_URL=$GIT_URL:$GIT_ORG/$ORIGIN_REPO
    echo "${NOCOLOR}Adding $GIT_ORG ${COLOR}$UPSTREAM_URL${NOCOLOR}"
    git remote add $GIT_ORG $UPSTREAM_URL
  else
    echo "${NOCOLOR}Not a fork of $GIT_USER! ==> ${COLOR}$ORIGIN_ORG{$NOCOLOR}"
  fi
}

function github-pull-request {
  if [ ! -z "$(assert_git)" ] ; then echo "Not a git repo!" ; return 1; fi
  if [ -z "$1" ] ; then echo "Usage: github-pull-request <git-org-name>" ; return 1; fi

  GIT_ORG=$1
  TARGET_BRANCH=${2:-master}
  CURRENT_BRANCH=$(eval parse_git_branch |sed 's/[()]//g')

  ORIGIN_ORG=`git remote -v |grep origin |grep '(fetch)' |awk -F ':' '{print $2}' |awk -F '/' '{print $1}'`
  ORIGIN_REP=`git remote -v |grep origin |grep '(fetch)' |awk -F ':' '{print $2}' |awk -F '[/ ]' '{print $2}'`
  TARGET_ORG=`git remote -v |grep $GIT_ORG |grep '(push)' |awk -F ':' '{print $2}' |awk -F '/' '{print $1}'`
  TARGET_REP=`git remote -v |grep $GIT_ORG |grep '(push)' |awk -F ':' '{print $2}' |awk -F '[/ \.]' '{print $2}'`
  TARGET_REP=${TARGET_REP/git@/https://}
  GIT_URL=`git remote -v |grep origin |grep '(fetch)'|awk '{print $2}' |awk -F ':' '{print $1}'`
  GIT_URL=${GIT_URL/git@/https://}

  COMP_STMT="$TARGET_BRANCH...$ORIGIN_ORG:$CURRENT_BRANCH"
  echo "Comparing $COMP_STMT"
  open "$GIT_URL/$TARGET_ORG/$TARGET_REP/compare/$COMP_STMT"  
}

function github-open {
  if [ ! -z "$(assert_git)" ] ; then echo "Not a git repo!" ; return 1; fi

  GIT_ORG=${1:-origin}
  GIT_BRANCH=${2:-master}

  TARGET_ORG=`git remote -v |grep $GIT_ORG |grep '(fetch)' |awk -F ':' '{print $2}' |awk -F '/' '{print $1}'`
  TARGET_REP=`git remote -v |grep $GIT_ORG |grep '(fetch)' |awk -F ':' '{print $2}' |awk -F '[/ \.]' '{print $2}'`
  TARGET_REP=${TARGET_REP/git@/https://}
  GIT_URL=`git remote -v |grep origin |grep '(fetch)'|awk '{print $2}' |awk -F ':' '{print $1}'`
  GIT_URL=${GIT_URL/git@/https://}

  open "$GIT_URL/$TARGET_ORG/$TARGET_REP/tree/$GIT_BRANCH"
}

alias dockerbash='docker run --rm -it --entrypoint /bin/bash'
alias editprofile='vi ~/.bash_profile && source ~/.bash_profile'
alias editalias='vi ~/.bash_alias && source ~/.bash_alias'
alias edithosts='sudo bash -c "vi /etc/hosts && killall -HUP mDNSResponder"'

