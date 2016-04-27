Start [eureka-server](../eureka-server) and [blog-api](../blog-api) in advance, then

    $ mvn clean package -Dmaven.test.skip=true
    $ cf login -a api.<your CF target>
    $ cf push --no-start
    $ cf set-env feed CF_TARGET https://api.<your CF target>
    $ cf start feed