#!/bin/bash
if [ -z "$2" ]; then
	echo "$0: usage: $0 <module id> <module name>"
	exit 1
fi
cp -r src/main/resources/lockpick src/main/resources/$1
mv src/main/resources/$1/lib39-{lockpick,$1}.mixins.json
mkdir -p src/main/java/com/unascribed/lib39/$1{,/mixin}
cp src/main/java/com/unascribed/lib39/lockpick/Lib39LockpickMixin.java src/main/java/com/unascribed/lib39/$1/Lib39$2Mixin.java
sed -i "s/lockpick/$1/g" src/main/resources/$1/* src/main/java/com/unascribed/lib39/$1/Lib39$2Mixin.java
sed -i "s/Lockpick/$2/g" src/main/resources/$1/* src/main/java/com/unascribed/lib39/$1/Lib39$2Mixin.java
