#!/bin/bash

export LD_PRELOAD=libswipl.so:${LD_PRELOAD}
export LD_LIBRARY_PATH=/usr/lib/swi-prolog/lib/amd64/:${LD_LIBRARY_PATH}


