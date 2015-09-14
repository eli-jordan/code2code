
# the git user the repo belongs to
GH_USERNAME="eli-jordan"

# the repo name
GH_REPO="code2code.git"

# the name of the branch used to push the 
GH_BRANCH="build-artifacts"

# The main entry point
main() {
  if [ "$TRAVIS_PULL_REQUEST" == "false" ]
  then
    push_artifacts
  else
    echo "Not pushing artifacts for pull request"
  fi
}

push_artifacts() {
  echo "Starting to push build artifacts"
    
  local all_targets="$HOME/artifacts_build_${TRAVIS_BUILD_NUMBER}_of_${TRAVIS_BRANCH}_branch_on_$(date -I)"
  mkdir "$all_targets"

  find . -type d -name 'target' | while read targetDir
  do
    echo "copying from $targetDir to $all_targets"
    cp -R --parents "$targetDir" "$all_targets"
  done

  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "Travis"

  if [ -e "$HOME/$GH_BRANCH/.git" ]
  then
     echo "Updating git repo using 'git pull'"
     cd "$HOME/$GH_BRANCH"
     git pull -f --quiet https://${GH_TOKEN}@github.com/${GH_USERNAME}/${GH_REPO} ${GH_BRANCH} > /dev/null
  else
     echo "Fetching git repo using 'git clone'"
     git clone --quiet --branch=${GH_BRANCH} https://${GH_TOKEN}@github.com/${GH_USERNAME}/${GH_REPO} ${GH_BRANCH} > /dev/null
  fi

  cd "$HOME/$GH_BRANCH"
  mv "$all_targets" .

  echo "Commiting and pushing new build artifacts"
  git add .
  git commit -m "Travis build $TRAVIS_BUILD_NUMBER pushed to $GH_BRANCH"
  git push -fq origin ${GH_BRANCH} > /dev/null

  echo "Completed pushing artifacts"
}

# run the script
main

