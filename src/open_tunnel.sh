#!/bin/zsh
# Yo this file is just for remote development
# Just opens SSH tunnel so that can connect to
# EECS mysql server
# If you run into timeout issues probably not
# on eecs network and should use this
# change username to make this work for you
ssh -L 3306:localhost:3306 charrod@45.79.39.50
