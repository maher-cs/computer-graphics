#!/bin/bash

rm -r code-upload
mkdir code-upload
cp --parents $(find . | grep java$ | grep -v code-example | grep -v double-pendulum) code-upload/
