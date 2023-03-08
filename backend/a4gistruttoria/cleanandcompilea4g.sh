
echo ~
which java
echo $JAVA_HOME;

export JAVA_HOME="/c/Program Files/Java/jdk-11.0.10";
export PATH="/c/Program Files/Java/jdk-11.0.10/bin:$PATH";
#setx /M JAVA_HOME "C:/Program Files/Java/jdk-11.0.10"
#set JAVA_HOME=C:/Program Files/Java/jdk-11.0.10
#set PATH=%PATH%;%JAVA_HOME%/bin

which java

echo clean;
mvn clean

#echo compile;
#mvn install;

echo "package with skip test"
mvn package -Dmaven.test.skip=true
#mvn package

