sed -i "s/def engine_version = .*/def engine_version = '$1'/g" build.gradle
./gradlew build
cp build/libs/rivalchess-$1-1.jar ~/Chess/ && chmod +x ~/Chess/*.jar
