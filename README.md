# BizHttpRequestTest

This is a helper tool library in developing or testing java web application.

If you are debugging apis with front-end engineers, you may encounter the following situation, they tell you that one of the apis returns an error, then you will ask them to send you their request details, though you have logged the request records in your application. Then you will copy to Postman to testing the reqeust on your localhost, and debugging step by step, this library can help you resolve this problem.


## Quick Start

By default, this library will working with zero configuration. In servlet-api supported web containers, you can add the following maven dependency.

### Maven

    <dependency>
      <groupId>com.github.jerryxia</groupId>
      <artifactId>devhelper</artifactId>
      <version>1.0.27</version>
    </dependency>

if you use spring boot, Add the following dependency.

    <dependency>
      <groupId>com.github.jerryxia</groupId>
      <artifactId>devhelper-spring-boot-autoconfigure</artifactId>
      <version>1.0.27</version>
    </dependency>


## Dashboard

[demo](https://res4gqk.appspot.com/)
[spring-boot demo](http://openshift-quickstarts-requestcapture.1d35.starter-us-east-1.openshiftapps.com/)

![sample01](doc/sample01.gif)




