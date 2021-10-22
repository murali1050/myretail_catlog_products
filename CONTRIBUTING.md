# Contributing to catalog

This is a short guide on how to contribute to the project.

## Submitting a pull request

If you find a bug that you'd like to fix, or a new feature that you'd like to implement then please submit a pull request via Bitbucket/Stash.

Now in your terminal

    git clone ssh://git@gitscm.myretail.com/retail/catalog.git
    cd catalog

Make a branch to add your new feature

    git checkout -b my-new-feature develop

And get hacking.

When ready - run the unit tests for the code you changed

    make test

Make sure you

  * Add documentation for a new feature
  * Add unit tests for a new feature
  * squash commits down to one per feature
  * rebase to develop `git rebase develop`

When you are done with that

    git push origin my-new-feature

Go to the Bitbucket website and click [Create pull request](https://confluence.atlassian.com/bitbucket/create-a-pull-request-774243413.html).

You patch will get reviewed and you might get asked to fix some stuff.

If so, then make the changes in the same branch, squash the commits, rebase it to develop then push it to Bitbucket with `--force`.

## Test

Tests are run using a testing framework, so at the top level you can run this to run all the tests.

**Assumes that you have Docker for Mac / Docker for Windows installed.**

```bash
# runs all tests (includes formatting and linting)
make test
# run all tests and generates code coverage (includes formatting and linting)
make cover
# builds the runnable jar (includes formatting and linting)
make build
```

## Making a release

[Release](RELEASE.md)
