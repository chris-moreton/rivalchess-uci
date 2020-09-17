sed -i "s/def engine_version = .*/def engine_version = '$1'/g" build.gradle
sed -i "s/def uci_build = .*/def uci_build = '$2'/g" build.gradle
./gradlew build
git add -A
git commit -m "Version $1-$2"
cp build/libs/rivalchess-$1-$2.jar ~/Chess/ && chmod +x ~/Chess/*.jar
