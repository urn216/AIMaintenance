javac src/mki/core/*.java src/mki/parser/*.java src/mki/io/*.java src/mki/math/*.java src/mki/math/matrix/*.java src/mki/math/vector/*.java src/mki/ui/components/*.java src/mki/ui/components/interactables/*.java src/mki/ui/control/*.java src/mki/ui/elements/*.java -d bin

cd bin

jar cfm ../versions/Game.jar ../data/compiler/manifest.txt mki ../data

java -jar ../versions/Game.jar